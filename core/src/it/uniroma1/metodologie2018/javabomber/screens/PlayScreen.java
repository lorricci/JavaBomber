package it.uniroma1.metodologie2018.javabomber.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.math.Vector2;

import it.uniroma1.metodologie2018.javabomber.JavaBomber;
import it.uniroma1.metodologie2018.javabomber.entities.BowBoar;
import it.uniroma1.metodologie2018.javabomber.entities.Enemy;
import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.entities.WizardOfTheElements;
import it.uniroma1.metodologie2018.javabomber.interfaces.GameScreen;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldRenderer;
import it.uniroma1.metodologie2018.javabomber.map.GameGrid;
import it.uniroma1.metodologie2018.javabomber.world.JavaBomberWorldController;
import it.uniroma1.metodologie2018.javabomber.world.JavaBomberWorldRenderer;

/**
 * Rappresenta il gestore principale della schermata e simulazione fisica del gioco.
 * COMANDI DI SPAWN
 * F = SPAWN DEL NEMICO BASE (BOWBOAR)
 * G = SPAWN DEL BOSS (WIZARDOFTHELEMENTS)
 * @author Federico Scozzafava + (Lorenzo Ricci)
 *
 */
public class PlayScreen implements GameScreen
{
	
///////////////////CAMPI DELLA CLASSE////////////////
	
	/**
	 * Controller del mondo legato al gioco.
	 */
	private WorldController worldController;
	
	/**
	 * Renderer delle entità di gioco.
	 */
	private WorldRenderer worldRenderer;
	
	/**
	 * Personaggio controllato dal giocatore.
	 */
	private Player player;
	
	/**
	 * Griglia di gioco.
	 */
	private GameGrid grid;
	
	/**
	 * Stato di pausa del gioco.
	 */
	private boolean paused;
	
/////////////////////////COSTRUTTORE/////////////////////////
	
	/**
	 * Costruisce il gestore della simulazione di gioco.
	 * @param game Istanza del gioco.
	 */
	public PlayScreen(JavaBomber game) {
		//Setup del controller e del renderer
		worldController = new JavaBomberWorldController();
		worldRenderer = new JavaBomberWorldRenderer(game,worldController);
		paused = false;
		//Setup delle entità presenti
		this.player = new Player(worldController, new Vector2(-528,304));
		grid = new GameGrid(worldController);
		grid.setTestStage();
		init();
	}

	/**
	 * Inizializza la mappa, il renderer e l'hud di gioco.
	 */
	private void init()
	{
		worldController.getMapRender().addAll(grid.getGrid());
		worldController.addEntity(player);
		worldController.setHUD(new BasicHUD(player));
	}

	@Override
	public void render(float delta) {
		if(!paused)
		{
			worldController.getHUD().update(delta);
			worldController.update(delta);
		}
		Gdx.gl.glClearColor(0.2f, 0.5f, 0.3f, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {}

	@Override
	public void pause() {paused = true;}

	@Override
	public void resume() {paused = false;}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		worldController.dispose();
		worldRenderer.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		player.keyDown(keycode);
		
		switch(keycode)
		{
		case Keys.P : 
		{
			if(player.getHealth() <= 0)
			{
				this.player = new Player(worldController, new Vector2(-528, 304));
				worldController.getToAdd().add(player);
				worldController.getHUD().resetToNewPlayer(player);
			}
		}
		break;
		case Keys.F : {
			Enemy enemy = new BowBoar(worldController, new Vector2(560, -336), player);
			worldController.getToAdd().add(enemy);
			
		}
		break;
		case Keys.G:
		{
			Enemy enemy = new WizardOfTheElements(worldController, new Vector2(560, -336), player);
			worldController.getToAdd().add(enemy);
		}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		player.keyUp(keycode);
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
