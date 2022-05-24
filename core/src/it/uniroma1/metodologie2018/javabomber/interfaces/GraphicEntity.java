package it.uniroma1.metodologie2018.javabomber.interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Estende l'interfaccia Entity per assegnare metodi per la rappesentazione
 * a schermo dell'entità di gioco.
 * @author Federico Scozzafava
 *
 */
public interface GraphicEntity extends Entity
{
	/**
	 * Disegna lo sprite dell'entità attraverso il batch in input.
	 * @param batch	Batch utilizzato per la rappresentazione dello sprite.
	 */
	void draw(Batch batch);
	
	/**
	 * Restituisce informazioni sulla grandezza dell'entità.
	 * @return Grandezza dell'entità.
	 */
	Vector2 getSize();
	
	/**
	 * Specifica come ogni entità grafica gestice il posizionamento dello sprite, in relazione
	 * alla posizione del suo body.
	 * @param body Body dell'entità.
	 * @param sprite Sprite dell'entità.
	 * 
	 * @author Lorenzo Ricci
	 */
	void handleSprite(Body body, Sprite sprite);
	
	/**
	 * Delega ad ogni entità grafica la gestione della collisioni con altre entità di gioco.
	 * @param actor2 Entità con cui si verifica la collisione.
	 * 
	 * @author Lorenzo Ricci
	 */
	void collide(GraphicEntity actor2);
	 
	/**
	  * Delega all'entità la distruzione del body e della fixture ed i dispose necessari. 
	  *	@author Lorenzo Ricci
	  */
	void killEntity();
	
	/**
	 * Delega all'entità il dispose della texture.
	 */
	void disposeTexture();
}
