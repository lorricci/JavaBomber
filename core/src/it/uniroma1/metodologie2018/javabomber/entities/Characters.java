package it.uniroma1.metodologie2018.javabomber.entities;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_CHARS;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_PASSBOMBS;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Bullet;
import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Wind;
import it.uniroma1.metodologie2018.javabomber.explosions.Explosion;
import it.uniroma1.metodologie2018.javabomber.interfaces.Entity;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.Teleportable;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.map.BurningTile;
import it.uniroma1.metodologie2018.javabomber.utils.BodyDispenser;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;
import it.uniroma1.metodologie2018.javabomber.utils.JavaBomberUserData;
import it.uniroma1.metodologie2018.javabomber.utils.RelativeBombStats;


/**
 * Superclasse che struttura le caratteristiche fondamentali di ogni personaggio all'interno del gioco.
 * @author Lorenzo Ricci
 *
 */
public abstract class Characters implements GraphicEntity, Teleportable
{
	
/////////////////////////CAMPI DELLA CLASSE/////////////////////////////////
	
	/**
	 * Numero di vite del personaggio.
	 */
	protected int lives;
	
	/**
	 * Numero di colpi rimasti all'armatura.
	 */
	protected int armor;
	
	/**
	 * Indica l'ultima posizione designata per un teleport del personaggio.
	 */
	protected Vector2 teleportPos;
	
	/**
	 * Grandezza del personaggio.
	 */
	protected Vector2 size;
	
	/**
	 * Sprite del personaggio.
	 */
	protected Sprite sprite;
	
	/**
	 * Posizione del personaggio.
	 */
	protected Vector2 position;
	
	/**
	 * Controller del mondo in cui è inserito il personaggio.
	 */
	protected WorldController controller;
	
	/**
	 * Body del personaggio.
	 */
	protected Body body;
	
	/**
	 * Creatore del body del personaggio.
	 */
	protected BodyDispenser dispenser;
	
	/**
	 * Direzione attuale del personaggio.
	 * Inizializzata allo stato di Idle.
	 */
	protected Direction direction = Direction.IDLE;

	/**
	 * Indica se il personaggio può prendere danni.
	 */
	protected boolean canTakeDamage = true;
	
	/**
	 * Texture Atlas per la gestione delle animazioni.
	 */
	protected TextureAtlas textureAtlas;
	
	/**
	 * Indica la velocità del personaggio.
	 */
	protected float charSpeed = 1.0f;

	/**
	 * Indica le statische delle bombe associate al personaggio. 
	 */
	protected RelativeBombStats bombStats;
	
	/**
	 * Indica se il personaggio può spingere o meno le bombe.
	 * Sarà possibile solo attraverso il raccoglimento del PowerUp.
	 */
	protected boolean canPush;
	
	/**
	 * Indica se il personaggio è intenzionato a spingere una bomba davanti a sè.
	 * La spinta è possibile solo attraverso il raccoglimento del PowerUp.
	 */
	protected boolean pushing;
	
	/**
	 * Indica se il personaggio può passare attraverso le bombe.
	 */
	protected boolean canPass;

	
///////////////////////////COSTRUTTORE/////////////////////////////////////
	
	/**
	 * Costruttore di base di un qualsiasi personaggio all'interno del gioco.
	 * @param controller Controller del world all'interno del quale esiste il personaggio.
	 * @param position	Posizione del personaggio.
	 */
	public Characters(WorldController controller, Vector2 position)
	{
		//Setup dei dati.
		this.controller = controller;
		this.position = position;
		dispenser = new BodyDispenser(controller);
		bombStats = new RelativeBombStats();
	}
	
	
///////////////////////METODI ASTRATTI DELLA SUPERCLASSE/////////////////////////////
	
	/**
	 * Gestisce l'animazione di morte del personaggio.
	 */
	abstract protected void displayDeathAnimation();
	
	/**
	 * Gestisce l'animazione del personaggio allo scadere del tempo.
	 */
	abstract protected void displayTimeFinishedAnimation();
	
	/**
	 * Gestisce l'animazione della camminata. 
	 */
	abstract protected void displayWalkingAnimation();
	
	
///////////////////////METODI GETTER E SETTER//////////////////////////////////////
	
	/**
	 * Fornisce informazioni sulla grandezza del personaggio.
	 * @return Size del personaggio
	 */
	public Vector2 getSize()	{return size;}
	
	/**
	 * Restituisce informazioni sulla vita del personaggio.
	 * @return	Vita del personaggio.
	 */
	public int getHealth()	{return lives;}
	
	/**
	 * Restituisce la velocità attuale del personaggio.
	 * @return Velocità del personaggio.
	 */
	public float getSpeed()	{return charSpeed;}
	
