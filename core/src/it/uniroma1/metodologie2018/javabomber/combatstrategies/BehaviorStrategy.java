package it.uniroma1.metodologie2018.javabomber.combatstrategies;

/**
 * Delega la gestione dell'approccio al movimento di un nemico ad ogni sua sottoclasse
 * attraverso lo strategy pattern.
 * @author Lorenzo Ricci
 *
 */
public interface BehaviorStrategy 
{
	/**
	 * Muove il nemico a seconda del suo pattern di movimento specificato
	 * nel metodo.
	 * @param deltaTime Tempo delta del gioco.
	 */
	void applyRules(float deltaTime);

	
}
