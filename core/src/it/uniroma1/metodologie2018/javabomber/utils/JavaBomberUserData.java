package it.uniroma1.metodologie2018.javabomber.utils;

import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;

/**
 * Gestisce lo userData di ogni fixture del gioco. Associata all'istanza dell'oggetto.
 * @author Lorenzo Ricci
 *
 */
public class JavaBomberUserData 
{
	
////////////////////CAMPI DELLA CLASSE//////////////////////
	
	/**
	 * Istanza dell'oggetto a cui è associata la userData.
	 */
	private GraphicEntity instance;
	
	/**
	 * Flag che identifica il body/fixture/joint come removibile.
	 */
	private boolean flaggedForRemove;
	
	
////////////////////////COSTRUTTORE//////////////////////////
	
	/**
	 * Costruisce un nuovo userData con un'istanza di una qualsiasi Graphic Entity.
	 * @param instance Istanza dell'oggetto da associare all'userData.
	 */
	public JavaBomberUserData(GraphicEntity instance)	{this.instance = instance;}
	
	
/////////////////METODI GETTER E SETTER///////////////////
	
	/**
	 * Restituisce informazioni sull'istanza associata.
	 * @return Istanza dell'oggetto associato alla userData.
	 */
	public GraphicEntity getInstance()	{return instance;}

	/**
	 * Verifica se l'oggetto è pronto per la rimozione.
	 * @return True : può essere rimosso | False : non può essere rimosso.
	 */
	public boolean isFlaggedForRemove()	{return flaggedForRemove;}
	
	/**
	 * Imposta l'oggetto come rimovibile.
	 */
	public void setAsRemovable()	{flaggedForRemove = true;}
}

