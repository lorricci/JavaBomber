package it.uniroma1.metodologie2018.javabomber.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Characters;
import it.uniroma1.metodologie2018.javabomber.entities.Enemy;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;

/**
 * Struttura le informazioni di base di qualsiasi PowerUp presente
 * all'interno del gioco.
 * @author Lorenzo Ricci
 *
 */
public abstract class PowerUp implements GraphicEntity 
{

///////////////////CAMPI DELLA CLASSE////////////////////
	
	/**
	 * Posizione del PowerUp.
	 */
	protected Vector2 position;
	
	/**
	 * Grandezza del PowerUp.
	 * Mantenuta costante per tutti i PowerUp.
	 */
	protected Vector2 size = new Vector2(26.0f, 26.0f);
	
	/**
	 * Sprite del PowerUp.
	 */
	protected Sprite sprite;
	
	/**
	 * Body del PowerUp.
	 */
	protected Body body;
	
	/**
	 * Controller legato ad ogni powerup.
	 */
	protected WorldController controller;
	
	/**
	 * Texture usata dal powerUp. (Non è necessario un textureAtlas dato che i powerUps non vengono animati).
	 */
	protected Texture texture;
	
	
/////////////////////////////COSTRUTTORE////////////////////////////
	
	/**
	 * Costruisce il PowerUp assegnandogli un body, in base alla posizione indicata in input.
	 * @param controller Controller legato ad ogni PowerUp.
	 * @param position Posizione del powerUp.
	 */
	public PowerUp(WorldController controller, Vector2 position)
	{
		this.controller = controller;
		this.position = position;
	}
	
	
//////////////////////METODI ASTRATTI DELLA CLASSE//////////////////////
	
	/**
	 * Applica il potenziamento/depotenziamento al personaggio che raccoglie il PowerUp.
	 * @param subject Personaggio che ha raccolto il PowerUp.
	 */
	public abstract void powerStats(Characters subject);
	
	
/////////////////////METODI GETTER E SETTER/////////////////////////////7
	
	/**
	 * Imposta il body del PowerUp.
	 * @param body Body da assegnare al PowerUp.
	 */
	public void setBody(Body body)	{this.body = body;}
	
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
	
	@Override
	public void setPosition(Vector2 position)	{this.position = position;}
	
	@Override
	public Vector2 getSize() {return size;}
	
	@Override
	public Vector2 getPosition() {return new Vector2(position);}
	
	@Override
	public Body getBody() {return body;}
	

///////////////////////////////METODI PER LA COLLISIONE///////////////////////////
	
	@Override
	public void collide(GraphicEntity actor2)
	{
		if(actor2 instanceof Characters && !(actor2 instanceof Enemy))
		{
			Characters pg = (Characters) actor2;
			powerStats(pg);
			killEntity();
		}
	}
	
	@Override
	public void killEntity()
	{
		setDataReady();
		controller.getToRemove().add(this);
	}
	
	@Override
	public void disposeTexture()	{texture.dispose();}
}
