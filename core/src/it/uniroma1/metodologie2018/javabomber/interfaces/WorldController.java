package it.uniroma1.metodologie2018.javabomber.interfaces;

import java.util.List;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;

import it.uniroma1.metodologie2018.javabomber.entities.Bomb;
import it.uniroma1.metodologie2018.javabomber.screens.BasicHUD;

/**
 * Implementa i metodi di base per la gestione del mondo fisico di gioco.
 * @author Federico Scozzafava
 *
 */
public interface WorldController extends InputProcessor,Disposable
{
	/**
	 * Aggiorna il mondo ogni intervallo deltaTime. 
	 * @param deltaTime Intervallo di aggiornamento.
	 */
	void update(float deltaTime);
	
	/**
	 * Aggiunge una entità alle entità del World.
	 * @param e	Entità da aggiungere.
	 */
	void addEntity(Entity e);
	
	/**
	 * Rimuove una entità dalle entità del World.
	 * @param e Entità da rimuovere.
	 */
	void removeEntity(Entity e);
	
	/**
	 * Restituisce la lista di entità del World.
	 * @return	Lista di entità del World.
	 */
	List<Entity> getEntities();
	
	/**
	 * Restituisce la lista di entità che verranno rimosse dal World nel prossimo step.
	 * @return	Lista di entità da rimuovere.
	 * 
	 * @author Lorenzo Ricci
	 */
	List<Entity> getToRemove();
	
	/**
	 * Restituisce la lista di entità che verranno aggiunte al World nel prossimo step.
	 * @return	Lista di entità da aggiungere.
	 * 
	 * @author Lorenzo Ricci
	 */
	List<Entity> getToAdd();
	
	/**
	 * Restituisce il livello corrente.
	 * @return	Il livello attuale.
	 */
	Level getLevel();
	
	/**
	 * Restituisce il World utilizzato.
	 * @return World attualmente in uso.
	 * 
	 * @author Lorenzo Ricci
	 */
	World getWorld();

	/**
	 * Restituisce la lista di oggetti da teletrasportare nel prossimo world.step
	 * @return Lista di body da teletrasportare.
	 */
	List<Teleportable> getToTeleport();
	
	/**
	 * Restituisce l'HUD del gioco.
	 * @return HUD del gioco.
	 */
	BasicHUD getHUD();

	/**
	 * Setta l'HUD utilizzata dal gioco con quella in input.
	 * @param basicHUD HUD da applicare al gioco.
	 */
	void setHUD(BasicHUD basicHUD);

	/**
	 * Restituisce la lista contenente tutte le bombe attualmente presenti nel World.
	 * @return Lista di bombe del world.
	 */
	List<Bomb> getPresentBombs();
	
	/**
	 * Restituisce la lista di bombe da rimuovere dal World.
	 * @return Lista di bombe da rimuovere.
	 */
	List<Bomb> getToRemoveBombs();

	/**
	 * Restituisce la lista contenente tutti i tile della mappa.
	 * @return Lista dei tile della mappa.
	 */
	List<Block> getMapRender();
}