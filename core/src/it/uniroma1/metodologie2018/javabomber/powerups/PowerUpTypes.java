package it.uniroma1.metodologie2018.javabomber.powerups;

/**
 * Elenca i tipi di PowerUp presenti nel gioco.
 * Utile nella creazione dinamica di powerUp.
 * @author Lorenzo Ricci
 *
 */
public enum PowerUpTypes 
{
		END_PORTAL(true), LIFE_UP(true), SPEED_UP(true), SPEED_DOWN(false), TIMER_UP(true), 
		TIMER_DOWN(false), RANGE_UP(true), RANGE_DOWN(false), MORE_BOMBS(true), LESS_BOMBS(false), 
		RANDOM_POSITIVE(true), RANDOM_NEGATIVE(false), ARMOR_SET(true), PASS_BOMBS(true), 
		PUSH_BOMBS(true);

		/**
		 * Indica se il tipo di powerUp è positivo o negativo.
		 */
		private boolean benefit;
	
		/**
		 * Costruttore delle costanti enumerative.
		 * @param benefit True se positivo | False se negativo
		 */
		PowerUpTypes(boolean benefit)	{this.benefit = benefit;}
		
		/**
		 * Restituisce true se il powerUp è positivo, false altrimenti.
		 * @return True se positivo | False se negativo.
		 */
		public boolean givesBenefit()	{return benefit;}
}
