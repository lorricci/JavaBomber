package it.uniroma1.metodologie2018.javabomber.powerups;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Characters;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.screens.GameTimer;
import static it.uniroma1.metodologie2018.javabomber.utils.PowerUpTextures.TIMER_DOWN;
/**
 * Diminuisce il tempo a disposizione per la conclusione del match.
 * 
 * @author Lorenzo Ricci
 */
public class TimerDown extends PowerUp
{

////////////////////CAMPI DELLA CLASSE///////////////////////
	
	/**
	 * Timer del match.
	 */
	private GameTimer gameTimer;
	
	
//////////////////COSTRUTTORE//////////////////////////////
	
	/**
	 * Costruisce il powerup facendo un setup dello sprite e del controller, oltre che settare il gameTimer.
	 * @param controller World legato al powerup.
	 * @param position Posizione del powerup.
	 */
	public TimerDown(WorldController controller, Vector2 position) 
	{
		//Setup delle informazioni.
		super(controller, position);
		setGameTimer(controller);
		//Setup dello sprite.
		texture = new Texture(Gdx.files.internal(TIMER_DOWN));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() *  2f / PPM, sprite.getHeight() * 2f / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
	}

	
///////////////////////METODI GETTER E SETTER////////////////////////

	/**
	 * Inizializza il gameTimer prendendolo dalle entità del world.
	 * @param controller World legato al timer.
	 */
	private void setGameTimer(WorldController controller)	
	{gameTimer = controller.getHUD().getGameTimer();}

	
////////////////////////METODI PER IL DISEGNO DELLO SPRITE/////////////////////

	@Override
	public void draw(Batch batch) {sprite.draw(batch);}
	
	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);}
	
	@Override
	public void update(float deltaTime) {handleSprite(body, sprite);}
	

/////////////////////METODI PER L'APPLICAZIONE DEL POWERUP//////////////////
	@Override
	public void powerStats(Characters subject) {gameTimer.subMinute();}
}