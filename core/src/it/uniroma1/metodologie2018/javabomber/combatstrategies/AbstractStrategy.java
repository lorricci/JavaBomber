package it.uniroma1.metodologie2018.javabomber.combatstrategies;

import java.util.Random;

import it.uniroma1.metodologie2018.javabomber.entities.Enemy;
import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;

/**
 * Costruisce tutte le informazioni fondamentali per ogni approccio di movimento utilizzato dai nemici.
 * @author Lorenzo Ricci
 *
 */
public abstract class AbstractStrategy implements BehaviorStrategy 
{
	
/////////////////////////CAMPI DELLA CLASSE///////////////////
	
	/**
	 * Giocatore a cui fare riferimento per l'approccio utilizzato.
	 */
	protected Player player;
	
	/**
	 * Nemico che userà l'approccio.
	 */
	protected Enemy enemy;
	
	/**
	 * Generatore di randomicità necessario al movimento.
	 */
	protected Random rng;

	/**
	 * Cooldown per i cambi di direzione del nemico.
	 */
	protected float movementsCD;
	
	/**
	 * Valore di reset per il movementsCD.
	 */
	protected float resetValue;
	
	
////////////////////////////COSTRUTTORE////////////////////////////
	
	/**
	 * Costruisce le informazioni fondamentali di ogni approccio di movimento.
	 * @param player Giocatore di riferimento.
	 * @param enemy Nemico che utilizza l'approccio.
	 * @param movementsCD Cooldown per i cambi di direzione del nemico.
	 * @param resetValue Valore di reset per il movementsCD.
	 */
	public AbstractStrategy(Player player, Enemy enemy, float movementsCD, float resetValue)
	{
		this.player = player;
		this.enemy = enemy;
		rng = new Random();
		this.movementsCD = movementsCD;
		this.resetValue = resetValue;
	}

	
/////////////////////////////METODI GETTER E SETTER//////////////////////////////
	
	/**
	 * Restituisce informazioni sul cooldown di movimento del nemico.
	 * @return Cooldown per il prossimo movimento del nemico.
	 */
	public float getMovementsCD()	{return movementsCD;}
	
	/**
	 * Riassegna al movementsCD il valore di reset.
	 */
	protected void resetMovementsCD()	{movementsCD = resetValue;}
	
	
////////////////METODI PER IL SETTING DEL MOVIMENTO DEL NEMICO/////////////////////

	/**
	 * Cambia la direzione del nemico per spostarlo a sinistra.
	 */
	protected void moveLeft()	{enemy.changeDirection(Direction.LEFT);}
	
	/**
	 * Cambia la direzione del nemico per spostarlo in alto.
	 */
	protected void moveUp()		{enemy.changeDirection(Direction.UP);}
	
	/**
	 * Cambia la direzione del nemico per spostarlo in basso.
	 */
	protected void moveDown()	{enemy.changeDirection(Direction.DOWN);}
	
	/**
	 * Cambia la direzione del nemico per spostarlo a destra.
	 */
	protected void moveRight()	{enemy.changeDirection(Direction.RIGHT);}
	
	/**
	 * Ferma il nemico.
	 */
	protected void stayStill()	{enemy.changeDirection(Direction.IDLE);}

}
