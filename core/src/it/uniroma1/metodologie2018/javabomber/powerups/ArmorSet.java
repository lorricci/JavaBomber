package it.uniroma1.metodologie2018.javabomber.powerups;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.PowerUpTextures.ARMOR_SET;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.entities.Characters;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;

/**
 * Conferisce una armatura al personaggio che la raccoglie.
 * Mentre permette di subire due colpi extra, rallenta il personaggio di un quarto della sua velocit�.
 * @author Lorenzo Ricci
 *
 */
public class ArmorSet extends PowerUp
{

////////////////////////////////COSTRUTTORE///////////////////////
	
	/**
	 * Costruisce il powerup facendo un setup del controller e dello sprite.
	 * @param controller World legato al powerup.
	 * @param position Posizione del powerUp.
	 */
	public ArmorSet(WorldController controller, Vector2 position) 
	{
		//Setup delle informazioni.
		super(controller, position);
		//Setup dello sprite.
		texture = new Texture(Gdx.files.internal(ARMOR_SET));
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

	
////////////////////////METODI PER L'APPLICAZIONE DEL POWERUP///////////////////////
	
	@Override
	public void powerStats(Characters subject) {if(!subject.hasArmor()) subject.wearArmor();}
}