package it.uniroma1.metodologie2018.javabomber.map;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Bullet;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.BORDERWALL;
/**
 * Rappresenta il muro indistruttibile, che può essere utilizzato
 * anche come "cornice" dell'arena.
 * @author Lorenzo Ricci
 *
 */
public class BorderWall extends Tile
{
	
///////////////////////////////COSTRUTTORE//////////////////////////////////
	
	/**
	 * Costruisce un muro indistruttibile.
	 * @param position Posizione del muro.
	 * @param controller World associato al muro.
	 */
	public BorderWall(Vector2 position, WorldController controller) 
	{
		//Setup dei dati.
		super(position, controller);
		//Setup del body.
		body = dispenser.dispenseWallBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(BORDERWALL));
		sprite.setSize((sprite.getWidth() / 2) / PPM, (sprite.getHeight() / 2) / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}

	
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
	public void collide(GraphicEntity actor2)
	{
		if(actor2 instanceof Bullet) 
		{
			Bullet bullet = (Bullet) actor2;
			bullet.hitTarget();
		}
	}

	@Override
	public void killEntity() {}
}
