package it.uniroma1.metodologie2018.javabomber.map;

import com.badlogic.gdx.math.Vector2;

import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Bullet;
import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Wind;
import it.uniroma1.metodologie2018.javabomber.explosions.Explosion;
import it.uniroma1.metodologie2018.javabomber.explosions.Thunder;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;

/**
 * Rappresentazione astratta di un muro di gioco distruttibile. Viene utilizzata
 * un'abstract class per avere flessibilità nel tipo di muro (Si può ad esempio avere ad
 * esempio muri più o meno resistenti o con determinate proprietà).
 * @author Lorenzo Ricci
 */
public abstract class Wall extends Tile
{
	
//////////////////////////////CAMPI DELLA CLASSE/////////////////////////
	
	/**
	 * Numero di colpi sostenibili dal muro.
	 */
	protected int hitPoints;

/////////////////////////////COSTRUTTORE/////////////////////////////////////////
	
	/**
	 * Costruttore di ogni muro distruttibile all'interno del gioco.
	 * @param position	Posizione del muro.
	 * @param controller	Controller gestore del mondo di gioco.
	 */
	public Wall(Vector2 position, WorldController controller)	{super(position, controller);}
	

//////////////////////////METODI ASTRATTI DELLA SUPERCLASSE////////////////////////
	
	/**
	 * Gestice la reazione di un muro al subire un colpo.
	 */
	public abstract void getHit();
	
	
////////////////////////METODI GETTER E SETTER/////////////////////////////////
	
	/**
	 * Restituisce il numero di Hit Points rimanenti.
	 * @return	Numero di colpi sostenibili dal muro attualmente.
	 */
	public int getHitPoints()	{return hitPoints;}
	
	/**
	 * Imposta un nuovo valore di hit points.
	 * @param hitPoints Nuovo numero di colpi sostenibili.
	 */
	public void setHitPoints(int hitPoints)	{this.hitPoints = hitPoints;}
	
	
//////////////////////////METODI DI INCREMENTO E DECREMENTO DELLE STATS//////////////////
	
	/**
	 * Ripara il muro incrementando i suoi hitpoints.
	 */
	public void repairWall()	{hitPoints++;}
	
	
//////////////////////////METODI DI GESTIONE DELLE COLLISIONI/////////////////////////
	
	@Override
	public void collide(GraphicEntity actor2)	
	{
		if(actor2 instanceof Explosion)	
			{if(!(actor2 instanceof Thunder)) getHit();}
		else if(actor2 instanceof Bullet)
		{
			Bullet bullet = (Bullet) actor2;
			if(!(actor2 instanceof Wind)) bullet.hitTarget();
			getHit();
		}
	}
	
}
