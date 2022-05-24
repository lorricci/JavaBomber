package it.uniroma1.metodologie2018.javabomber;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.uniroma1.metodologie2018.javabomber.interfaces.GameScreen;
import it.uniroma1.metodologie2018.javabomber.screens.PlayScreen;

/**
 * Descrive i requisiti principali necessari alla creazione di una istanza di gioco.
 * @author Federico Scozzafava
 *
 */
public class JavaBomber extends Game 
{
	/**
	 * Batch utilizzata dal gioco.
	 */
	private Batch batch;
	
	@Override
	public void create () 
	{
		// Set Libgdx log level
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void setScreen(Screen screen) 
	{
		super.setScreen(screen);
		if(screen instanceof GameScreen)
		{
			GameScreen newScreen = (GameScreen)screen;
			Gdx.input.setInputProcessor(newScreen);
		}
		else
			Gdx.input.setInputProcessor(null);
	}

	/**
	 * Restituisce la batch utilizzata dal gioco.
	 * @return Batch del gioco. 
	 */
	public Batch getBatch() {return batch;}

	@Override
	public void dispose() 
	{
		batch.dispose();
		super.dispose();
	}

}
