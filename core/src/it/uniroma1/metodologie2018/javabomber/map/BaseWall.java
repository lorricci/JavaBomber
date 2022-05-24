package it.uniroma1.metodologie2018.javabomber.map;

import static it.uniroma1.metodologie2018.javabomber.utils.BlockTextures.BASEWALL;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.powerups.PowerUp;
import it.uniroma1.metodologie2018.javabomber.powerups.PowerUpFactory;
import it.uniroma1.metodologie2018.javabomber.powerups.PowerUpTypes;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;
/**
 * Rappresenta il muro distruttibile di base che costituisce una mappa di BomberMan.
 * @author Lorenzo Ricci
 *
 */
public class BaseWall extends Wall
{ 
	
//////////////////////////////////COSTRUTTORE////////////////////////////////
	
	/**
	 * Costruisce un muro distruttibile, impostando i suoi hitPoints ad 1.
	 * @param position Posizione del muro.
	 * @param controller World associato al muro.
	 */
	public BaseWall(Vector2 position, WorldController controller) 
	{
		//Setup dei dati.
		super(position, controller);	
		hitPoints = 1;
		//Setup del body.
		body = dispenser.dispenseWallBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(BASEWALL));
		sprite.setSize((sprite.getWidth() / 2) / PPM, (sprite.getHeight() / 2) / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}	
	
	
//////////////////////////METODI GETTER E SETTER////////////////////////////////////////
	
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

	
//////////////////////////METODI PER IL DISEGNO DELLO SPRITE E DELLE ANIMAZIONI////////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite)
	{sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (sprite.getHeight() / 2f) );}

	@Override
	public void update(float deltaTime) 	
	{
		handleSprite(body, sprite);
		if(hitPoints == 0)
		{
			Vector2 spawnPos = this.position;
			killEntity();
			spawnPowerUp(spawnPos);
		}
	}

	
/////////////////////METODI PER LA GESTIONE DELLE COLLISIONI E DEI DAMAGE/////////////////////////////////////
	
	@Override
	public void getHit()	{if(hitPoints > 0)	hitPoints--;}
	

	@Override
	public void killEntity() 
	{
		setDataReady();
		resetFloor();
		controller.getToRemove().add(this);
	}
	
	/**
	 * Resetta il booleano del pavimento a cui è associato il muro.
	 */
	private void resetFloor()
	{
		for(GraphicEntity bl : controller.getMapRender())
		{
			if(bl instanceof Floor)
				if(bl.getPosition().equals(this.getPosition()))
				{
					Floor floor = (Floor) bl;
					floor.resetWall();
				}
		}
	}
	
	
//////////////////////METODI PER LO SPAWN DEI POWERUP///////////////////////////////////////////
	
	/**
	 * Spawna con una certa probabilità, un PowerUp casuale nella posizione del muro distrutto.
	 * Utilizza la factory per decidere il tipo di PowerUp a runtime.
	 */
	private void spawnPowerUp(Vector2 spawnPos)
	{
		Random randomizer = new Random();
		if(randomizer.nextInt(3) > 1) 
		{
			PowerUpTypes[] typeList = PowerUpTypes.values();
			int toGet = randomizer.nextInt(typeList.length);
			if(!(typeList[toGet] == PowerUpTypes.END_PORTAL && PowerUpFactory.hasPortalSpawned()))
			{
				PowerUp toSpawn = PowerUpFactory.producePowerUp(typeList[toGet], controller, spawnPos);
				toSpawn.setBody(dispenser.dispensePowerUpBody(spawnPos, toSpawn.getSize(), toSpawn));
				controller.getToAdd().add(toSpawn);
			}
		}
	}
}
