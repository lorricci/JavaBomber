package it.uniroma1.metodologie2018.javabomber.entities;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.BombAnimationFrames.BOMB00;
import static it.uniroma1.metodologie2018.javabomber.utils.BombAnimationFrames.BOMB01;
import static it.uniroma1.metodologie2018.javabomber.utils.BombAnimationFrames.BOMB02;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import it.uniroma1.metodologie2018.javabomber.explosions.Explosion;
import it.uniroma1.metodologie2018.javabomber.explosions.ExplosionTypes;
import it.uniroma1.metodologie2018.javabomber.explosions.FireExplosion;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.map.BorderWall;
import it.uniroma1.metodologie2018.javabomber.map.BurningTile;
import it.uniroma1.metodologie2018.javabomber.map.FrozenTile;
import it.uniroma1.metodologie2018.javabomber.map.Wall;
import it.uniroma1.metodologie2018.javabomber.utils.BodyDispenser;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;
/**
 * Rappresenta la bomba utilizzata da Bomberman. Fornisce i metodi fondamentali
 * per manipolare ogni aspetto della bomba, compreso il tipo di esplosione.
 * @author Lorenzo Ricci
 *
 */
public class Bomb implements GraphicEntity		
{

	
/////////////////////////////CAMPI DELLA CLASSE////////////////////////////////////
	
	/**
	 * Personaggio che ha piazzato la bomba.
	 */
	private Characters placer;
	
	/**
	 * Indica se la bomba è appena stata piazzata.
	 */
	private boolean justPlaced = true;
	
	/**
	 * Danni inferti dalla bomba.
	 * Inizializzati a valore 1.
	 */
	private int damage;
	
	/**
	 * Raggio dell'esplosione considerando come centro la posizione della bomba.
	 * Inizializzato al valore 32f (ovvero la larghezza di un blocco).
	 */
	private float range;
	
	/**
	 * Timer di esplosione della bomba.
	 */
	private float timer = 1.5f;
	
	/**
	 * Posizione della bomba.
	 */
	private Vector2 position;
	
	/**
	 * Sprite della bomba.
	 */
	private Sprite sprite;
	
	/**
	 * Dimensione della bomba.
	 */
	private Vector2 size = new Vector2(20f, 20f);
	
	/**
	 * Corpo fisico della bomba.
	 */
	private Body body;
	
	/**
	 * Dispenser per la creazione del corpo.
	 */
	private BodyDispenser dispenser;
	
	/**
	 * Gestore dell'animazione della bomba.
	 */
	private TextureAtlas textureAtlas;
	
	/**
	 * WorldController usato dal gioco.
	 */
	private WorldController controller;
	
	/**
	 * Frame corrente della bomba.
	 */
	private int currentFrame = 2;
	
////////////////////////////////////COSTRUTTORE//////////////////////////////////////
	
	/**
	 * Costruisce la bomba nella posizione data.
	 * @param position  Posizione in cui viene piazzata la bomba.
	 * @param controller Controller legato al mondo di gioco in cui viene piazzata la bomba.
	 * @param placer Personaggio che ha piazzato la bomba.
	 */
	public Bomb(Vector2 position, WorldController controller, Characters placer)	
	{
		//Setup dei dati.
		this.position = position;
		this.controller = controller;
		this.placer = placer;
		//Setup del body.
		dispenser = new BodyDispenser(controller);
		body = dispenser.dispenseBombBody(position, size, this);
		//Setup dello sprite.
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/Bomb/sprites.txt"));
		sprite = new Sprite(textureAtlas.findRegion(BOMB00));
		sprite.setSize((sprite.getWidth() / 2.5f) / PPM , (sprite.getHeight() / 2.5f) / PPM);
		sprite.setOrigin(sprite.getWidth() / 2.f, sprite.getHeight() / 2.f);
		handleSprite(body, sprite);
	}
	
	
////////////////////////////METODI SETTER E GETTER///////////////////////////////////////////////

	/**
	 * Modifica i danni inferti dalla bomba.
	 * @param damage Nuova quantità di danni.
	 */
	public void setDamage(int damage)	{this.damage = damage;}
	
	/**
	 * Restituisce la quantità di danno fatto dalla bomba.
	 * @return	Quantità di danni attuali.
	 */
	public int getDamage()	{return damage;}
	
	/**
	 * Modifica il range dell'esplosione della bomba.
	 * @param range Nuovo raggio di esplosione.
	 */
	public void setRange(float range)	{this.range = range;}
	
