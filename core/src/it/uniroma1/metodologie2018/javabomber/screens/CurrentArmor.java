package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * Rappresentazione a schermo della vita rimanente all'armatura del giocatore.
 * @author Lorenzo Ricci
 *
 */
public class CurrentArmor implements HUDComponent
{
	
/////////////////////////////CAMPI DELLA CLASSE///////////////////////////
	
	/**
	 * Giocatore da cui ricavare le statistiche.
	 */
	private Player player;
	
	/**
	 * Font per scrivere testo a schermo.
	 */
	private BitmapFont font;
	
	/**
	 * Sprite dell'armatura.
	 */
	private Sprite sprite;
	
	
////////////////////////COSTRUTTORE/////////////////////////
	
	/**
	 * Costruisce il display dell'armatura rimasta al giocatore.
	 * @param player Giocatore a cui associare i valori.
	 */
	public CurrentArmor(Player player)
	{
		//Setup delle info.
		this.player = player;
		font = new BitmapFont();
		//Setup dello sprite.
		Texture texture = new Texture(Gdx.files.internal("BasicHUD/Armor.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * 1.75f, sprite.getHeight() * 1.65f);
		sprite.setPosition(-150, 354);
	}
	
	
/////////////////////////METODI GETTER E SETTER////////////////////////////
	
	@Override
	public void resetToNewPlayer(Player player) {this.player = player;}
	

//////////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT//////////////
	
	@Override
	public void draw(Batch batch) 
	{
		sprite.draw(batch);
		font.getData().setScale(1.5f);
		font.draw(batch, Integer.toString((int) (player.armorLeft())), -115, 375);
	}

	
}
