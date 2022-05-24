package it.uniroma1.metodologie2018.javabomber.utils;

import com.badlogic.gdx.math.Vector2;

/**
 * Enumerazione che rappresenta le direzioni che un character generico
 * può prendere, compresa la "stance" di Idle, quando il personaggio è fermo.
 * @author Federico Scozzafava
 *
 */
public enum Direction
{
	UP(new Vector2(0, 1.0f)), 
	DOWN(new Vector2(0, -1.0f)), 
	LEFT(new Vector2(-1.0f ,0)), 
	RIGHT(new Vector2(1.0f ,0)), 
	IDLE(new Vector2(0,0));

	/**
	 * Direzione della costante enumerativa
	 */
	private Vector2 direction;
	
	/**
	 * Costruttore delle costanti enumerative.
	 * @param direction Direzione
	 */
	Direction(Vector2 direction)		{this.direction = direction;}

	/**
	 * Restituisce la direzione associata alla costante enumerativa
	 * @return	Direzione della costante enumerativa
	 */
	public Vector2 getDirVector()	{return direction;}
	
	
}