	/**
	 * Modifica il timer della bomba. 
	 * @param timer Timer da assegnare alla bomba.
	 */
	public void setTimer(float timer)	{this.timer = timer;}
	
	/**
	 * Restituisce il timer attuale della bomba.
	 * @return Timer attuale della bomba.
	 */
	public float getTimer()	{return timer;}
	
	/**
	 * Restituisce l'attuale range della bomba.
	 * @return Raggio attuale dell'esplosione.
	 */
	public float getRange()	{return range;}
	
	@Override
	public void setPosition(Vector2 position)	{this.position = new Vector2(position);}
	
	@Override
	public Vector2 getPosition()	{return new Vector2(position);}

	@Override
	public Body getBody() 	{return body;}

	@Override
	public Vector2 getSize() {return size;}

	/**
	 * Imposta la bomba come già piazzata.
	 */
	public void setAsPlacedYet()	{justPlaced = false;}
	
	/**
	 * Restituisce lo stato di piazzamento della bomba.
	 * @return boolean True se è già stata piazzata, false altrimenti.
	 */
	public boolean isJustPlaced()	{return justPlaced;}
	
	/**
	 * Imposta la flag di rimozione dello userData del body e della fixture come true.
	 */
	private void setDataReady()
	{
		JavaBomberUserData bodyData = (JavaBomberUserData) body.getUserData();
		JavaBomberUserData fixtureData = (JavaBomberUserData) body.getFixtureList().get(0).getUserData();
		bodyData.setAsRemovable();
		fixtureData.setAsRemovable();
	}
	
	
///////////////////////METODI PER L'ESPLOSIONE DELLA BOMBA/////////////////////////////
	
	/**
	 * Fa detonare la bomba, poi si occupa del dispose del corpo e della texture.
	 */
	private void detonate()
	{
		handleExplosion();
		placer.getBombStats().addPlacing();
		controller.getToRemoveBombs().add(this);
		killEntity();
	}
	
	/**
	 * Gestisce la forma e l'esplosione della bomba. 
	 * (Usa una forma standard a croce, classica di BomberMan.)
	 */
	private void handleExplosion()
	{
		ArrayList<Explosion> toAdd = new ArrayList<Explosion>();
		FireExplosion center = new FireExplosion(new Vector2(getPosition()), controller, ExplosionTypes.CENTER);
		toAdd.add(center);
		explodeVertical(toAdd);
		explodeLateral(toAdd);
		controller.getToAdd().addAll(toAdd); // Aggiunta delle esplosioni alla lista aggiunte del controller.
	}
	
	/**
	 * Fa esplodere la bomba verticalmente, sfruttando i singoli metodi privati 
	 * explodeUp ed explodeDown.
	 * @param toAdd Lista a cui aggiungere le esplosioni.
	 */
	private void explodeVertical(ArrayList<Explosion> toAdd)
	{
		explodeUp(toAdd);
		explodeDown(toAdd);
	}
	
	/**
	 * Fa esplodere lateralmente la bomba, sfruttando i singoli metodi privati
	 * explodeLeft ed ExplodeRight.
	 * @param toAdd Lista a cui aggiungere le esplosioni.
	 */
	private void explodeLateral(ArrayList<Explosion> toAdd)
	{
		explodeLeft(toAdd);
		explodeRight(toAdd);
	}
	
	/**
	 * Gestisce l'espansione verso l'alto dell'esplosione.
	 * @param toAdd Lista a cui aggiungere le esplosioni.
	 */
	private void explodeUp(ArrayList<Explosion> toAdd)
	{
		Vector2 bombCoor = getPosition();
		for(float posY = bombCoor.y + 32; posY <= bombCoor.y + range; posY += 32)
		{
			if(checkZone(bombCoor.x, posY))	
			{
				ExplosionTypes spriteType = posY == bombCoor.y + range ? ExplosionTypes.END_UP : ExplosionTypes.VERTICAL;
				FireExplosion expl = new FireExplosion(new Vector2(bombCoor.x, posY), controller, spriteType);
				toAdd.add(expl);
			}
			else break;
		}	
	}
	
	/**
	 * Gestice l'espansione verso il basso dell'esplosione.
	 * @param toAdd Lista a cui aggiungere le esplosioni.
	 */
	private void explodeDown(ArrayList<Explosion> toAdd)
	{
		Vector2 bombCoor = getPosition();
		for(float posY = bombCoor.y - 32; posY >= bombCoor.y - range; posY -= 32)
		{
			if(checkZone(bombCoor.x, posY))	
			{
				ExplosionTypes spriteType = posY == bombCoor.y - range ? ExplosionTypes.END_DOWN : ExplosionTypes.VERTICAL;
				FireExplosion expl = new FireExplosion(new Vector2(bombCoor.x, posY), controller, spriteType);
				toAdd.add(expl);
			}
			else break;
		}	
	}
	
