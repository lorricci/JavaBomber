package it.uniroma1.metodologie2018.javabomber.interfaces;

/**
 * Indica che la classe è "teletrasportabile". Utilizzata per 
 * applicare trasformazioni del body che porterebbero a crash di gioco.
 * @author Lorenzo Ricci
 *
 */
public interface Teleportable 
{
	/**
	 * Delega all'entità la gestione di eventuali transform del body o altre operazioni
	 * generiche sul body che porterebbero al crash del World.
	 */
	void applyTransforms();
}
