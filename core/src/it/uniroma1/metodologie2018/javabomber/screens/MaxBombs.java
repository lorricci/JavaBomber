package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * Rappresenta il numero massimo di bombe piazzabili contemporaneamente dal giocatore.
 * @author Lorenzo Ricci
 *
 */
public class MaxBombs implements HUDComponent
{
	
////////////////////////CAMPI DELLA CLASSE/////////////////////////////
	
	/**
	 * Giocatore da cui ricavare le statistiche.
	 */
	private Player player;
	
	/**
	 * Font per scrivere testo a schermo.
	 */
	private BitmapFont font;
	
	/**
	 * Sprite della bomba.
	 */
	private Sprite sprite;
	
	
//////////////////////////COSTRUTTORE////////////////////////////////
	
	/**
	 * Costruisce a display il numero massimo di bombe piazzabili dal
	 * giocatore al momento.
	 * @param player Giocatore a cui associare le statistiche.
	 */
	public MaxBombs(Player player)
	{
		//Setup dei dati.
		this.player = player;
		font = new BitmapFont();
		//Setup dello sprite.
		Texture texture = new Texture(Gdx.files.internal("BasicHUD/Bombs.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * 1.75f, sprite.getHeight() * 1.65f);
		sprite.setPosition(-210, 354);
	}
	
/////////////////////////METODI GETTER E SETTER///////////////////////////
	
	@Override
	public void resetToNewPlayer(Player player) {this.player = player;}
	
////////////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT////////////////
	
	@Override
	public void draw(Batch batch) 
	{
		sprite.draw(batch);
		font.getData().setScale(1.5f);
		font.draw(batch, Integer.toString(player.getBombStats().getMaxBombs()), -175, 375);
	}
}