	/**
	 * Gestice l'espansione verso il lato sinistro dell'esplosione.
	 * @param toAdd Lista a cui aggiungere le esplosioni.
	 */
	private void explodeLeft(ArrayList<Explosion> toAdd)
	{
		Vector2 bombCoor = getPosition();
		for(float posX = bombCoor.x - 32; posX >= bombCoor.x - range; posX -= 32)
		{
			if(checkZone(posX, bombCoor.y))
			{
				ExplosionTypes spriteType = posX == bombCoor.x - range ? ExplosionTypes.END_LEFT : ExplosionTypes.LATERAL;
				FireExplosion expl = new FireExplosion(new Vector2(posX, bombCoor.y), controller, spriteType);
				toAdd.add(expl);
			}
			else break;
		}
	}
	
	/**
	 * Gestice l'espansione verso il lato destro dell'esplosione.
	 * @param toAdd Lista a cui aggiungere le esplosioni.
	 */
	private void explodeRight(ArrayList<Explosion> toAdd)
	{
		Vector2 bombCoor = getPosition();
		for(float posX = bombCoor.x + 32; posX <= bombCoor.x + range; posX += 32)
		{
			if(checkZone(posX, bombCoor.y))
			{
				ExplosionTypes spriteType = posX == bombCoor.x + range ? ExplosionTypes.END_RIGHT : ExplosionTypes.LATERAL;
				FireExplosion expl = new FireExplosion(new Vector2(posX, bombCoor.y), controller, spriteType);
				toAdd.add(expl);
			}
			else break;
		}
	}
	
	/**
	 * Controlla se nella zona indicata dalle coordinate è possibile creare un'esplosione.
	 * @param posX	Posizione x dell'ipotetica esplosione.
	 * @param posY	Posizione y dell'ipotetica esplosione.
	 * @return	True : possibile piazzamento | False : non si può piazzare l'esplosione
	 * 
	 */
	private boolean checkZone(float posX, float posY)
	{
		for(GraphicEntity ent : controller.getMapRender())
		{
			if((posX >= ent.getPosition().x - 16 && posX <= ent.getPosition().x + 16) && 
					   (posY >= ent.getPosition().y - 16 && posY <= ent.getPosition().y + 16))	
				if(ent instanceof Wall)
				{
					Wall wall = (Wall) ent;
					wall.getHit();
					return false;
				}
				else if(ent instanceof BorderWall) return false;
		}
		return true;
	}
	
	
///////////////METODI PER LA GESTIONE DELLO SPRITE//////////////////////////
	
	@Override
	public void update(float deltaTime) 
	{
		timer -= deltaTime;
		if(timer <= 0) detonate(); //Controlla lo stato del timer e dell'esplosione, se ha raggiunto lo 0, la fa detonare.	
		else 
		{
			setPosition(new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM));
			handleSprite(body, sprite);
		}
	}
	
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}	
	
	@Override
	public void handleSprite(Body body, final Sprite sprite)
	{
		sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (sprite.getHeight() / 2f) );
		Timer.schedule(new Task() {
			@Override
			public void run() 
			{
				currentFrame--;
				//Resetta al primo frame per evitare che venga riutilizzato
				//lo sprite_frame 2 con la miccia spenta.
				switch(currentFrame)
				{
				case 0	:	sprite.setRegion(textureAtlas.findRegion(BOMB00)); break;
				case 1	:	sprite.setRegion(textureAtlas.findRegion(BOMB01)); break;
				case 2	:	sprite.setRegion(textureAtlas.findRegion(BOMB02)); break;
				default :
				{
					currentFrame = 1;
					sprite.setRegion(textureAtlas.findRegion(BOMB01));
				}
				break;
				}
			}
		}, Gdx.graphics.getDeltaTime());
	}

/////////////////////////////////METODI PER LA GESTIONE DEL BODY//////////////////////////////
	
	@Override
	public void collide(GraphicEntity actor2) 
	{
		if(actor2 instanceof FrozenTile) body.setLinearVelocity(0,0);
		else if(actor2 instanceof BurningTile) setTimer(0.0f);
	}

	@Override
	public void killEntity() 
	{
		setDataReady();
		controller.getToRemove().add(this);
	}
	
	@Override
	public void disposeTexture()	{textureAtlas.dispose();}
}
