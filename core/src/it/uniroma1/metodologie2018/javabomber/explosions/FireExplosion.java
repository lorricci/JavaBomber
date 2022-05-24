package it.uniroma1.metodologie2018.javabomber.explosions;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.CENTER00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.CENTER01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.CENTER02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.CENTER03;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_DOWN00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_DOWN01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_DOWN02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_DOWN03;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_LEFT00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_LEFT01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_LEFT02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_LEFT03;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_RIGHT00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_RIGHT01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_RIGHT02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_RIGHT03;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_UP00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_UP01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_UP02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.END_UP03;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.LATERAL00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.LATERAL01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.LATERAL02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.LATERAL03;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.VERTICAL00;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.VERTICAL01;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.VERTICAL02;
import static it.uniroma1.metodologie2018.javabomber.utils.FireExplosionFrames.VERTICAL03;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Characters;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.map.Wall;
/**
 * Costruisce l'esplosione standard di fiamme, generata di solito
 * dalla bomba piazzata dal giocatore.
 * @author Lorenzo Ricci
 *
 */
public class FireExplosion extends Explosion 
{
	
	
////////////////////////CAMPI DELLA CLASSE///////////////////////////////////
	
	/**
	 * Tipo di esplosione, definisce lo sprite utilizzato nell'animazione.
	 */
	private ExplosionTypes spriteType;
	
	/**
	 * Spazio di tempo in cui la fiammata deve rimanere a schermo.
	 */
	private float timeOnScreen = 0.50f;
	
	
	/**
	 * Frame attuale di animazione dell'esplosione.
	 */
	private int currentFrame = 0;
	
	/**
	 * Accumulatore del delay dell'animazione per gestire il passaggio da un frame all'altro.
	 */
	private float animeFrameDelay = 0.0f;
	
	/**
	 * Tempo fisso di delay tra un frame e l'altro.
	 */
	private float timeFrames = 0.10f;
	
	
/////////////////////////////////COSTRUTTORE///////////////////////////////
	
	/**
	 * Costruisce l'esplosione assegnandole una posizione ed uno sprite, in base alla sua posizione
	 * nella "croce" di esplosione.
	 * @param position	Posizione della fiammata nella mappa di gioco.
	 * @param controller	World legato al mondo di gioco.
	 * @param spriteType	Tipo di sprite da prendere nello spritesheet.
	 */
	public FireExplosion(Vector2 position, WorldController controller, ExplosionTypes spriteType) 
	{
		//Setup delle informazioni.
		super(position, controller);
		this.spriteType = spriteType;
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/Explosion/sprites.txt"));
		size = new Vector2(28f, 28f);
		body = dispenser.dispenseExplosionBody(position, size, this);
		//Setup dello sprite.
		setupSprite();
		if(spriteType.equals(ExplosionTypes.CENTER)) sprite.setSize(sprite.getWidth() / 1.5f / PPM, sprite.getHeight() / 1.5f / PPM);
		else sprite.setSize(sprite.getWidth() / PPM, sprite.getHeight() / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2);
	}


///////////////////////////METODI PER IL DISEGNO DELLO SPRITE E ANIMAZIONI/////////////////////////////
	
	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);}

	/**
	 * Inizializza lo sprite in base al tipo di fiammata.
	 */
	private void setupSprite()
	{
		switch(spriteType)
		{
		case CENTER		:	sprite = new Sprite(textureAtlas.findRegion(CENTER00));		break;
		case END_DOWN	:	sprite = new Sprite(textureAtlas.findRegion(END_DOWN00));	break;
		case END_LEFT	:	sprite = new Sprite(textureAtlas.findRegion(END_LEFT00));	break;
		case END_RIGHT	:	sprite = new Sprite(textureAtlas.findRegion(END_RIGHT00));	break;
		case END_UP		:	sprite = new Sprite(textureAtlas.findRegion(END_UP00));		break;
		case LATERAL	:	sprite = new Sprite(textureAtlas.findRegion(LATERAL00));	break;
		case VERTICAL	:	sprite = new Sprite(textureAtlas.findRegion(VERTICAL00));	
		}
	}
	
	/**
	 * Gestisce l'animazione da utilizzare in base al tipo di fiammata.
	 */
	private void handleAnimation()
	{
		switch(spriteType)
		{
		case CENTER		:	centralFlame();		break;
		case END_DOWN	:	lowerEndFlame();	break;
		case END_LEFT	:	leftEndFlame();		break;
		case END_RIGHT	:	rightEndFlame();	break;
		case END_UP		:	upperEndFlame();	break;
		case LATERAL	:	lateralFlame();		break;
		case VERTICAL	:	verticalFlame();	
		}
	}
	
	/**
	 * Gestisce l'animazione della fiammata centrale.
	 */
	private void centralFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(CENTER00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(CENTER01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(CENTER02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(CENTER03)); 
			}
			currentFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della fiammata laterale.
	 */
	private void lateralFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(LATERAL00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(LATERAL01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(LATERAL02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(LATERAL03)); 
			}
			currentFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della fiammata verticale.
	 */
	private void verticalFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(VERTICAL00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(VERTICAL01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(VERTICAL02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(VERTICAL03)); 
			}
			currentFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'ultima fiammata di sinistra.
	 */
	private void leftEndFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(END_LEFT00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(END_LEFT01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(END_LEFT02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(END_LEFT03)); 
			}
			currentFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'ultima fiammata di destra.
	 */
	private void rightEndFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(END_RIGHT00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(END_RIGHT01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(END_RIGHT02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(END_RIGHT03)); 
			}
			currentFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'ultima fiammata in alto.
	 */
	private void upperEndFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(END_UP00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(END_UP01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(END_UP02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(END_UP03)); 
			}
			currentFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'ultima fiammata in basso.
	 */
	private void lowerEndFlame()
	{
		while(animeFrameDelay >= timeFrames)
		{
			animeFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(END_DOWN00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(END_DOWN01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(END_DOWN02)); break;
			default : sprite.setRegion(textureAtlas.findRegion(END_DOWN03)); 
			}
			currentFrame++;
		}
	}
	
	@Override
	public void update(float deltaTime) 
	{
		animeFrameDelay += deltaTime;
		timeOnScreen -= deltaTime;
		handleSprite(body, sprite);
		handleAnimation();
		if(timeOnScreen <= 0)	killEntity();
	}
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}
	
	
/////////////////////METODI PER LA GESTIONE DELLE COLLISIONI////////////////////////
	
	@Override
	public void collide(GraphicEntity actor2)	
	{
		if(actor2 instanceof Characters)	//Collisione con un giocatore.	
		{
			Characters pg = (Characters) actor2;
			pg.getHit();
		}
		else if(actor2 instanceof Wall)		//Collisione con un muro.
		{
			Wall wall = (Wall) actor2;
			wall.getHit();
		}
	}
}