	/**
	 * Cambia il coefficiente di velocità del personaggio.
	 * @param charSpeed Coefficiente di velocità del personaggio.
	 */
	public void setSpeed(float charSpeed) {this.charSpeed = charSpeed;}
	
	/**
	 * Restituisce la possibilità del personaggio di passare attraverso le bombe.
	 * Può essere attivata solo attraverso l'apposito PowerUp.
	 * @return True se può passare le bombe, false altrimenti.
	 */
	public boolean canPassBombs()	{return canPass;}
	
	/**
	 * Indica se il personaggio può spingere le bombe.
	 * @return True se può spingere le bombe, false altrimenti.
	 */
	public boolean canPushBombs()	{return canPush;}
	
	/**
	 * Rende possibile la spinta delle bombe al personaggio.
	 */
	public void enablePushBombs()	{canPush = true;}
	
	/**
	 * Restituisce le informazioni sulla bomba associata al personaggio.
	 * @return Stats della bomba del personaggio.
	 */
	public RelativeBombStats getBombStats()	{return bombStats;}
	
	/**
	 * Restituisce lo sprite del personaggio.
	 * @return Lo sprite del personaggio.
	 */
	public Sprite getSprite()	{return sprite;}
	
	/**
	 * Restituisce la direzione in cui si sta muovendo un personaggio.
	 * @return Direzione attuale del personaggio.
	 */
	public Direction getDirection()	{return direction;}
	
	@Override
	public Vector2 getPosition() {return position;}

	@Override
	public void setPosition(Vector2 position) 	{this.position = new Vector2(position);}
	
	@Override
	public Body getBody() {return body;}

	/**
	 * Restituisce il numero di colpi ancora sostenibili dall'armatura.
	 * @return Vita dell'armor.
	 */
	public int armorLeft()	{return armor;}

	/**
	 * Indica se il personaggio corrente indossa un armor.
	 * @return True se il personaggio indossa un armor, false altrimenti.
	 */
	public boolean hasArmor() {return armor > 0;}
	
	/**
	 * Controlla se il personaggio è morto e lo ferma. 
	 * @return True se il personaggio è morto, false altrimenti.
	 */
	protected boolean checkIfDead()
	{
		if(lives <= 0)
		{
			body.setLinearVelocity(0, 0);
			return true;
		}
		return false;
	}

	@Override
	public void applyTransforms()	{body.setTransform(teleportPos, body.getAngle());}
	
	/**
	 * Imposta la posizione in cui viene teletrasportato il corpo.
	 * @param teleportPos Nuova posizione in cui teletrasportare il corpo.
	 */
	public void setTransformPos(Vector2 teleportPos)
	{
		this.teleportPos = teleportPos;
		controller.getToTeleport().add(this);
	}
	
	/**
	 * Imposta la flag di rimozione dello userData del body e della fixture come true.
	 */
	private void setDataReady()
	{
		JavaBomberUserData bodyData = (JavaBomberUserData) body.getUserData();
		bodyData.setAsRemovable();
		body.getFixtureList().forEach(fix -> {
			JavaBomberUserData fixtureData = (JavaBomberUserData) fix.getUserData();
			fixtureData.setAsRemovable();
		});
	}

	
//////////////////////////METODI PER INCREMENTO E DECREMENTO DELLE STATS/////////////////////////

	
	/**
	 * Incrementa la vita del personaggio.
	 */
	public void increaseHealth()	{if(lives > 0) lives++;}
	
	/**
	 * Decrementa la vita del personaggio.
	 */
	public void decreaseHealth()		{if(lives > 0) lives--;}
	
	/**
	 * Aumenta la velocità del personaggio di un quarto.
	 */
	public void speedUp()	{if(charSpeed < 2f) charSpeed += 0.25f;}
	
	/**
	 * Diminuisce la velocità del personaggio di un quarto.
	 */
	public void speedDown()	{if(charSpeed > 0.50) charSpeed -= 0.25f;}
	
	/**
	 * Gestisce l'attivazione del powerUp dell'armatura.
	 */
	public void wearArmor()	{speedDown(); armor = 2;}
	
	/**
	 * Permette al personaggio di passare attraverso le bombe.
	 */
	public void enableBombPassing()	
	{
		Filter newFilter = new Filter();
		newFilter.categoryBits = CATEGORY_CHARS;
		newFilter.maskBits = MASK_PASSBOMBS;
		getBody().getFixtureList().get(0).setFilterData(newFilter);
		canPass = true;
	}
	
	
/////////////////////METODI PER GESTIONE DELLE COLLISIONI E DEI DAMAGE/////////////////////////
	
	/**
	 * Esprime il comportamento del personaggio quando viene colpito.
	 */
	public void getHit()
	{
		if(canTakeDamage)	
		{
			if(hasArmor())	hitArmor();
			else	decreaseHealth();
			canTakeDamage = false;
		}
		checkHitStatus();
	}

