package it.uniroma1.metodologie2018.javabomber.screens;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.HEIGHT;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;

import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.interfaces.HUDComponent;

/**
 * L'HUD del gioco. Rappresenta a schermo tutte le informazioni sulle statistiche del giocatore.
 * @author Lorenzo Ricci
 *
 */
public class BasicHUD implements HUDComponent 
{
	
//////////////////////////////////CAMPI DELLA CLASSE/////////////////////////////
	
	/**
	 * Timer della partita.
	 */
	private GameTimer gameTimer;
	
	/**
	 * Velocità del giocatore.
	 */
	private CurrentSpeed currentSpeed;
	
	/**
	 * Vite rimaste al giocatore.
	 */
	private LivesLeft livesLeft;
	
	/**
	 * Distanza delle esplosioni espressa in blocchi.
	 */
	private CurrentBlockRange blockRange;
	
	/**
	 * Numero massimo di bombe piazzabili.
	 */
	private MaxBombs maxBombs;
	
	/**
	 * Vita rimasta all'armatura.
	 */
	private CurrentArmor currentArmor;
	
	/**
	 * Possibilità di passare le bombe.
	 */
	private PassBombsHUD bombPassing;
	
	/**
	 * Possibilità di spingere le bombe.
	 */
	private PushBombsHUD bombPushing;
	
	/**
	 * Camera per la gestione dell'HUD.
	 */
	private OrthographicCamera hudCamera;
	

///////////////////////////////COSTRUTTORE////////////////////////////////
	
	/**
	 * Costruisce l'HUD completa del gioco. Inizializza ciascuno
	 * dei componenti legandoli al giocatore passato in input.
	 * @param player Giocatore a cui associare i valori dell'HUD.
	 */
	public BasicHUD(Player player)
	{
		//Setup della camera e del player di riferimento.
		hudCamera = new OrthographicCamera(WIDTH, HEIGHT);
		hudCamera.position.set(WIDTH / 2f, HEIGHT / 2f, 0);
		//Setup delle componenti HUD.
		gameTimer = new GameTimer();
		currentSpeed = new CurrentSpeed(player);
		livesLeft = new LivesLeft(player);
		blockRange = new CurrentBlockRange(player);
		maxBombs = new MaxBombs(player);
		currentArmor = new CurrentArmor(player);
		bombPassing = new PassBombsHUD(player);
		bombPushing = new PushBombsHUD(player);
	}
	
	
///////////////////////////METODI GETTER E SETTER/////////////////////////////
	
	/**
	 * Restituisce lo stato attuale del timer di gioco.
	 * @return Il timer di gioco.
	 */
	public GameTimer getGameTimer()	{return gameTimer;}

	@Override
	public void resetToNewPlayer(Player player) 
	{
		currentSpeed.resetToNewPlayer(player);
		livesLeft.resetToNewPlayer(player);
		blockRange.resetToNewPlayer(player);
		maxBombs.resetToNewPlayer(player);
		currentArmor.resetToNewPlayer(player);
		bombPassing.resetToNewPlayer(player);
		bombPushing.resetToNewPlayer(player);
	}
	
	
//////////////////////METODI PER IL DISEGNO DELLO SPRITE E DEL FONT/////////////////////
	
	@Override
	public void draw(Batch batch) 
	{
		batch.setProjectionMatrix(hudCamera.combined);
		gameTimer.draw(batch);
		currentSpeed.draw(batch);
		livesLeft.draw(batch);
		blockRange.draw(batch);
		maxBombs.draw(batch);
		currentArmor.draw(batch);
		bombPassing.draw(batch);
		bombPushing.draw(batch);
	}

	/**
	 * Esegue l'update di ogni componente dell'hud che lo richiede.
	 * @param deltaTime DeltaTime del gioco.
	 */
	public void update(float deltaTime) {gameTimer.update(deltaTime);}

}
