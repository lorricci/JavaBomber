package it.uniroma1.metodologie2018.javabomber.combatstrategies;

import it.uniroma1.metodologie2018.javabomber.entities.Enemy;
import it.uniroma1.metodologie2018.javabomber.entities.Player;

/**
 * Rappresenta la strategia di avvicinamento al giocatore. 
 * @author Lorenzo Ricci
 *
 */
public class ApproachThreat extends AbstractStrategy
{

	/**
	 * Utilizza il supercostruttore per generare tutte le informazioni utili per avvicinarsi al giocatore.
	 * @param player Istanza del giocatore da "inseguire".
	 * @param enemy Istanza del nemico che userà l'approccio.
	 * @param movementsCD Cooldown del cambio di comportamento.
	 * @param resetValue Valore di reset del cooldown.
	 */
	public ApproachThreat(Player player, Enemy enemy, float movementsCD, float resetValue)	{super(player, enemy, movementsCD, resetValue);}
	
	@Override
	public void applyRules(float deltaTime)
	{
		movementsCD -= deltaTime;
		if(movementsCD <= 0)
		{
			applySimpleLane();
			applyFourZones();
		}
	}

	/**
	 * Controlla la strategia di movimento attraverso lo scan delle semplici lane
	 * in cui si trova il personaggio.
	 */
	private void applySimpleLane()
	{
		if(enemy.checkLeftLane()) 		
		{
			moveLeft();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveUp();
				else moveDown();
			}
			resetMovementsCD();
		}
		else if(enemy.checkRightLane()) 	
		{
			moveRight();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveUp();
				else moveDown();
			}
			resetMovementsCD();
		}
		else if(enemy.checkUpperLane()) 	
		{
			moveUp();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveLeft();
				else moveRight();
			}
			resetMovementsCD();
		}
		else if(enemy.checkBottomLane())	
		{
			moveDown();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveLeft();
				else moveRight();
			}
			resetMovementsCD();
		}
		else stayStill();
	}
	
	/**
	 * Controlla la strategia di movimento attraverso lo scan dei quadranti
	 * relativi alla posizione del giocatore.
	 */
	private void applyFourZones()
	{
		if(enemy.checkBottomLeft())
		{
			if(rng.nextBoolean()) moveLeft();
			else moveDown();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveRight();
				else moveUp();
			}	
			resetMovementsCD();
		}
		else if(enemy.checkBottomRight())
		{
			if(rng.nextBoolean()) moveRight();
			else moveDown();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveLeft();
				else moveDown();
			}	
			resetMovementsCD();
		}
		else if(enemy.checkUpperLeft())
		{
			if(rng.nextBoolean()) moveLeft();
			else moveUp();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveRight();
				else moveDown();
			}	
			resetMovementsCD();
		}
		else if(enemy.checkUpperRight())
		{
			if(rng.nextBoolean()) moveRight();
			else moveUp();
			if(enemy.wallsAhead() == 0)
			{
				if(rng.nextBoolean()) moveLeft();
				else moveDown();
			}	
			resetMovementsCD();
		}
	}
}