	/**
	 * Gestisce il tempo di invincibilità del personaggio dopo che è stato colpito.
	 * (Evita collisioni multiple con la stessa esplosione o "combo" indesiderate).
	 */
	private void checkHitStatus()
	{
		if(!canTakeDamage)
		{
			Timer.schedule(new Task() {
				
				@Override
				public void run() {canTakeDamage = true;}
				
			}, 1.0f);
			
		}
	}		
	
	/**
	 * Gestisce il colpo subito dal personaggio se indossa l'armatura.
	 */
	private void hitArmor()
	{
		armor--;
		if(armor == 0) speedUp();
	}
	
	@Override
	public void collide(GraphicEntity actor2)	
	{
		if(actor2 instanceof Explosion)	getHit();
		else if(actor2 instanceof Bullet)
		{
			Bullet bullet = (Bullet) actor2;
			if(!(bullet instanceof Wind)) bullet.hitTarget();
			getHit();
		}
		else if(actor2 instanceof BurningTile && this instanceof Player)	getHit();
	}
	
	@Override
	public void killEntity()
	{
		setDataReady();
		controller.getToRemove().add(this);
	}
	
	@Override
	public void disposeTexture()	{textureAtlas.dispose();}
	
	
////////////////METODI PER IL DETECTING DELLE BOMBE DIRETTAMENTE DAVANTI AD UN CHARACTER/////////////////////////
	
	/**
	 * Scannerizza tutte le bombe esistenti nel mondo per trovare se una di esse è di fronte a Bomberman.
	 */
	protected boolean bombIsAhead()
	{
		for(Bomb b : controller.getPresentBombs())				
			if(scanBombAhead(b)) 
			{
				if(!canPush) b.getBody().setLinearVelocity(0,0);
				else
				{
					if(pushing) pushBomb(b);
					else b.getBody().setLinearVelocity(0,0);
				}
				return true; 		
			}
		return false;
	}
	
	/**
	 * Scansiona l'area di fronte a Bomberman in base alla sua direzione e conferma
	 * l'esistenza di una bomba direttamente davanti a lui.
	 * @param ahead Bomba su cui effettuare il check della posizione.
	 * @return True se c'è una bomba subito davanti, false altrimenti.
	 */
	private boolean scanBombAhead(Bomb ahead)
	{
		boolean somethingAhead = false;
		Vector2 bombPos = ahead.getPosition();
		switch(direction)
		{
		case DOWN 	: somethingAhead = lookDownBomb(bombPos); 	break;
		case IDLE 	: somethingAhead = lookDownBomb(bombPos);	break;
		case LEFT 	: somethingAhead = lookLeftBomb(bombPos); 	break;
		case RIGHT	: somethingAhead = lookRightBomb(bombPos); 	break;
		case UP 	: somethingAhead = lookUpBomb(bombPos); 	break;
		}
		return somethingAhead;
	}
		
	/**
	 * Controlla se nell'area subito sotto il personaggio è presente una bomba.
	 * @param bombPos Posizione della bomba.
	 * @return True se c'è una bomba, false altrimenti.
	 */
	private boolean lookDownBomb(Vector2 bombPos)
	{
		if(xBombIsAligned(bombPos.x)) 
			if(bombPos.y <= position.y - 8f && bombPos.y >= position.y - 28f)
				return true;
		return false;
	}
		
	/**
	 * Controlla se nell'area subito sopra il personaggio è presente una bomba.
	 * @param bombPos Posizione della bomba.
	 * @return True se c'è una bomba, false altrimenti.
	 */
	private boolean lookUpBomb(Vector2 bombPos)
	{
		if(xBombIsAligned(bombPos.x))
			if(bombPos.y >= position.y + 8f && bombPos.y <= position.y + 28f)
					return true;
		return false;
	}
		
	/**
	 * Controlla se nell'area subito a sinistra del personaggio è presente una bomba.
	 * @param bombPos Posizione della bomba.
	 * @return True se c'è una bomba, false altrimenti.
	 */
	private boolean lookLeftBomb(Vector2 bombPos)
	{
		if(yBombIsAligned(bombPos.y)) 
			if(bombPos.x <= position.x - 8f && bombPos.x >= position.x - 28f)
				return true;
		return false;
	}
		
	/**
	 * Controlla se nell'area subito a destra del personaggio è presente una bomba.
	 * @param bombPos Posizione della bomba.
	 * @return True se c'è una bomba, false altrimenti.
	 */
	private boolean lookRightBomb(Vector2 bombPos)
	{
		if(yBombIsAligned(bombPos.y)) 
			if(bombPos.x >= position.x + 8f && bombPos.x <= position.x + 28f)
					return true;
			return false;
	}

