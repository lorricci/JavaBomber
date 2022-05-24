package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * Rappresenta il tempo rimasto nella partita attuale.
 * Contiene metodi per la rappresentazione a schermo del timer, e il setting di un diverso timer.
 * 
 * @author Lorenzo Ricci
 */
public class GameTimer implements HUDComponent
{	
	
//////////////////////CAMPI DELLA CLASSE////////////////////////////
	
	/**
	 * Tempo totale del match espresso in secondi.
	 * (Utilizza un intero per la gestione della suddivisione in minuti : secondi nella rappresentazione).
	 */
	private int total = 300;
	
	/**
	 * Secondi attualmente in display.
	 */
	private float seconds = 0f;
	
	/**
	 * Font per la rappresentazione a schermo del timer.
	 */
	private BitmapFont font;
	
	/**
	 * Stringa da rappresentare a schermo tramite il font.
	 */
	private String toScreen;
	
	/**
	 * Sprite del timer.
	 */
	private Sprite sprite;
	
	
////////////////////////////////////COSTRUTTORE/////////////////////////////////////
	
	/**
	 * Costruisce il GameTimer per rappresentarlo a schermo. Inizializza la camera e il formato della stringa.
	 */
	public GameTimer()
	{
		//Setup dei dati.
		font = new BitmapFont();
		//Setup dello sprite e della stringa del timer a schermo.
		toScreen = 0 + total / 60 + " : " + 0 + String.format("%.0f", seconds);
		Texture texture = new Texture(Gdx.files.internal("BasicHUD/Timer.png"));
		sprite = new Sprite(texture);
		sprite.setSize(sprite.getWidth() * 1.75f, sprite.getHeight() * 1.65f);
		sprite.setPosition(-575, 354);
	}


///////////////////////METODI GETTER E SETTER///////////////////////////
	
	/**
	 * Indica se il timer della partita è finito.
	 * @return True se il timer è finito, false altrimenti.
	 */
	public boolean isFinished()	{return total < 0;}
	
	@Override
	public void resetToNewPlayer(Player player) {/*Nessuna specifica*/}
	

//////////////////METODI PER L'INCREMENTO E DECREMENTO DELLE STATS///////////////////
	
	/**
	 * Aggiunge un minuto al timer.
	 */
	public void addMinute()	{total += 60;}
	
	/**
	 * Toglie un minuto al timer.
	 */
	public void subMinute()	{total -= 60;}

	
//////////////////METODI PER L'UPDATE DEL TIMER A SCHERMO//////////////////////
	
	public void update(float deltaTime) 
	{
		if(total < 0)	
		{
			toScreen = "Time's up";
			//suddenDeath() o un qualsiasi metodo come game.setScreen(new GameOver());
		}
		else
		{
			if(seconds <= 0f)	//Resetta i secondi a 0 e scala il tempo totale di un minuto.	
			{
				seconds = 60f;
				total -= 60;
			}
			seconds -= deltaTime;
			updateToScreen();
		}
	}

	/**
	 * Aggiorna la stringa da rappresentare tramite il font, anteponendo degli 0 se le cifre sono minori di 10.
	 */
	private void updateToScreen()	
	{
		if(total / 60 < 10)
			toScreen = seconds < 10  ? 	0 + total / 60 + " : " + 0 + String.format("%.0f", seconds) : 
										0 + total / 60 + " : " + String.format("%.0f", seconds);
		else
			toScreen = seconds < 10 ? total / 60 + " : " + 0 + String.format("%.0f", seconds) :
									  total / 60 + " : " + String.format("%.0f", seconds);
	}
	
	
///////////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT/////////////////////
	
	@Override
	public void draw(Batch batch)
	{
		font.getData().setScale(1.5f);
		font.draw(batch, toScreen, -535, 375);
		sprite.draw(batch);
	}
}