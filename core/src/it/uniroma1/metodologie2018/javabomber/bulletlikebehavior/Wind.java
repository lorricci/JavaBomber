package it.uniroma1.metodologie2018.javabomber.bulletlikebehavior;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.WindTextures.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.map.BorderWall;
import it.uniroma1.metodologie2018.javabomber.map.Wall;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;
/**
 * Rappresenta la folata di vento utilizzata dal boss come attacco base. Trapassa tutto ciò
 * che colpisce tranne i muri indistruttibili dell'arena.
 * @author Lorenzo Ricci
 *
 */
public class Wind extends Bullet
{

///////////////////////////////COSTRUTTORE///////////////////////
	
	/**
	 * Costruisce la folata di vento attraverso il supercostruttore.
	 * @param position Posizione della folata.
	 * @param controller Controller utilizzato dal gioco.
	 * @param facing Direzione della folata.
	 */
	public Wind(Vector2 position, WorldController controller, Direction facing) 
	{
		//Setup dei dati.
		super(position, controller, facing);
		maxTravelDistance = 128f;
		//Setup della velocità del body.
		specifyVelocity();
		body.getFixtureList().forEach(fix -> fix.setSensor(true));
		//Setup dello sprite.
		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/Wind/sprites.txt"));
		setSpriteDirection();
		sprite.setSize(sprite.getWidth() / PPM * 1.9f, sprite.getHeight() / PPM * 1.7f);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}

////////////////////////////METODI PER IL DISEGNO DELLO SPRITE/////////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);}

	@Override
	public void update(float deltaTime) 
	{
		if(hasHit) killEntity();
		else if(finishedTravelling()) killEntity();
		else 
		{
			setPosition(new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM));
			handleSprite(body, sprite);
		}
	}

	/**
	 * Assegna la regione corretta dello spritesheet al texture atlas in base alla
	 * direzione della folata.
	 */
	private void setSpriteDirection()
	{
		switch(facing)
		{
		case UP		:	sprite = new Sprite(textureAtlas.findRegion(WINDUP)); 		break;
		case DOWN	:	sprite = new Sprite(textureAtlas.findRegion(WINDDOWN));		break;
		case LEFT	:	sprite = new Sprite(textureAtlas.findRegion(WINDLEFT)); 	break;
		case RIGHT	:	sprite = new Sprite(textureAtlas.findRegion(WINDRIGHT)); 	break;
		default		:	sprite = new Sprite(textureAtlas.findRegion(WINDDOWN));
		}
	}
	
	
////////////////////////METODI PER LA GESTIONE DEL BODY///////////////////
	
	/**
	 * Imprime alla folata di vento una velocità lineare in base alla direzione già assegnata.
	 */
	private void specifyVelocity()
	{
		switch(facing)
		{
		case DOWN	:	body.setLinearVelocity(0, -2.4f); break;
		case LEFT	:	body.setLinearVelocity(-2.4f, 0); break;
		case RIGHT	:	body.setLinearVelocity(2.4f, 0); 	break;
		case UP		:	body.setLinearVelocity(0, 2.4f); 	break;
		case IDLE	:	body.setLinearVelocity(0, -2.4f); break;
		}
	}

	@Override
	public void collide(GraphicEntity actor2) 
	{
		if(actor2 instanceof BorderWall)	hitTarget();
		else if(actor2 instanceof Player)
		{
			Player pg = (Player) actor2;
			pg.getHit();
		}
		else if(actor2 instanceof Wall)
		{
			Wall wall = (Wall) actor2;
			wall.getHit();
		}
	}
}
