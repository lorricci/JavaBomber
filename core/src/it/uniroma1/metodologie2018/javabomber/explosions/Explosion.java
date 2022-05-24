package it.uniroma1.metodologie2018.javabomber.explosions;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.BodyDispenser;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;

/**
 * Descrive i parametri di rappresentazione di ogni esplosione del gioco. 
 * Ogni sottoclasse descriverà un singolo tipo di esplosione.
 * @author Lorenzo Ricci
 *
 */
public abstract class Explosion implements GraphicEntity
{
	
////////////////////////////CAMPI DELLA CLASSE//////////////////////////////
	
	/**
	 * Body dell'esplosione.
	 */
	protected Body body;
	
	/**
	 * Sprite dell'esplosione.
	 */
	protected Sprite sprite;
	
	/**
	 * Grandezza dell'esplosione.
	 */
	protected Vector2 size;
	
	/**
	 * Posizione dell'esplosione nella mappa.
	 */
	protected Vector2 position;
	
	/**
	 * Dispenser dei corpi delle esplosioni.
	 */
	protected BodyDispenser dispenser;
	
	/**
	 * World legato ad ogni esplosione.
	 */
	protected WorldController controller;
	
	/**
	 * TextureAtlas usato nell'animazione dello sprite delle esplosioni.
	 */
	protected TextureAtlas textureAtlas;
	

//////////////////////////////////COSTRUTTORE///////////////////////////////
	
	/**
	 * Costruttore fondamentale di ogni esplosione.
	 * @param position Posizione dell'esplosione.
	 * @param controller World legato all'esplosione.
	 */
	public Explosion(Vector2 position, WorldController controller)
	{
		this.position = position;
		this.controller = controller;
		dispenser = new BodyDispenser(controller);
	}

	
/////////////////////////////////METODI GETTER E SETTER////////////////////////////
	
	@Override
	public Vector2 getSize() {return size;}
	
	@Override
	public Vector2 getPosition() {return new Vector2(position);}

	@Override
	public void setPosition(Vector2 position) {this.position = new Vector2(position);}

	@Override
	public Body getBody() {return body;}
	

/////////////////////METODI DI DEFAULT PER LA GESTIONE DEL BODY/////////////////////
	
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
	
	@Override
	public void killEntity() 
	{
		setDataReady();
		controller.getToRemove().add(this); 
	}

	@Override
	public void disposeTexture()	{textureAtlas.dispose();}
}
