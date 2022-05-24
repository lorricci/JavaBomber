package it.uniroma1.metodologie2018.javabomber.interfaces;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Contiene tutti i metodi di base per la gestione di un'entit� di gioco.
 * @author Federico Scozzafava
 *
 */
public interface Entity
{
	/**
	 * Aggiorna lo stato dell'entit� per ogni deltatime.
	 * @param deltaTime	Tempo di frequenza dell'aggiornamento.
	 */
	void update(float deltaTime);
	
	/**
	 * Restituisce la posizione attuale dell'entit�.
	 * @return	Posizione attuale.
	 */
	Vector2 getPosition();
	
	/**
	 * Assegna una nuova posizione all'entit�.
	 * @param position	Posizione da assegnare.
	 */
	void setPosition(Vector2 position);
	
	/**
	 * Restituisce informazioni sul body dell'entit�.
	 * @return Il body dell'entit�.
	 */
	Body getBody();
}
