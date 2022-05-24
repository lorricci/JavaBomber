package it.uniroma1.metodologie2018.javabomber.bulletlikebehavior;

import static it.uniroma1.metodologie2018.javabomber.utils.ArrowTextures.ARROW_DOWN;
import static it.uniroma1.metodologie2018.javabomber.utils.ArrowTextures.ARROW_LEFT;
import static it.uniroma1.metodologie2018.javabomber.utils.ArrowTextures.ARROW_RIGHT;
import static it.uniroma1.metodologie2018.javabomber.utils.ArrowTextures.ARROW_UP;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

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
 * Rappresenta una classica freccia scoccata da un arco. 
 * @author Lorenzo Ricci
 *
 */
public class Arrow extends Bullet
{
	
//////////////////////////////////////COSTRUTTORE//////////////////////////////////
	
	/**
	 * Costruisce l'istanza di una freccia.
	 * @param position Posizione di partenza della freccia.
	 * @param controller Worldcontroller del gioco.
	 * @param facing Direzione della freccia.
	 */
	public Arrow(Vector2 position, WorldController controller, Direction facing) 
	{
		//Setup delle informazioni.
		super(position, controller, facing);
		maxTravelDistance = 128f;
		//Setup della velocità del body.
		specifyVelocity();
		//Setup dello sprite.
		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/Arrow/sprites.txt"));
		setSpriteDirection();
		sprite.setSize(sprite.getWidth() / PPM, sprite.getHeight() / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}

////////////////////////////////METODI PER IL DISEGNO DELLO SPRITE//////////////////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);}


	@Override
	public void update(float deltaTime) 
	{
		//Controlla se ha colpito un bersaglio.
		if(hasHit) killEntity();
		//Controlla se ha raggiunto la distanza massima.
		else if(finishedTravelling()) killEntity();
		else 
		{
			setPosition(new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM));
			handleSprite(body, sprite);
		}
	}

	/**
	 * Assegna la regione corretta dello spritesheet al texture atlas in base alla
	 * direzione della freccia.
	 */
	private void setSpriteDirection()
	{
		switch(facing)
		{
		case UP		:	sprite = new Sprite(textureAtlas.findRegion(ARROW_UP));		break;
		case DOWN	:	sprite = new Sprite(textureAtlas.findRegion(ARROW_DOWN));	break;
		case LEFT	:	sprite = new Sprite(textureAtlas.findRegion(ARROW_LEFT));	break;
		case RIGHT	:	sprite = new Sprite(textureAtlas.findRegion(ARROW_RIGHT));	break;
		default		:	sprite = new Sprite(textureAtlas.findRegion(ARROW_DOWN));	break;
		}
	}
	
	
////////////////////////////////METODI PER LA GESTIONE DEL BODY//////////////////////////
	
	/**
	 * Imprime alla freccia una velocità lineare in base alla direzione già assegnata.
	 */
	private void specifyVelocity()
	{
		switch(facing)
		{
		case DOWN	:	body.setLinearVelocity(0, -2.0f); break;
		case LEFT	:	body.setLinearVelocity(-2.0f, 0); break;
		case RIGHT	:	body.setLinearVelocity(2.0f, 0); 	break;
		case UP		:	body.setLinearVelocity(0, 2.0f); 	break;
		default		:	body.setLinearVelocity(0, 2.0f); break;
		}
	}
	
	@Override
	public void collide(GraphicEntity actor2) 
	{
		if(actor2 instanceof Player)
		{
			hitTarget();
			Player pg = (Player) actor2;
			pg.getHit();
		}
		if(actor2 instanceof Wall)
		{
			hitTarget();
			Wall wall = (Wall) actor2;
			wall.getHit();
		}
		if(actor2 instanceof BorderWall)	hitTarget();
	}
}
