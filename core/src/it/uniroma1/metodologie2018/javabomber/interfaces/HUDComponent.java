package it.uniroma1.metodologie2018.javabomber.interfaces;

import com.badlogic.gdx.graphics.g2d.Batch;

import it.uniroma1.metodologie2018.javabomber.entities.Player;

/**
 * Indica che la classe fa parte delle componenti dell'HUD di gioco. 
 * @author Lorenzo Ricci
 *
 */
public interface HUDComponent 
{
	/**
	 * Disegna l'hud a schermo.
	 * @param batch Batch del gioco.
	 */
	void draw(Batch batch);
	
	/**
	 * Associa le statistiche dell'HUD ad un nuovo player.
	 * @param player Nuovo giocatore a cui associare le statistiche.
	 */
	void resetToNewPlayer(Player player);	
}
