package it.uniroma1.metodologie2018.javabomber.interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Estende l'interfaccia Entity per assegnare metodi per la rappesentazione
 * a schermo dell'entit� di gioco.
 * @author Federico Scozzafava
 *
 */
public interface GraphicEntity extends Entity
{
	/**
	 * Disegna lo sprite dell'entit� attraverso il batch in input.
	 * @param batch	Batch utilizzato per la rappresentazione dello sprite.
	 */
	void draw(Batch batch);
	
	/**
	 * Restituisce informazioni sulla grandezza dell'entit�.
	 * @return Grandezza dell'entit�.
	 */
	Vector2 getSize();
	
	/**
	 * Specifica come ogni entit� grafica gestice il posizionamento dello sprite, in relazione
	 * alla posizione del suo body.
	 * @param body Body dell'entit�.
	 * @param sprite Sprite dell'entit�.
	 * 
	 * @author Lorenzo Ricci
	 */
	void handleSprite(Body body, Sprite sprite);
	
	/**
	 * Delega ad ogni entit� grafica la gestione della collisioni con altre entit� di gioco.
	 * @param actor2 Entit� con cui si verifica la collisione.
	 * 
	 * @author Lorenzo Ricci
	 */
	void collide(GraphicEntity actor2);
	 
	/**
	  * Delega all'entit� la distruzione del body e della fixture ed i dispose necessari. 
	  *	@author Lorenzo Ricci
	  */
	void killEntity();
	
	/**
	 * Delega all'entit� il dispose della texture.
	 */
	void disposeTexture();
}
