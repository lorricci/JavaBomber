package it.uniroma1.metodologie2018.javabomber.explosions;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.ThunderAnimationFrames.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;

/**
 * Incantesimo del tuono utilizzato dal mago. 
 * Rappresenta le stesse caratteristiche dell'esplosione di fuoco, seppur
 * abbia una maggiore durata a schermo.
 * @author Lorenzo Ricci
 *
 */
public class Thunder extends Explosion
{
/////////////////////////CAMPI DELLA CLASSE//////////////////////////////
	
	/**
	 * Durata dell'esplosione a schermo.
	 */
	private float timeOnScreen = 1.05f;
	
	/**
	 * Frame di animazione del tuono.
	 */
	private int currentFrame;
	
	/**
	 * Accumulatore di delay tra i diversi frame dell'animazione del tuono.
	 */
	private float animFrameDelay;
	
	/**
	 * Secondi di delay tra i diversi frame dell'animazione.
	 */
	private float timeFrames = 0.15f;
	
	
////////////////////////COSTRUTTORE///////////////////////////////
	
	/**
	 * Costruisce il tuono attraverso il supercostruttore dell'esplosione.
	 * @param position Posizione dell'esplosione.
	 * @param controller World del gioco.
	 */
	public Thunder(Vector2 position, WorldController controller) 
	{
		//Setup delle infos.
		super(position, controller);
		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/Thunder/sprites.txt"));
		size = new Vector2(15f, 15f);
		body = dispenser.dispenseExplosionBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(THUNDER00));
		sprite.setSize(sprite.getWidth() / PPM * 2.6f, sprite.getHeight() / PPM * 1.6f);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
	}
	
	
/////////////////////////METODI PER IL DISEGNO DELLO SPRITE/////////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 8.5f);}

	@Override
	public void update(float deltaTime) 
	{
		animFrameDelay += deltaTime;
		timeOnScreen -= deltaTime;
		if(timeOnScreen <= 0.0f) killEntity();
		else
		{
			handleSprite(body, sprite);
			handleAnimation();
		}
	}

	/**
	 * Gestisce l'animazione del tuono ed il passaggio tra i vari frame.
	 */
	private void handleAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			if(currentFrame > 6) killEntity();
			else
			{
				switch(currentFrame)
				{
				case 0	:	sprite.setRegion(textureAtlas.findRegion(THUNDER00));	break;
				case 1	:	sprite.setRegion(textureAtlas.findRegion(THUNDER01));	break;
				case 2	:	sprite.setRegion(textureAtlas.findRegion(THUNDER02));	break;
				case 3	:	sprite.setRegion(textureAtlas.findRegion(THUNDER03));	break;
				case 4	:	sprite.setRegion(textureAtlas.findRegion(THUNDER04));	break;
				case 5	:	sprite.setRegion(textureAtlas.findRegion(THUNDER05));	break;
				case 6	:	sprite.setRegion(textureAtlas.findRegion(THUNDER06));	break;
				}
				currentFrame++;
			}
		}
	}
	
	
/////////////////////////////////////METODI PER LA GESTIONE DEL CORPO/////////////////////////////////
	
	@Override
	public void collide(GraphicEntity actor2) 
	{
		if(actor2 instanceof Player)
		{
			Player pg = (Player) actor2;
			pg.getHit();
			System.out.println(pg.getHealth());
		}
	}
}