package it.uniroma1.metodologie2018.javabomber.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import it.uniroma1.metodologie2018.javabomber.entities.Bomb;
import it.uniroma1.metodologie2018.javabomber.entities.Player;
import it.uniroma1.metodologie2018.javabomber.map.FrozenTile;

/**
 * Contact Listener specifico del gioco. Imposta le azioni e reazioni di ogni tipo di collisione "complessa"
 * all'interno del mondo di gioco.
 * @author Lorenzo Ricci
 *
 */
public class JavaBomberContactListener implements ContactListener 
{
	/**
	 * UserData della fixture del primo actor.
	 */
	JavaBomberUserData actor1Data;
	
	/**
	 * UserData della fixture del secondo actor.
	 */
	JavaBomberUserData actor2Data; 
	
	@Override
	public void beginContact(Contact contact) 
	{
		actor1Data = (JavaBomberUserData) contact.getFixtureA().getUserData();
		actor2Data = (JavaBomberUserData) contact.getFixtureB().getUserData();
		
		actor1Data.getInstance().collide(actor2Data.getInstance());
	}
	
	@Override
	public void endContact(Contact contact) 
	{
		actor1Data = (JavaBomberUserData) contact.getFixtureA().getUserData();
		actor2Data = (JavaBomberUserData) contact.getFixtureB().getUserData();
		
		bombPlacedSetting(actor1Data, actor2Data);
		frozenSpeedBack(actor1Data, actor2Data);
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) 
	{
		actor1Data = (JavaBomberUserData) contact.getFixtureA().getUserData();
		actor2Data = (JavaBomberUserData) contact.getFixtureB().getUserData();
	
		if(solveInitialCollision(actor1Data, actor2Data)) contact.setEnabled(false);
	}
	
	/**
	 * Restituisce la normale velocità al giocatore quando si conclude il contatto con
	 * un tile ghiacciato dal boss.
	 * @param actor1 Primo actor della collisione.
	 * @param actor2 Secondo actor della collisione.
	 */
	private void frozenSpeedBack(JavaBomberUserData actor1, JavaBomberUserData actor2)
	{
		if(actor1.getInstance() instanceof FrozenTile && actor2.getInstance() instanceof Player)
		{
			Player pg = (Player) actor2.getInstance();
			pg.switchOnFrozenTile();
			pg.setSpeed(pg.getSpeed() * 2f);
		}
		else if(actor1.getInstance() instanceof Player && actor2.getInstance() instanceof FrozenTile)
		{
			Player pg = (Player) actor1.getInstance();
			pg.switchOnFrozenTile();
			pg.setSpeed(pg.getSpeed() * 2f);;
		}
	}
	
//METODI PER EVITARE CHE LA BOMBA COLLIDA NON APPENA PIAZZATA. LA BOMBA INIZIERA' A COLLIDERE CON UN PERSONAGGIO
//SOLO DOPO AVER CONCLUSO IL PRIMO CONTATTO CON LA STESSA, PER EVITARE SPINTE NON PREVISTE DEL PERSONAGGIO O DELLA BOMBA.
	
	/**
	 * Restituisce true se la bomba è appena piazzata e necessita di annullare la collisione, false altrimenti.
	 * @param actor1 Primo actor della collisione.
	 * @param actor2 Secondo actor della collisione.
	 * @return True se la collisione va annullata, false altrimenti.
	 */
	private boolean solveInitialCollision(JavaBomberUserData actor1, JavaBomberUserData actor2)
	{
		if(actor1.getInstance() instanceof Bomb && actor2.getInstance() instanceof Player)	return disableCollision(actor1);
		else if(actor1.getInstance() instanceof Player && actor2.getInstance() instanceof Bomb) return disableCollision(actor2);
		else return false;
	}
	
	/**
	 * Metodo di comodo per la leggibilità del metodo solveInitialCollision(). 
	 * Dice se la bomba è appena stata piazzata o meno.
	 * @param actor Userdata della bomba.
	 * @return True se la bomba è stata appena piazzata, false altrimenti.
	 */
	private boolean disableCollision(JavaBomberUserData actor)
	{
		Bomb bomb = (Bomb) actor.getInstance();
		return bomb.isJustPlaced();
	}
	
	/**
	 * Setta la bomba come piazzata alla fine del contatto. 
	 * In questo modo vengono riabilitate le collisioni fra bomba e characters dopo il piazzamento. 
	 * @param actor1 Primo actor della collisione.
	 * @param actor2 Secondo actor della collisione.
	 */
	private void bombPlacedSetting(JavaBomberUserData actor1, JavaBomberUserData actor2)
	{
		if(actor1.getInstance() instanceof Bomb && actor2.getInstance() instanceof Player)
		{
			Bomb bomb = (Bomb) actor1.getInstance();
			bomb.setAsPlacedYet();
		}
		else if(actor1.getInstance() instanceof Player && actor2.getInstance() instanceof Bomb)
		{
			Bomb bomb = (Bomb) actor2.getInstance();
			bomb.setAsPlacedYet();
		}
	}
	
	
	
	//METODI INUTILIZZATI
	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) 
	{}
}
