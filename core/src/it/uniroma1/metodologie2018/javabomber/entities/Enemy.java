package it.uniroma1.metodologie2018.javabomber.entities;

import com.badlogic.gdx.math.Vector2;

import it.uniroma1.metodologie2018.javabomber.combatstrategies.AbstractStrategy;
import it.uniroma1.metodologie2018.javabomber.explosions.FireExplosion;
import it.uniroma1.metodologie2018.javabomber.interfaces.Entity;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.map.BorderWall;
import it.uniroma1.metodologie2018.javabomber.map.Wall;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;

/**
 * Costruisce la struttura di ogni nemico del gioco. Fornisce metodi di scansione dell'area intorno al 
 * nemico, oltre ad implementare ogni metodo della superclasse Characters.
 * @author Lorenzo Ricci
 *
 */
public abstract class Enemy extends Characters 
{
	
//////////////////////////CAMPI DELLA CLASSE/////////////////////////////
	
	/**
	 * Giocatore di cui il nemico ha informazioni.
	 */
	protected Player player;
	
	/**
	 * Schema di movimento del nemico.
	 */
	protected AbstractStrategy behavior;
	
	/**
	 * Indica la grandezza dell'area di checking del nemico.
	 * (Essendo centrata nel nemico, il valore va settato alla metà
	 * dell'area voluta. i.e. : se l'area voluta è di 64f intorno al nemico, la zoneCheck va
	 * settata a 32f).
	 */
	protected float zoneCheck;
	
	/**
	 * Distanza di sicurezza (espressa in blocchi) adottata dal nemico.
	 */
	protected float safeDistance;	
	
	/**
	 * Timer per il cambio di comportamento del nemico.
	 */
	protected float behaviorTimes;
	
///////////////////////////COSTRUTTORE///////////////////////////////////
	
	/**
	 * Costruttore di ogni nemico del gioco.
	 * @param controller World legato alla simulazione fisica del gioco.
	 * @param position Posizione in cui spawnare il nemico.
	 * @param player Istanza del giocatore. (Serve a regolare l'IA del nemico).
	 */
	public Enemy(WorldController controller, Vector2 position, Player player) 
	{
		super(controller, position);
		this.player = player;
	}

	
////////////////////////////METODI GETTER E SETTER//////////////////////////////
	
	/**
	 * Cambia direzione al nemico.
	 * @param direction Nuova direzione da utilizzare.
	 */
	public void changeDirection(Direction direction)	{this.direction = direction;}
	
	
///////////////////////////METODI PER IL CHECK DELLA ZONA DI MINACCIA E PERICOLO/////////////////////////////
	
	/**
	 * Controlla se il giocatore si trova entro la zona di minaccia del nemico.
	 * @return True se il giocatore è in zona, false altrimenti.
	 */
	protected boolean playerInAttackRange()
	{
		Vector2 playerPos = player.getPosition();
		return isInVerticalZone(playerPos.y, zoneCheck) && isInHorizontalZone(playerPos.x, zoneCheck);
	}

	/**
	 * Controlla se il giocatore si trova entro la zona di sicurezza del nemico.
	 * @return True se il giocatore è "troppo" vicino, false altrimenti.
	 */
	protected boolean isInDanger()
	{
		Vector2 playerPos = player.getPosition();
		return isInVerticalZone(playerPos.x, safeDistance) && isInHorizontalZone(playerPos.x, safeDistance);
	}
	
	/**
	 * Controlla la sezione verticale della zona di minaccia del nemico.
	 * @param yPos Posizione Y del giocatore.
	 * @return True se il giocatore è nella zona verticale, false altrimenti.
	 */
	private boolean isInVerticalZone(float yPos, float distCheck)
	{
		return yPos <= position.y + distCheck + size.y / 2f && yPos >= position.y - distCheck - size.y / 2f;
	}
	
	/**
	 * Controlla la sezione orizzontale della zona di minaccia del nemico.
	 * @param xPos Posizione X del giocatore.
	 * @return True se il giocatore è nella zona orizzontale, false altrimenti.
	 */
	private boolean isInHorizontalZone(float xPos, float distCheck)
	{
		return xPos <= position.x + distCheck + size.y / 2f && xPos >= position.x - distCheck - size.y / 2f;
	}
	
	
//////////////////METODI PER IL CHECKING DI UN'ESPLOSIONE DI FRONTE AL NEMICO/////////////////

