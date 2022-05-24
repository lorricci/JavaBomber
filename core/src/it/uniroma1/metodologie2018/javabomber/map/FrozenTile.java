package it.uniroma1.metodologie2018.javabomber.map;

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
import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.*;
/**
 * Rappresenta il pavimento ghiacciato dal boss. Rallenta il giocatore dimezzando la sua
 * velocità fintanto che il giocatore è sul tile.
 * @author Lorenzo Ricci
 *
 */
public class FrozenTile extends Tile
{
//////////////////////////CAMPI DELLA CLASSE//////////////////////
	
	/**
	 * Tempo a schermo del ghiaccio del tile.
	 */
	private float timeOnScreen = 20.0f;
	
	/**
	 * Frame di animazione del tile.
	 */
	private int currentFrame = 0;
	
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
	 * Costruisce il pavimento ghiacciato dal boss.
	 * @param position Posizione del tile.
	 * @param controller World del gioco.
	 */
	public FrozenTile(Vector2 position, WorldController controller) 
	{
		//Setup delle infos.
		super(position, controller);
		//Setup del body.
		body = dispenser.dispenseFloorBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(ICETILE00));
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
	 * Gestisce l'animazione del ghiaccio presente sul tile.
	 */
	private void displayAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(currentFrame)
			{
			case 0 : sprite.setRegion(textureAtlas.findRegion(ICETILE00)); break;
			case 1 : sprite.setRegion(textureAtlas.findRegion(ICETILE01)); break;
			case 2 : sprite.setRegion(textureAtlas.findRegion(ICETILE02)); break;
			default :
			{
				currentFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(ICETILE00));
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
			pg.switchOnFrozenTile();
		}
		else if(actor2 instanceof Bomb)
		{
			Bomb bomb = (Bomb) actor2;
			bomb.getBody().setLinearVelocity(0,0);
		}
	}

	@Override
	public void killEntity() 
	{
		setDataReady();
		controller.getToRemove().add(this);
	}

}
