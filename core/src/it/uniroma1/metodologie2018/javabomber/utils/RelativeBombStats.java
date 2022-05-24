package it.uniroma1.metodologie2018.javabomber.utils;

import it.uniroma1.metodologie2018.javabomber.entities.Bomb;

/**
 * Rappresenta le informazioni relative alla bomba posseduta da un singolo personaggio.
 * 
 * @author Lorenzo Ricci
 *
 */
public class RelativeBombStats 
{

//////////////////////CAMPI DELLA CLASSE////////////////////
	
	/**
	 * Range della bomba.
	 */
	private float range;
	
	/**
	 * Danni inflitti dalla bomba.
	 */
	private int damage;	
	
	/**
	 * Numero di bombe ancora piazzabili dal personaggio.
	 */
	private int availBombs;
	
	/**
	 * Numero massimo di bombe piazzabili dal personaggio.
	 */
	private int maxBombs;
	
///////////////////////////////COSTRUTTORE//////////////////////////////
	
	/**
	 * Costruttore delle informazioni della bomba.
	 * Inizializza il range a 32f (ovvero un blocco di larghezza) ed i danni ad 1.
	 */
	public RelativeBombStats()
	{
		range = 32f;
		damage = 1;
		availBombs = 1;
		maxBombs = 1;
	}
	
	
/////////////////////////METODI GETTER E SETTER//////////////////////
	
	/**
	 * Imposta i dati della bomba in input secondo i dati correnti del personaggio.
	 * @param bomb Bomba a cui impostare i valori.
	 */
	public void setBombInfos(Bomb bomb)
	{
		bomb.setDamage(damage);
		bomb.setRange(range);
	}
	
	/**
	 * Indica se ci sono bombe piazzabili a disposizione.
	 * @return True se il personaggio può piazzare bombe, false altrimenti.
	 */
	public boolean canPlace()	{return availBombs > 0;}

	/**
	 * Restituisce il range delle bombe piazzabili dal personaggio.
	 * @return Range della bomba.
	 */
	public float getRange() {return range;}

	/**
	 * Restituisce il numero massimo di bombe piazzabili dal personaggio.
	 * @return Numero massimo di bombe.
	 */
	public int getMaxBombs() {return maxBombs;}
	
	
///////////////////////METODI PER L'INCREMENTO E DECREMENTO DELLE STATS///////////////////////////
	
	/**
	 * Aumenta il range della bomba di una quantità data.
	 * @param addRange Quantità di range da aggiungere alla bomba.
	 */
	public void boostRange(float addRange) {range += addRange;}
	
	/**
	 * Diminuisce il range della bomba di una quantità data, con un cap di 32f.
	 * @param subRange Quantità di range da sottrarre alla bomba.
	 */
	public void decreaseRange(float subRange)	
	{
		range -= subRange;
		if(range < 32.0f)	range = 32.0f;
	}
	
	/**
	 * Aumenta i danni inflitti dalla bomba.
	 * @param addDamage Quantità di danni da aggiungere alla bomba.
	 */
	public void boostDamage(int addDamage)  {damage += addDamage;}
	
	/**
	 * Diminuisce i danni inflitti dalla bomba, con un cap di 1.
	 * @param subDamage Quantità di danni da sottrarre alla bomba.
	 */
	public void decreaseDamage(int subDamage) 
	{
		damage -= subDamage;
		if(damage < 1) damage = 1;
	}
	/**
	 * Incrementa il numero di bombe ancora a disposizione.
	 */
	public void addPlacing()	{if(availBombs < maxBombs) availBombs++;}
	
	/**
	 * Decrementa il numero di bombe ancora a disposizione.
	 */
	public void subPlacing()	{if(availBombs >= 0) availBombs--;}
	
	/**
	 * Aumenta il numero massimo di bombe piazzabili dal personaggio.
	 */
	public void maxUp()	{maxBombs++;}
	
	/**
	 * Diminuisce il numero massimo di bombe piazzabili dal personaggio
	 * con un cap minimo di 1 bomba alla volta.
	 */
	public void maxDown()	{if(maxBombs > 1) maxBombs--;}
	
}
