package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * Rappresenta a schermo la velocità del giocatore. E' espressa attraverso un moltiplicatore,
 * che può andare da 0.50x minimo, fino ad un massimo di 2.0x.
 * @author Lorenzo Ricci
 *
 */
public class CurrentSpeed implements HUDComponent
{
	
//////////////////////////CAMPI DELLA CLASSE//////////////////////
	
	/**
	 * Giocatore da cui ricavare le statistiche.
	 */
	private Player player;
	
	/**
	 * Font per scrivere testo a schermo.
	 */
	private BitmapFont font;
	
	/**
	 * Sprite della velocità.
	 */
	private Sprite sprite;
	

//////////////////////////COSTRUTTORE//////////////////////
	
	/**
	 * Costruisce a display il moltiplicatore corrente di velocità del giocatore.
	 * @param player Giocatore a cui associare le statistiche.
	 */
	public CurrentSpeed(Player player)
	{
		//Setup dei dati.
		this.player = player;
		font = new BitmapFont();
		//Setup dello sprite.
		Texture texture = new Texture(Gdx.files.internal("BasicHUD/Speed.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * 1.75f, sprite.getHeight() * 1.65f);
		sprite.setPosition(-450, 354);
	}
	
	
//////////////////////METODI GETTER E SETTER///////////////////////
	
	@Override
	public void resetToNewPlayer(Player player) {this.player = player;}
	
	
///////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT//////////////
	
	@Override
	public void draw(Batch batch) 
	{
		sprite.draw(batch);
		font.getData().setScale(1.5f);
		font.draw(batch, "x" + Float.toString(player.getSpeed()), -415, 375);
	}

	
}
