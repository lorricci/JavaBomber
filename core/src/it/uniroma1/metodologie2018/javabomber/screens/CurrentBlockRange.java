package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * Rappresenta a schermo il range dell'esplosione attualmente in uso.
 * Il range viene espresso in blocchi, quindi ogni blocco è uguale a 32.0f di distanza.
 * @author Lorenzo Ricci
 *
 */
public class CurrentBlockRange implements HUDComponent
{
	
///////////////////CAMPI DELLA CLASSE/////////////////
	
	/**
	 * Giocatore da cui ricavare le statistiche.
	 */
	private Player player;
	
	/**
	 * Font per scrivere testo a schermo.
	 */
	private BitmapFont font;
	
	/**
	 * Sprite del range dell'esplosione.
	 */
	private Sprite sprite;
	
	
////////////////////////COSTRUTTORE///////////////////
	
	/**
	 * Imposta il display del range dell'esplosione delle bombe 
	 * piazzabili dal giocatore.
	 * @param player Giocatore a cui associare il display.
	 */
	public CurrentBlockRange(Player player)
	{
		//Setup delle info.
		this.player = player;
		font = new BitmapFont();
		//Setup dello sprite.
		Texture texture = new Texture(Gdx.files.internal("BasicHUD/Range.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * 1.75f, sprite.getHeight() * 1.65f);
		sprite.setPosition(-275, 354);
	}
	
	
/////////////////////METODI GETTER E SETTER//////////////////
	
	@Override
	public void resetToNewPlayer(Player player) {this.player = player;}
	
	
////////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT/////////////
	
	@Override
	public void draw(Batch batch) 
	{
		sprite.draw(batch);
		font.getData().setScale(1.5f);
		font.draw(batch, Integer.toString((int) (player.getBombStats().getRange() / 32)), -240, 375);
	}

	
}
