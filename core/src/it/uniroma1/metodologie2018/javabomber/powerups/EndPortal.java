package it.uniroma1.metodologie2018.javabomber.powerups;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.PowerUpTextures.END_PORTAL;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Characters;
import it.uniroma1.metodologie2018.javabomber.entities.Enemy;
import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
	
/**
 * Rappresenta il portale di fine livello.
 * @author Lorenzo Ricci
 *
 */
public class EndPortal extends PowerUp 
{
	
//////////////////////////COSTRUTTORE//////////////////////////////
	
	/**
	 * Costruisce il portale facendo un setup dello sprite e del controller.
	 * @param controller World legato al powerup.
	 * @param position Posizione del powerUp.
	 */
	public EndPortal(WorldController controller, Vector2 position) 
	{
		//Setup delle informazioni.
		super(controller, position);
		//Setup dello sprite.
		texture = new Texture(Gdx.files.internal(END_PORTAL));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() / 2f / PPM, sprite.getHeight() / 2f / PPM);
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
	

///////////////////////METODI PER L'APPLICAZIONE DEL POWERUP//////////////////////////

	@Override
	//NON AVENDO ALTRI LIVELLI, PER DIMOSTRAZIONE SEMPLICEMENTE PRINTA IN CONSOLE CHI E' PASSATO PER IL PORTALE PER 
	//AVERE UN FEEDBACK DEL PICKUP E LO TELETRASPORTA AL SUO SPAWN.
	public void powerStats(Characters subject) 
	{
		if(subject instanceof Player)
		{
			subject.setTransformPos(new Vector2(-528 / PPM, 304 / PPM));
			System.out.println("Il giocatore è passato per il portale!");
		}
		else if(subject instanceof Enemy)	
		{
			subject.setTransformPos(new Vector2(560 / PPM, -336 / PPM));
			System.out.println("Il nemico è passato per il portale!");
		}
		//game.startNextLevel() potrebbe essere un ipotetico metodo chiamato dal power-up.
	}

}
