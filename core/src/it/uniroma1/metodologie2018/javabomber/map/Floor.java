package it.uniroma1.metodologie2018.javabomber.map;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.FLOOR;
/**
 * Rappresenta la semplice griglia camminabile della mappa, ovvero le celle su cui
 * è consentito il passaggio dei diversi personaggi. 
 * @author Lorenzo Ricci
 *
 */
public class Floor extends Tile
{
	/**
	 * Indica se nella posizione del pavimento è presente un muro.
	 */
	private boolean bWall;
	
//////////////////////////COSTRUTTORE/////////////////////////////
	
	/**
	 * Costruisce un tile di pavimentazione.
	 * @param position Posizione del pavimento.
	 * @param controller World associato al pavimento.
	 */
	public Floor(Vector2 position, WorldController controller) 
	{
		//Setup dei dati.
		super(position, controller);
		//Setup del body.
		body = dispenser.dispenseFloorBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(FLOOR));
		sprite.setSize((sprite.getWidth() / 2) / PPM, (sprite.getHeight() / 2) / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
	}

	
/////////////////////////////METODI GETTER E SETTER////////////////////////

	/**
	 * Restituisce True se nel posto del pavimento è presente un muro, false altrimenti.
	 * @return True se c'è un muro, false altrimenti.
	 */
	public boolean hasWall()	{return bWall;}
	
	/**
	 * Setta a true il booleano indicante la presenza di un muro.
	 */
	public void spawnWall()		{bWall = true;}
	
	/**
	 * Resetta a false il booleano indicante la presenza di un muro.
	 */
	public void resetWall()		{bWall = false;}
	
///////////////////////////METODI PER IL DISEGNO DELLO SPRITE///////////////

	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (sprite.getHeight() / 2f) );}

	@Override
	public void update(float deltaTime) {handleSprite(body, sprite);}


/////////////////////METODI PER LA GESTIONE DELLE COLLISIONI////////////////

	@Override
	public void collide(GraphicEntity actor2) {}

	@Override
	public void killEntity() {}
}
