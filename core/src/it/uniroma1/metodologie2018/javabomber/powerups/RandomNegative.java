package it.uniroma1.metodologie2018.javabomber.powerups;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Characters;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import static it.uniroma1.metodologie2018.javabomber.utils.PowerUpTextures.RANDOM_NEGATIVE;
/**
 * Applica un effetto negativo a caso tra tutti i powerUp del gioco.
 * @author Lorenzo Ricci
 *
 */
public class RandomNegative extends PowerUp
{

//////////////////////COSTRUTTORE////////////////////////
	
	/**
	 * Costruisce un powerUp negativo a caso.
	 * @param controller World associato al gioco.
	 * @param position Posizione del powerup.
	 */
	public RandomNegative(WorldController controller, Vector2 position) 
	{
		//Setup dei dati.
		super(controller, position);
		//Setup dello sprite.
		texture = new Texture(Gdx.files.internal(RANDOM_NEGATIVE));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() *  2f / PPM, sprite.getHeight() * 2f / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
	}

	
////////////////////////METODI PER IL DISEGNO DELLO SPRITE/////////////////////

	@Override
	public void draw(Batch batch) {sprite.draw(batch);}
	
	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);}
	
	@Override
	public void update(float deltaTime) {handleSprite(body, sprite);}


//////////////////////METODI PER L'APPLICAZIONE DEL POWERUP////////////////////
	
	@Override
	public void powerStats(Characters subject) 
	{
		ArrayList<PowerUpTypes> types = filterNegative();
		Random random = new Random();
		PowerUp powerUp = PowerUpFactory.producePowerUp(types.get(random.nextInt(types.size())), controller, this.position);
		powerUp.powerStats(subject);
	}

	/**
	 * Filtra unicamente i tipi negativi di powerUp, escludendo il RandomNegative stesso.
	 * @return Lista di PowerUp negativi
	 */
	private ArrayList<PowerUpTypes> filterNegative()
	{
		ArrayList<PowerUpTypes> filteredPows = new ArrayList<>();
		for(PowerUpTypes pow : PowerUpTypes.values())
			if(!pow.givesBenefit() && pow != PowerUpTypes.RANDOM_NEGATIVE)
				filteredPows.add(pow);
		return filteredPows;
	}
	
}