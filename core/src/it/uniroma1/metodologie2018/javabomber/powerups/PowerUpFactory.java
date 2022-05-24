package it.uniroma1.metodologie2018.javabomber.powerups;

import com.badlogic.gdx.math.Vector2;

import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;

/**
 * Factory utilizzata per lo spawning dinamico dei PowerUp una volta che viene distrutto un muro.
 * @author Lorenzo Ricci
 *
 */
public class PowerUpFactory 
{
	
/////////////////////CAMPI DELLA CLASSE////////////////////
	
	/**
	 * PowerUp da produrre.
	 */
	private static PowerUp toProduce;
	
	/**
	 * Controlla se il portale è già stato creato una volta.
	 */
	private static boolean portalSpawned;
	
	
/////////////////////METODO DI PRODUZIONE//////////////////////
	
	/**
	 * Produce un powerUp in base al tipo indicato.
	 * @param type Tipo di powerUp da generare.
	 * @param controller World legato ai powerup.
	 * @param spawnPos Posizione di spawn del powerUp.
	 * @return Un'istanza del powerUp.
	 */
	public static PowerUp producePowerUp(PowerUpTypes type, WorldController controller, Vector2 spawnPos)
	{
		switch(type)
		{
			case END_PORTAL :
			{
				toProduce = new EndPortal(controller, spawnPos);	
				portalSpawned = true;
				break;
			}
			case SPEED_UP	: toProduce = new SpeedUp(controller, spawnPos); 				break;
			case SPEED_DOWN	: toProduce = new SpeedDown(controller, spawnPos);			 	break;
			case LIFE_UP	: toProduce = new LifeUp(controller, spawnPos); 				break;
			case TIMER_UP	: toProduce = new TimerUp(controller, spawnPos); 				break;
			case TIMER_DOWN : toProduce = new TimerDown(controller, spawnPos); 				break;
			case RANGE_UP	: toProduce = new RangeUp(controller, spawnPos); 				break;
			case RANGE_DOWN	: toProduce = new RangeDown(controller, spawnPos); 				break;
			case MORE_BOMBS : toProduce = new MoreBombs(controller, spawnPos);				break;
			case LESS_BOMBS : toProduce = new LessBombs(controller, spawnPos);				break;
			case RANDOM_NEGATIVE : toProduce = new RandomNegative(controller, spawnPos); 	break;
			case RANDOM_POSITIVE: toProduce = new RandomPositive(controller, spawnPos); 	break;
			case ARMOR_SET : toProduce = new ArmorSet(controller, spawnPos); 				break;
			case PASS_BOMBS	: toProduce = new PassBombs(controller, spawnPos); 				break;
			case PUSH_BOMBS : toProduce = new PushBombs(controller, spawnPos); 				break;
		}
			return toProduce;
	}
	
	
///////////////////////////METODI GETTER E SETTER/////////////////////
	
	/**
	 * Indica se il portale è stato già spawnato o meno.
	 * @return True se il portale è già spawnato, false altrimenti.
	 */
	public static boolean hasPortalSpawned()	{return portalSpawned;}
}
