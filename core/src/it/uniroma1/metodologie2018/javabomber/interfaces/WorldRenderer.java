package it.uniroma1.metodologie2018.javabomber.interfaces;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Disposable;

/**
 * Implementa i metodi per la corretta gestione del rendering del mondo di gioco.
 * @author Federico Scozzafava
 *
 */
public interface WorldRenderer extends Disposable
{
	/**
	 * Specifica i metodi di rendering del mondo di gioco.
	 */
	void render();

	/**
	 * Ridimensiona la finestra di gioco.
	 * @param width Larghezza della finestra.
	 * @param height Altezza della finestra.
	 */
	void resize(int width, int height);
	
	/**
	 * Restituisce la camera di gioco.
	 * @return La camera di gioco.
	 * 
	 * @author Lorenzo Ricci
	 */
	OrthographicCamera getGameCamera();
	
}