	/**
	 * Controlla se il personaggio è allineato con la posizione della bomba data in input.
	 * @param xPos Posizione x della bomba.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean xBombIsAligned(float xPos)	{return xPos <= position.x + 16f  && xPos >= position.x - 16f;}
	
	/**
	 * Controlla se il personaggio è allineato con la posizione della bomba data in input.
	 * @param yPos Posizione y della bomba.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean yBombIsAligned(float yPos)	{return yPos <= position.y + 16f  && yPos >= position.y - 16f;}
	
	
////////////////////////////////METODI PER LA SPINTA DI UNA BOMBA//////////////////////////////////
	
	/**
	 * Spinge la bomba applicando un impulso lineare in base alla direzione del personaggio.
	 * @param bomb Bomba da spingere.
	 */
	private void pushBomb(Bomb bomb)
	{
		switch(direction)
		{
		case DOWN 	: bomb.getBody().applyLinearImpulse(new Vector2(0.0f, -2.5f / PPM), bomb.getBody().getWorldCenter(), true);	break;
		case IDLE 	: bomb.getBody().applyLinearImpulse(new Vector2(0.0f, 0.0f), bomb.getBody().getWorldCenter(), true);	break;
		case LEFT 	: bomb.getBody().applyLinearImpulse(new Vector2(-2.5f / PPM, 0.0f), bomb.getBody().getWorldCenter(), true);	break;
		case RIGHT 	: bomb.getBody().applyLinearImpulse(new Vector2(2.5f / PPM, 0.0f), bomb.getBody().getWorldCenter(), true);	break;
		case UP		: bomb.getBody().applyLinearImpulse(new Vector2(0.0f, 2.5f / PPM), bomb.getBody().getWorldCenter(), true);	break;
		}
	}

	
////////////////////////METODI PER IL DETECTING DI ALTRI CHARACTERS DAVANTI AL PERSONAGGIO/////////////////

	/**
	 * Controlla se il personaggio si trova di fronte ad un altro pg del gioco.
	 * @return True se c'è un personaggio di fronte, false altrimenti.
	 */
	protected boolean charIsAhead()
	{
		boolean bombAhead = false;
		switch(direction)
		{
		case UP 	:	bombAhead = lookCharUp(); 		break;
		case DOWN	:	bombAhead = lookCharDown(); 	break;
		case IDLE	:	bombAhead = lookCharDown();		break;
		case LEFT	:	bombAhead = lookCharLeft();		break;
		case RIGHT	:	bombAhead = lookCharRight();	break;
		}
		return bombAhead;
	}
	
	/**
	 * Controlla se sopra al personaggio è presente un altro pg di gioco.
	 * @return True se c'è un pg, false altrimenti.
	 */
	private boolean lookCharUp()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof Characters)
			{
				if(xCharIsAligned(ent))
					if(ent.getPosition().y <= position.y + 25 && ent.getPosition().y >= position.y + 16f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se sotto al personaggio è presente un altro pg di gioco.
	 * @return True se c'è un pg, false altrimenti.
	 */
	private boolean lookCharDown()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof Characters)
			{
				if(xCharIsAligned(ent))
					if(ent.getPosition().y >= position.y - 25 && ent.getPosition().y <= position.y - 16f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se a sinistra del personaggio è presente un altro pg di gioco.
	 * @return True se c'è un pg, false altrimenti.
	 */
	private boolean lookCharLeft()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof Characters)
			{
				if(yCharIsAligned(ent))
					if(ent.getPosition().x >= position.x - 25 && ent.getPosition().x <= position.x - 16f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se a destra del personaggio è presente un altro pg di gioco.
	 * @return True se c'è un pg, false altrimenti.
	 */
	private boolean lookCharRight()
	{
		for(Entity ent : controller.getEntities())
			if(ent instanceof Characters)
			{
				if(yCharIsAligned(ent))
					if(ent.getPosition().x <= position.x + 25 && ent.getPosition().x >= position.x + 16f)
						return true;
			}
		return false;
	}
	
	/**
	 * Controlla se il personaggio è allineato ad una entità sull'asse Y.
	 * @param ent Entità di gioco.
	 * @return True se è allineata, false altrimenti.
	 */
	private boolean xCharIsAligned(Entity ent)	{return ent.getPosition().x >= position.x - 16f && ent.getPosition().x <= position.x + 16f;}
	
	/**
	 * Controlla se il personaggio è allineato ad una entità sull'asse X.
	 * @param ent Entità di gioco.
	 * @return True se è allineata, false altrimenti.
	 */
	private boolean yCharIsAligned(Entity ent)	{return ent.getPosition().y >= position.y - 16f && ent.getPosition().y <= position.y + 16f;}
}
