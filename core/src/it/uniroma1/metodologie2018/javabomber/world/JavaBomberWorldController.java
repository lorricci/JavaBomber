package it.uniroma1.metodologie2018.javabomber.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import it.uniroma1.metodologie2018.javabomber.entities.Bomb;
import it.uniroma1.metodologie2018.javabomber.explosions.Explosion;
import it.uniroma1.metodologie2018.javabomber.interfaces.Block;
import it.uniroma1.metodologie2018.javabomber.interfaces.Entity;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.Level;
import it.uniroma1.metodologie2018.javabomber.interfaces.Teleportable;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.screens.BasicHUD;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberContactListener;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;

/**
 * Gestore della simulazione fisica del gioco.
 * @author Federico Scozzafava + (Lorenzo Ricci)
 *
 */
public class JavaBomberWorldController implements WorldController
{
	
	/**
	 * Lista di entità presenti nel gioco.
	 */
	private List<Entity> entities;
	
	/**
	 * Lista di componenti della mappa presenti nel gioco.
	 */
	private List<Block> enviroment;
	
	/**
	 * Lista da aggiungere alle entità nel prossimo world.step.
	 */
	private List<Entity> toAdd;
	
	/**
	 * Lista da rimuovere dalle entità nel prossimo world.step.
	 */
	private List<Entity> toRemove;
	
	/**
	 * Lista di entità da teletrasportare nel prossimo world.step.
	 */
	private List<Teleportable> toTeleport;
	
	/**
	 * Lista delle bombe presenti all'interno del gioco.
	 */
	private List<Bomb> presentBombs;
	
	/**
	 * Lista delle bombe da rimuovere dal gioco.
	 */
	private List<Bomb> toRemoveBombs;	
	
	/**
	 * World per la simulazione fisica del gioco.
	 */
	private World world;
	
	/**
	 * HUD del gioco.
	 */
	private BasicHUD basicHud;
	
	/**
	 * ContactListener del gioco, gestore delle collisioni.
	 */
	private JavaBomberContactListener contactListener = new JavaBomberContactListener();
	
	/**
	 * Costruisce il gestore della simulazione fisica del gioco.
	 */
	public JavaBomberWorldController() 
	{
		entities = new ArrayList<>();
		toRemove = new ArrayList<>();
		toAdd = new ArrayList<>();
		presentBombs = new ArrayList<>();
		toRemoveBombs = new ArrayList<>();
		toTeleport = new ArrayList<>();
		enviroment = new ArrayList<>();
		world = new World(new Vector2(0,0), true);
		world.setContactListener(contactListener);
	}

	@Override
	public synchronized void update(float deltaTime) 
	{
		if(!world.isLocked()) updateWorldState(deltaTime);
		world.step(1/60f, 6, 2);
	}
	
	/**
	 * Aggiorna lo stato del World, eseguendo tutte le azioni necessarie attraverso i sottometodi
	 * chiamati.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void updateWorldState(float deltaTime)
	{
		manageNewEntities();
		delToRemove();
		presentBombs.removeAll(toRemoveBombs);
		manageTeleports();
		sortForRendering();
		updateState(deltaTime);
	}

	/**
	 * Cancella dal World tutte le entità pronte per la rimozione.
	 * Se una entità corrisponde ad una parte della mappa, viene tolta dalla lista 
	 * "enviroment" corrispondente, invece che dalla lista entities.
	 */
	private void delToRemove()	
	{
		for(Entity e : toRemove)
		{
			if(e instanceof Block) enviroment.remove(e);
			else entities.remove(e);
			cleanSweep(e);
			if(e instanceof GraphicEntity)
			{
				GraphicEntity ge = (GraphicEntity) e;
				ge.disposeTexture();
			}
		}
		toRemove.clear();
	}
	
	/**
	 * Ordina la lista di entità del world per il rendering ordinato.
	 * Il rendering avviene dall'alto verso il basso, in modo che le entità
	 * più in alto vengano coperte da quelle in basso. Riordina poi la lista 
	 * per fare in modo che le esplosioni vengano sempre renderizzate per ultime, passando
	 * sopra qualsiasi altra entità.
	 */
	private void sortForRendering()
	{
		//SORT PER ALTEZZA
		entities.sort((ent1, ent2)-> {
			return (int) (ent2.getPosition().y - ent1.getPosition().y);	
		});
		
		//SORT DELLE ESPLOSIONI
		entities.sort((ent1, ent2) -> {
			if(ent1 instanceof Explosion && !(ent2 instanceof Explosion)) return 1;
			else if(!(ent1 instanceof Explosion) && ent2 instanceof Explosion) return -1;
			else return 0;
		});
	}
	
	/**
	 * Aggiunge al World tutte le unità in coda per essere aggiunte.
	 */
	private void manageNewEntities()
	{
		toAdd.forEach(ent -> {
			if(ent instanceof Block)	enviroment.add((Block) ent);
			else entities.add(ent);
		});
		toAdd.clear();
	}
	
	/**
	 * Esegue il teletrasporto di tutte le entità in coda per il teletrasporto.
	 */
	private void manageTeleports()
	{
		for(Teleportable e : toTeleport)
			e.applyTransforms();
		toTeleport.clear();
	}
	
	/**
	 * Esegue l'update di tutte le entità presenti nel World.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void updateState(float deltaTime)
	{
		for(GraphicEntity e : enviroment)
			e.update(deltaTime);
		for(Entity e : entities)
			e.update(deltaTime);
	}
	
	/**
	 * Rimuove il body e la fixture di una entità data in input, eseguendo
	 * controlli sulla possibilità di rimozione, attraverso la flag dello userData. 
	 * @param e Entità da rimuovere.
	 */
	private void cleanSweep(Entity e)
	{
		JavaBomberUserData bodyData = (JavaBomberUserData) e.getBody().getUserData();
		if(bodyData != null)
			if(bodyData.isFlaggedForRemove() && !world.isLocked()) 
			{
				for(Fixture fix : e.getBody().getFixtureList())
				{
					JavaBomberUserData fixData = (JavaBomberUserData) fix.getUserData();
					if(fixData.isFlaggedForRemove() && !world.isLocked())	e.getBody().destroyFixture(fix);
				}
				world.destroyBody(e.getBody());
			}
	}
	@Override
	public void dispose() {world.dispose();}

	@Override
	public synchronized void addEntity(Entity e) {entities.add(e);}

	@Override
	public synchronized void removeEntity(Entity e) {entities.remove(e);}

	@Override
	public synchronized List<Entity> getEntities() {return entities;}

	@Override
	public synchronized List<Entity> getToRemove()	{return toRemove;}
	
	@Override
	public synchronized List<Entity> getToAdd() {return toAdd;}
	
	@Override
	public synchronized List<Teleportable> getToTeleport() {return toTeleport;}
	
	@Override
	public synchronized World getWorld() {return world;}

	@Override
	public BasicHUD getHUD() {return basicHud;}

	@Override
	public void setHUD(BasicHUD basicHud)	{this.basicHud = basicHud;}
	
	@Override
	public List<Bomb> getPresentBombs()	{return presentBombs;}
	
	public List<Bomb> getToRemoveBombs() {return toRemoveBombs;}
	@Override
	public List<Block> getMapRender()	{return enviroment;}
	
	
	
	
	//METODI INUTILIZZATI
	@Override
	public Level getLevel() {return null;}

	@Override
	public boolean keyDown(int keycode) {return false;}

	@Override
	public boolean keyUp(int keycode) {return false;}

	@Override
	public boolean keyTyped(char character) {return false;}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}

	@Override
	public boolean scrolled(int amount) {return false;}

	
}
