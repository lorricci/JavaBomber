package it.uniroma1.metodologie2018.javabomber.interfaces;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

/**
 * Indica che la classe implementata rappresenta la mappa di gioco.
 * @author Federico Scozzafava
 *
 */
public interface Map 
{
	/**
	 * Renderizza la mappa.
	 */
	void render();
	
	/**
	 * Inizializza la mappa.
	 */
	void init();
	
	/**
	 * Esegue un update dello stato della mappa per ogni deltaTime.
	 * @param deltaTime Tempo delta del gioco.
	 */
	void update(float deltaTime);
	
	/**
	 * Riporta la mappa allo stato iniziale.
	 */
	void reset();
	
	/**
	 * Restituisce la grandezza (x,y) della mappa in numero di blocchi.
	 * @return Grandezza (x,y) della mappa.
	 */
	Vector2 getSize();
	
	/**
	 * Restituisce la lista di blocchi che costituiscono la mappa.
	 * @return Lista dei blocchi della mappa.
	 */
	List<Block> getBlocks();
	
	/**
	 * Inserisce una entità all'interno della mappa in base alla sua posizione.
	 * @param e  Entità da inserire.
	 * @param pos Posizione in cui piazzare l'entità
	 */
	void putEntity(Entity e, Vector2 pos);
	
	/**
	 * Restituisce il blocco presente nella posizione indicata in input.
	 * @param pos Posizione del blocco voluto.
	 * @return Blocco presente nella posizione indicata.
	 */
	Entity getBlock(Vector2 pos);
	
	/**
	 * Restituisce l'entità presenta nella posizione indicata in input.
	 * @param pos Posizione dell'entità.
	 * @return Entità presente nella posizione indicata.
	 */
	Entity getEntity(Vector2 pos);
}
