package it.uniroma1.metodologie2018.javabomber.utils;

/**
 * Identifica tutte le costanti utilizzate nel gioco. 
 * Le costanti sono statiche, permettendone l'accesso senza dover istanziare la classe Constants.
 * @author Lorenzo Ricci
 *
 */
public final class BodyRelatedConstants 
{
	/**
	 * Pixel Per Meters, rappresenta lo scaling utilizzato dal gioco.
	 */
	public static final float PPM = 100f;

	/**
	 * Larghezza della schermata di gioco.
	 */
	public static final float WIDTH = 1366;
	
	/**
	 * Altezza della schermata di gioco.
	 */
	public static final float HEIGHT = 768;
	
	/**
	 * Category bit per i personaggi.
	 */
	public static final short CATEGORY_CHARS = 1;
	
	/**
	 * Category bit per le bombe.
	 */
	public static final short CATEGORY_BOMB = 2;
	
	/**
	 * Category bit per i muri.
	 */
	public static final short CATEGORY_WALLS = 4;
	
	/**
	 * Category bit per le esplosioni.
	 */
	public static final short CATEGORY_EXPLOSION = 8;
	
	/**
	 * Category bit per il pavimento.
	 */
	public static final short CATEGORY_FLOOR = 16;
	
	/**
	 * Category bit per i powerUp.
	 */
	public static final short CATEGORY_POWERUP = 32;
	
	/**
	 * Maschera di collisione per i personaggi.
	 */
	public static final short MASK_CHARS = CATEGORY_CHARS | CATEGORY_BOMB | CATEGORY_WALLS | CATEGORY_EXPLOSION | CATEGORY_POWERUP | CATEGORY_FLOOR;
	
	/**
	 * Maschera di collisione per i nemici.
	 */
	public static final short MASK_ENEMY = CATEGORY_CHARS | CATEGORY_BOMB | CATEGORY_WALLS | CATEGORY_EXPLOSION;
	
	/**
	 * Maschera di collisione per i personaggi con il powerUp del passaggio delle bombe.
	 */
	public static final short MASK_PASSBOMBS = CATEGORY_CHARS | CATEGORY_WALLS | CATEGORY_EXPLOSION | CATEGORY_POWERUP | CATEGORY_FLOOR;
	
	/**
	 * Maschera di collisione per le bombe.
	 */
	public static final short MASK_BOMB = CATEGORY_CHARS | CATEGORY_WALLS | CATEGORY_FLOOR;
	
	/**
	 * Maschera di collisione per le esplosioni.
	 */
	public static final short MASK_EXPLOSION = CATEGORY_CHARS | CATEGORY_WALLS;
	
	/**
	 * Maschera di collisione per i muri.
	 */
	public static final short MASK_WALLS = CATEGORY_CHARS | CATEGORY_EXPLOSION | CATEGORY_BOMB;
	
	/**
	 * Maschera di collisione per i powerUp.
	 */
	public static final short MASK_POWERUP = CATEGORY_CHARS;
	
	/**
	 * Maschera di collisione per il pavimento.
	 */
	public static final short MASK_FLOOR = CATEGORY_BOMB | CATEGORY_CHARS;
}