	/**
	 * Controlla se davanti al nemico è presente una esplosione.
	 * @return True se c'è un'esplosione, false altrimenti.
	 */
	protected boolean explosionAhead()
	{
		boolean explAhead = false;
		switch(direction)
		{
		case DOWN	:	explAhead = explosionDown();	break;
		case IDLE	:	explAhead = explosionDown();	break;
		case LEFT	:	explAhead = explosionLeft();	break;
		case RIGHT	:	explAhead = explosionRight();	break;
		case UP		:	explAhead = explosionUp();		break;
		}
		return explAhead;
	}
	
	/**
	 * Controlla se subito sopra il nemico è presente una esplosione lasciata dalla bomba di un giocatore.
	 * @return True se c'è un esplosione, false altrimenti.
	 */
	private boolean explosionUp()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof FireExplosion)
			{
				if(xExplAligned(ent))
					if(ent.getPosition().y >= position.y + 16f && ent.getPosition().y <= position.y + 48f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se subito sotto il nemico è presente una esplosione lasciata dalla bomba di un giocatore.
	 * @return True se c'è un esplosione, false altrimenti.
	 */
	private boolean explosionDown()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof FireExplosion)
			{
				if(xExplAligned(ent))
					if(ent.getPosition().y <= position.y - 16f && ent.getPosition().y >= position.y - 48f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se subito a sinistra del nemico è presente una esplosione lasciata dalla bomba di un giocatore.
	 * @return True se c'è un esplosione, false altrimenti.
	 */
	private boolean explosionLeft()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof FireExplosion)
			{
				if(yExplAligned(ent))
					if(ent.getPosition().x <= position.x - 16f && ent.getPosition().x >= position.x - 48f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se subito a destra del nemico è presente una esplosione lasciata dalla bomba di un giocatore.
	 * @return True se c'è un esplosione, false altrimenti.
	 */
	private boolean explosionRight()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof FireExplosion)
			{
				if(yExplAligned(ent))
					if(ent.getPosition().x >= position.x + 16f && ent.getPosition().x <= position.x + 48f)
						return true;
			}
		return false;
	}

	/**
	 * Controlla se l'entità in input è allineata con il nemico sull'asse Y.
	 * @param ent Entità da controllare.
	 * @return True se allineata, false altrimenti.
	 */
	private boolean yExplAligned(Entity ent) {return ent.getPosition().y <= position.y + 16f && ent.getPosition().y >= position.y - 16f;}
	
	/**
	 * Controlla se l'entità in input è allineata con il nemico sull'asse X.
	 * @param ent Entità da controllare.
	 * @return True se allineata, false altrimenti.
	 */
	private boolean xExplAligned(Entity ent) {return ent.getPosition().x <= position.x + 16f && ent.getPosition().x >= position.x - 16f;}
	
	
/////////////////////////////METODI PER IL DETECTING DI MURI DI FRONTE AL NEMICO/////////////////////////
	
	/**
	 * Restituisce un intero corrispondente al tipo di muro di fronte al nemico.
	 * @return 0 : BorderWall di fronte al nemico | 1 : Muro distruttibile di fronte al nemico | meno 1 : nessun muro di fronte al nemico.
	 */
	public int wallsAhead()
	{
		int wallType = -1;
		switch(direction)
		{
		case UP		:	wallType = wallsUp();		break;
		case DOWN	:	wallType = wallsDown();		break;
		case IDLE	:	wallType = wallsDown();		break;
		case LEFT	:	wallType = wallsLeft();		break;
		case RIGHT	:	wallType = wallsRight();	break;
		}
		return wallType;
	}
	
	/**
	 * Controlla se sopra il nemico è presente un muro.
	 * @return True se c'è un muro, false altrimenti.
	 */
	private int wallsUp()
	{
		for(GraphicEntity bl : controller.getMapRender())
		{
			if(blockXCentered(bl))
				if(bl.getPosition().y >= position.y + 16f && bl.getPosition().y <= position.y + 48f)
				{
					if(bl instanceof BorderWall) return 0;
					else if(bl instanceof Wall) return 1;
				}
		}
		return -1;
	}
	
	/**
	 * Controlla se sotto il nemico è presente un muro.
	 * @return True se c'è un muro, false altrimenti.
	 */
	private int wallsDown()
	{
		for(GraphicEntity bl : controller.getMapRender())
		{
			if(blockXCentered(bl))
				if(bl.getPosition().y <= position.y - 16f && bl.getPosition().y >= position.y - 48f)
				{
					if(bl instanceof BorderWall) return 0;
					else if(bl instanceof Wall) return 1;
				}
		}
		return -1;
	}
	
	/**
	 * Controlla se a sinistra del nemico è presente un muro.
	 * @return True se c'è un muro, false altrimenti.
	 */
	private int wallsLeft()
	{
		for(GraphicEntity bl : controller.getMapRender())
		{
			if(blockYCentered(bl))
				if(bl.getPosition().x <= position.x - 16f && bl.getPosition().x >= position.x - 48f)
				{
					if(bl instanceof BorderWall) return 0;
					else if(bl instanceof Wall) return 1;
				}
		}
		return -1;
	}
	
	/**
	 * Controlla se a destra del nemico è presente un muro.
	 * @return True se c'è un muro, false altrimenti.
	 */
	private int wallsRight()
	{
		for(GraphicEntity bl : controller.getMapRender())
		{
			if(blockYCentered(bl))
				if(bl.getPosition().x >= position.x + 16f && bl.getPosition().x <= position.x + 48f)
				{
					if(bl instanceof BorderWall) return 0;
					else if(bl instanceof Wall) return 1;
				}
		}
		return -1;
	}
	
	/**
	 * Controlla se la posizione del muro in input è allineata con il nemico sull'asse Y.
	 * @param bl Muro in input.
	 * @return True se allineata, false altrimenti.
	 */
	private boolean blockYCentered(GraphicEntity bl)
		{return bl.getPosition().y <= position.y + 12f && bl.getPosition().y >= position.y - 12f;}
	
	/**
	 * Controlla se la posizione del muro in input è allineata con il nemico sull'asse Y.
	 * @param bl Muro in input.
	 * @return True se allineata, false altrimenti.
	 */
	private boolean blockXCentered(GraphicEntity bl)
		{return bl.getPosition().x <= position.x + 12f && bl.getPosition().x >= position.x - 12f;}
	
	
/////////////////////////METODI PER IL CHECKING DELLA STESSA LANE////////////////////////////////
	
	/**
	* Controlla se il giocatore è a sinistra del nemico, sulla stessa lane Y.
	* @return True se il giocatore è a sinistra, false altrimenti.
	*/
	public boolean checkLeftLane()
		{return yIsCentered() && player.getPosition().x < position.x;}
	
	/**
	* Controlla se il giocatore è a destra del nemico, sulla stessa lane Y.
	* @return True se il giocatore è a destra, false altrimenti.
	*/
	public boolean checkRightLane()
		{return yIsCentered() && player.getPosition().x > position.x;}
	
	/**
	* Controlla se il giocatore è al di sopra del nemico, sulla stessa lane X.
	* @return True se il giocatore è sopra, false altrimenti.
	*/
	public boolean checkUpperLane()
		{return xIsCentered() && player.getPosition().y > position.y;}
	
	/**
	* Controlla se il giocatore è al di sotto del nemico, sulla stessa lane X.
	* @return True se il giocatore è sotto, false altrimenti.
	*/
	public boolean checkBottomLane()
		{return xIsCentered() && player.getPosition().y < position.y;}

	/**
	 * Controlla se il nemico è allineato con il giocatore sull'asse x.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean xIsCentered()
		{return position.x <= player.getPosition().x + 16f && position.x >= player.getPosition().x - 16f;}
	
	/**
	 * Controlla se il nemico è allineato con il giocatore sull'asse y.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean yIsCentered()
		{return position.y <= player.getPosition().y + 16f && position.y >= player.getPosition().y - 16f;}

	
//////////////////////////METODI PER IL CHECKING DEI QUADRANTI RELATIVI ALLA POSIZIONE DEL PLAYER//////////////////////
	
	/**
	* Controlla se il giocatore si trova nel "quadrante" in basso a sinistra rispetto al nemico.
	* @return True se il giocatore nel quadrante in basso a sinistra, false altrimenti.
	*/
	public boolean checkBottomLeft()
		{return player.getPosition().x < position.x - 16f && player.getPosition().y < position.y - 16;}
	
	/**
	* Controlla se il giocatore si trova nel "quadrante" in basso a destra rispetto al nemico.
	* @return True se il giocatore nel quadrante in basso a destra, false altrimenti.
	*/
	public boolean checkBottomRight()
		{return player.getPosition().x > position.x + 16 && player.getPosition().y < position.y - 16f;}
	
	/**
	* Controlla se il giocatore si trova nel "quadrante" in alto a sinistra rispetto al nemico.
	* @return True se il giocatore nel quadrante in alto a sinistra, false altrimenti.
	*/
	public boolean checkUpperLeft()
		{return player.getPosition().x < position.x - 16f && player.getPosition().y > position.y + 16f;}
	
	/**
	* Controlla se il giocatore si trova nel "quadrante" in alto a destra rispetto al nemico.
	* @return True se il giocatore nel quadrante in alto a destras, false altrimenti.
	*/
	public boolean checkUpperRight()
		{return player.getPosition().x > position.x + 16f && player.getPosition().y > position.y + 16f;}
}
