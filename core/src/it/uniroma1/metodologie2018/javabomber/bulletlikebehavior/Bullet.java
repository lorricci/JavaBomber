package it.uniroma1.metodologie2018.javabomber.bulletlikebehavior;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.BodyDispenser;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;

/**
 * Definisce la costruzione base di un qualsiasi "proiettile" utilizzato all'interno del gioco.
 * @author Lorenzo Ricci
 *
 */
abstract public class Bullet implements GraphicEntity 
{
	
////////////////////////////CAMPI DELLA CLASSE//////////////////////////////////
	
	/**
	 * Distanza massima raggiunta dal proiettile.
	 */
	protected float maxTravelDistance;

	/**
	 * Direzione in cui è diretto il proiettile.
	 */
	protected Direction facing;

	/**
	 * Posizione di partenza del proiettile.
	 */
	protected Vector2 startingPos;
	
	/**
	 * Posizione attuale del proiettile.
	 */
	protected Vector2 position;
	
	/**
	 * Sprite del proiettile.
	 */
	protected Sprite sprite;
	
	/**
	 * Body del proiettile.
	 */
	protected Body body;
	
	/**
	 * Grandezza del body del proiettile.
	 */
	protected Vector2 size = new Vector2(16f, 16f);
	
	/**
	 * Dispenser di body per tutte le sottoclassi di Bullet.
	 */
	protected BodyDispenser dispenser;

	/**
	 * WorldController usato dal gioco.
	 */
	protected WorldController controller;
	
	/**
	 * TextureAtlas utilizzato da tutte le sottoclassi di Bullet.
	 */
	protected TextureAtlas textureAtlas;
	
	/**
	 * Indica se il proiettile ha colpito qualcosa.
	 * True se ha colpito un bersaglio, false altrimenti.
	 */
	protected boolean hasHit;
	
	
//////////////////////////COSTRUTTORE//////////////////////////////////////
	
	/**
	 * Supercostruttore di tutte le sottoclassi di Bullet.
	 * @param position Posizione del proiettile.
	 * @param controller Worldcontroller usato dal gioco.
	 * @param facing Direzione del proiettile.
	 */
	public Bullet(Vector2 position, WorldController controller, Direction facing)
	{
		this.position = position;
		startingPos = new Vector2(position);
		this.controller = controller;
		this.facing = facing;
		dispenser = new BodyDispenser(controller);
		body = dispenser.dispenseBulletBody(position, size, this);
	}
	
	
//////////////////////////METODI GETTER E SETTER//////////////////////////
	
	/**
	 * Segnala al proiettile che ha colpito qualcosa.
	 */
	public void hitTarget()	{hasHit = true;}
	
	/**
	 * Restituisce se il proiettile ha colpito qualcosa.
	 * @return True se ha colpito qualcosa, false altrimenti.
	 */
	public boolean hasHit()	{return hasHit;}
	
	@Override
	public Vector2 getSize() {return size;}
	
	@Override
	public Vector2 getPosition() {return new Vector2(position);}
	
	@Override
	public void setPosition(Vector2 position) {this.position = new Vector2(position);}
	
	@Override
	public Body getBody() {return body;}
	

///////////////////////////METODI DI DEFAULT PER LA GESTIONE DEL BODY//////////////////////
	
	/**
	 * Controlla se il proiettile ha raggiunto la distanza massima.
	 * @return True se il proiettile ha viaggiato per la sua distanza massima, false altrimenti.
	 */
	protected boolean finishedTravelling()
	{
		boolean endTravel = false;
		switch(facing)
		{
		case UP		:	if(position.y >= startingPos.y + maxTravelDistance)	endTravel = true;	break;
		case DOWN	:	if(position.y <= startingPos.y - maxTravelDistance) endTravel = true;	break;
		case LEFT	:	if(position.x <= startingPos.x - maxTravelDistance)	endTravel = true;	break;
		case RIGHT	:	if(position.x >= startingPos.x + maxTravelDistance) endTravel = true;	break;
		default		:	if(position.y <= startingPos.y - maxTravelDistance) endTravel = true;	break;
		}
		return endTravel;
	}
	
	@Override
	public void killEntity() 
	{
		setDataReady();
		controller.getToRemove().add(this);
	}
	
	@Override
	public void disposeTexture()	{textureAtlas.dispose();}
	
	/**
	 * Imposta la flag di rimozione dello userData del body e della fixture.
	 */
	private void setDataReady()
	{
		JavaBomberUserData bodyData = (JavaBomberUserData) body.getUserData();
		JavaBomberUserData fixtureData = (JavaBomberUserData) body.getFixtureList().get(0).getUserData();
		bodyData.setAsRemovable();
		fixtureData.setAsRemovable();
	}
}
