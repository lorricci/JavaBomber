package it.uniroma1.metodologie2018.javabomber.map;

import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.FIRETILE00;
import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.FIRETILE01;
import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.FIRETILE02;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Bomb;
import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;
/**
 * Rappresenta il pavimento infuocato dal boss. Danneggia il giocatore
 * se questo viene a contatto con il tile.
 * @author Lorenzo Ricci
 *
 */
public class BurningTile extends Tile
{
//////////////////////////CAMPI DELLA CLASSE//////////////////////
	
	/**
	 * Tempo a schermo del ghiaccio del tile.
	 */
	private float timeOnScreen = 20.0f;
	
	/**
	 * Frame di animazione del tile.
	 */
	private int currentFrame;
	
	/**
	 * Accumulatore di delay per l'animazione del tile.
	 */
	private float animFrameDelay = 0.0f;
	
	/**
	 * Tempo di attesa tra un frame e l'altro.
	 */
	private float timeFrames = 0.20f;
	
	
/////////////////////////////COSTRUTTORE/////////////////////////////
	
	/**
	 * Costruisce il pavimento infuocato.
	 * @param position Posizione del tile.
	 * @param controller World del gioco.
	 */
	public BurningTile(Vector2 position, WorldController controller) 
	{
		//Setup delle infos.
		super(position, controller);
		//Setup del body.
		body = dispenser.dispenseFloorBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(FIRETILE00));
		sprite.setSize(sprite.getWidth() / 2f / PPM, sprite.getHeight() / 2f / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
	}

	
/////////////////////////////METODI GETTER E SETTER////////////////////////

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
	
	
/////////////////////////////METODI PER IL DISEGNO DELLO SPRITE///////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite)
	{sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (sprite.getHeight() / 2f) );}
	

	@Override
	public void update(float deltaTime) 
	{
		timeOnScreen -= deltaTime;
		animFrameDelay += deltaTime;
		if(timeOnScreen <= 0) killEntity();
		else
		{
			handleSprite(body, sprite);
			displayAnimation();
		}
	}

	/**
	 * Gestisce l'animazione del fuoco presente sul tile.
	 */
	private void displayAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(FIRETILE00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(FIRETILE01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(FIRETILE02)); break;
			default :
			{
				currentFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(FIRETILE00));
			}
			}
			currentFrame++;
		}
	}
	
///////////////////////////METODI PER LA GESTIONE DEL BODY//////////////////////////////////////

	@Override
	public void collide(GraphicEntity actor2) 
	{
		if(actor2 instanceof Player)
		{
			Player pg = (Player) actor2;
			pg.getHit();
		}
		else if(actor2 instanceof Bomb)
		{
			Bomb bomb = (Bomb) actor2;
			bomb.setTimer(0);
		}
	}

	@Override
	public void killEntity() 
	{
		setDataReady();
		controller.getToRemove().add(this);
	}

}