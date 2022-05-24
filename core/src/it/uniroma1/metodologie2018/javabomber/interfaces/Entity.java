package it.uniroma1.metodologie2018.javabomber.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Contiene tutti i metodi di base per la gestione di un'entità di gioco.
 * @author Federico Scozzafava
 *
 */
public interface Entity
{
	/**
	 * Aggiorna lo stato dell'entità per ogni deltatime.
	 * @param deltaTime	Tempo di frequenza dell'aggiornamento.
	 */
	void update(float deltaTime);
	
	/**
	 * Restituisce la posizione attuale dell'entità.
	 * @return	Posizione attuale.
	 */
	Vector2 getPosition();
	
	/**
	 * Assegna una nuova posizione all'entità.
	 * @param position	Posizione da assegnare.
	 */
	void setPosition(Vector2 position);
	
	/**
	 * Restituisce informazioni sul body dell'entità.
	 * @return Il body dell'entità.
	 */
	Body getBody();
}
