package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * Rappresenta il numero di vite rimanenti al giocatore.
 * @author Lorenzo Ricci
 *
 */
public class LivesLeft implements HUDComponent 
{
	
/////////////////////////CAMPI DELLA CLASSE//////////////////////
	
	/**
	* Giocatore da cui ricavare le statistiche.
	*/
	private Player player;
	
	/**
	 * Font per scrivere testo a schermo.
	 */
	private BitmapFont font;
	
	/**
	 * Sprite delle vite.
	 */
	private Sprite sprite;
	
	
/////////////////////////COSTRUTTORE////////////////////////////
	
	/**
	 * Costruisce a display il numero di vite rimanenti al giocatore.
	 * @param player Giocatore a cui associare le statistiche.
	 */
	public LivesLeft(Player player)
	{
		//Setup delle info.
		this.player = player;
		font = new BitmapFont();
		//Setup dello sprite.
		Texture texture = new Texture(Gdx.files.internal("BasicHUD/Lives.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * 1.75f, sprite.getHeight() * 1.65f);
		sprite.setPosition(-345, 354);
	}
	

////////////////////METODI GETTER E SETTER////////////////////
	
	@Override
	public void resetToNewPlayer(Player player) {this.player = player;}
	

/////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT/////////////////
	@Override
	public void draw(Batch batch) 
	{
		sprite.draw(batch);
		font.getData().setScale(1.5f);
		font.draw(batch, Integer.toString(player.getHealth()), -310, 375);
	}
}
