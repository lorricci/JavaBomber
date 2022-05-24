package it.uniroma1.metodologie2018.javabomber.entities;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Wind;
import it.uniroma1.metodologie2018.javabomber.combatstrategies.ApproachThreat;
import it.uniroma1.metodologie2018.javabomber.combatstrategies.AvoidThreat;
import it.uniroma1.metodologie2018.javabomber.explosions.Thunder;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.map.BurningTile;
import it.uniroma1.metodologie2018.javabomber.map.Floor;
import it.uniroma1.metodologie2018.javabomber.map.FrozenTile;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;
import static it.uniroma1.metodologie2018.javabomber.utils.WizardTextures.*;
/**
 * Rappresenta il boss finale del gioco, un mago capace di controllare gli elementi del
 * Vento, Elettricità, Fuoco e Ghiaccio. Cambia dinamicamente il suo playstyle in base
 * alla sua stance e all'elemento scelto nel tempo.
 * @author Lorenzo Ricci
 *
 */
public class WizardOfTheElements extends Enemy
{

/////////////////////////////CAMPI DELLA CLASSE////////////////////////////////////////////
	/**
	 * Frames di animazioni della camminata.
	 */
	private int walkFrame;
	
	/**
	 * Frames di animazioni della morte.
	 */
	private int deathFrame;
	
	/**
	 * Frame per il casting del vento.
	 */
	private int windCastFrame;
	
	/**
	 * Frame per il casting di zone di fuoco o ghiaccio.
	 */
	private int baseCastFrame;
	
	/**
	 * Frame per il casting dell'attacco di tuono.
	 */
	private int thunderCastFrame;
	
	/**
	 * Accumulatore per il delay di animazione tra un frame e l'altro.
	 */
	private float animFrameDelay;
	
	/**
	 * Timer di countdown per il prossimo casting della tempesta.
	 */
	private float thunStrikeCd = 25.0f;
	
	/**
	 * Indica la stance attuale del boss fra le due possibili.
	 * True : stance del fuoco | False : stance del ghiaccio.
	 */
	private boolean fireStance;
	
	/**
	 * Timer che gestisce il cambio di stance tra fuoco e ghiaccio.
	 */
	private float timeStanceChange = 30f;
	
	/**
	 * Indica se il mago sta utilizzando un attacco di zona di fuoco o ghiacchio.
	 */
	private boolean baseCasting;
	
	/**
	 * Indica se il mago sta utilizzando una folata di vento.
	 */
	private boolean windCasting;
	
	/*
	 * Indica se il mago sta utilizzando il potente attacco di tuono.
	 */
	private boolean thunderCasting;

	/**
	 * Indica se la tempesta di tuono è attualmente in corso.
	 */
	private boolean thunderStrikes;
	
	/**
	 * Posizione iniziale della tempesta che va verso il basso.
	 */
	private float downThunStart = 304;
	
	/**
	 * Posizione iniziale della tempesta che va verso l'alto.
	 */
	private float upThunStart = -336;
	
	/**
	 * Posizione iniziale della temepsta che va verso destra.
	 */
	private float rightThunStart = -528;
	
	/**
	 * Posizione iniziale della tempesta che va verso sinistra.
	 */
	private float leftThunStart = 560;
	
	/**
	 * Accumulatore di delay tra un'ondata di tuono e l'altra.
	 */
	private float thunStrikeDelay;
	
	/**
	 * Delay tra una lane e l'altra colpite dalla tempesta.
	 */
	private float thunStrikeTimes = 0.50f;
	
	/**
	 * Direzione della tempesta.
	 */
	private Direction thunderCastDirection;
	
	/**
	 * Range dell'attacco di vento del boss. 
	 */
	private float atkRange = 96f;
	
	/**
	 * Tempo di cooldown del casting della zona di ghiaccio/fuoco.
	 */
	private float baseCastCooldown = 10f;
	
	/**
	 * Tempo di cooldown per la folata di vento.
	 */
	private float windGustCooldown = 2f;
	
	/**
	 * Indica se il mago può attaccare con la folata di vento.
	 */
	private boolean canWind = true;
	
	/**
	 * Indica se il mago può posizionare le zone infiammate/ghiacciate.
	 */
	private boolean canBaseCast = true;
	
	
/////////////////////////////COSTRUTTORE///////////////////////////////////////

	/**
	 * Costruisce il mago impostando le informazioni del boss.
	 * @param controller World del gioco.
	 * @param position Posizione del boss.
	 * @param player Giocatore a cui fare riferimento.
	 */
	public WizardOfTheElements(WorldController controller, Vector2 position, Player player) 
	{
		//Setup dei dati.
		super(controller, position, player);
		this.lives = 7;
		size = new Vector2(16f, 16f);
		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/Enemies/Wizard Of The Elements/sprites.txt"));
		//Setup dei dati relativi all'ia.
		zoneCheck = 160f;
		safeDistance = 96f;
		behaviorTimes = 2.5f;
		behavior = new ApproachThreat(player, this, 0.28f, 0.28f);
		//Assegnazione randomica della stance.
		Random random = new Random();
		fireStance = random.nextBoolean();
		//Setup del body.
		body = dispenser.dispenseCharacterBody(position, size, this);
		//Setup dello sprite.
		if(fireStance) sprite = new Sprite(textureAtlas.findRegion(FIREIDLE));
		else sprite = new Sprite(textureAtlas.findRegion(ICEIDLE));
		sprite.setSize(sprite.getWidth() / PPM * 1.5f, sprite.getHeight() / PPM * 2f);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}

	
////////////////////////METODI GETTER E SETTER////////////////////////////
	
	/**
	 * Resetta il timer e cambia la stance del mago.
	 */
	private void setStanceChange()
	{
		fireStance = !fireStance;
		timeStanceChange = 50f;
	}
	
	
////////////////////////METODI PER IL DISEGNO DELLO SPRITE///////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 5f);}


	@Override
	public void update(float deltaTime)
	{
		animFrameDelay += deltaTime;
		if(thunStrikeCd <= 0) thunderCasting = true;
		//Controlla se il boss è morto.
		if(checkIfDead())	displayDeathAnimation();
		//Controlla se il tempo è finito per chiamare lo stop del boss.
		else if(controller.getHUD().getGameTimer().isFinished()) displayTimeFinishedAnimation();
		//Gestisce l'animazione in base al casting di incantesimi.
		else if(windCasting) windCastAnimation(deltaTime);
		else if(baseCasting) baseCastAnimation();
		else if(thunderCasting) thunderCastAnimation();
		else if(timeStanceChange <= 0) setStanceChange();
		//Gestice l'animazione in base al movimento.
		else
		{
			manageBaseCooldowns(deltaTime);
			//Posizionamento
			if(bombIsAhead() || charIsAhead()) body.setLinearVelocity(0,0);
			else body.setLinearVelocity(direction.getDirVector().x * Gdx.graphics.getDeltaTime() * PPM * charSpeed, direction.getDirVector().y * Gdx.graphics.getDeltaTime() * PPM * charSpeed);
			setPosition(new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM));
			//Cambio di approccio e metodi di attacco.
			if(playerInAttackRange()) castZoneAttack();
			if(couldHit() && !(behavior instanceof AvoidThreat)) windAttack();
			manageBehaviorChanges();
			behavior.applyRules(deltaTime);
			if(wallsAhead() == 1) windAttack();
			
			displayWalkingAnimation();
			handleSprite(body, sprite);
		}
		//Gestore dell'attacco di tuono.
		if(thunderStrikes) 
		{
			thunStrikeDelay += deltaTime;
			castThunders();
			thunStrikeCd = 25f;
		}
	}

	@Override
	protected void displayDeathAnimation() 
	{
		float deathTimeFrames = 0.20f;
		while(animFrameDelay >= deathTimeFrames)
		{
			animFrameDelay -= deathTimeFrames;
			switch(deathFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(DEATH00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(DEATH01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(DEATH02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(DEATH03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(DEATH04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(DEATH05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(DEATH06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(DEATH07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(DEATH08)); break;
			case 9	:	sprite.setRegion(textureAtlas.findRegion(DEATH09)); break;
			default	:	killEntity();
			}
			deathFrame++;
		}
	}

	@Override
	protected void displayTimeFinishedAnimation() {body.setLinearVelocity(0,0);}

	@Override
	protected void displayWalkingAnimation() 
	{
		switch(direction)
		{
		case UP :	
		{
			if(fireStance) fireWalkUp();
			else iceWalkUp();
		}
			break;
		case DOWN :	
		{
			if(fireStance) fireWalkDown();
			else iceWalkDown();
		}
			break;
		case LEFT :	
		{
			if(fireStance) fireWalkLeft();
			else iceWalkLeft();
		}
			break;
		case RIGHT :	
		{
			if(fireStance) fireWalkRight();
			else iceWalkRight();
		}
			break;
		default	:	
		{
			if(fireStance) sprite.setRegion(textureAtlas.findRegion(FIREIDLE));
			else sprite.setRegion(textureAtlas.findRegion(ICEIDLE));
		}
		}
	}

	/**
	 * Gestisce l'animazione della camminata verso l'alto durante la stance di fuoco.
	 */
	private void fireWalkUp()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_BACK00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_BACK02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(FIREWALK_BACK00));
			}
			}
			walkFrame++;
		}
	}

	/**
	 * Gestisce l'animazione della camminata verso il basso durante la stance di fuoco.
	 */
	private void fireWalkDown()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_FRONT02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(FIREWALK_FRONT00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso sinistra durante la stance di fuoco.
	 */
	private void fireWalkLeft()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_LEFT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_LEFT02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(FIREWALK_LEFT00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso destra durante la stance di fuoco.
	 */
	private void fireWalkRight()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_RIGHT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_RIGHT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIREWALK_RIGHT02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(FIREWALK_RIGHT00));
			}
			}
			walkFrame++;
		}
	}
	

	/**
	 * Gestisce l'animazione della camminata verso il basso durante la stance di ghiaccio.
	 */
	private void iceWalkDown()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_FRONT02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(ICEWALK_FRONT00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso l'alto durante la stance di ghiaccio.
	 */
	private void iceWalkUp()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_BACK00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_BACK02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(ICEWALK_BACK00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso sinistra durante la stance di ghiacchio.
	 */
	private void iceWalkLeft()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_LEFT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_LEFT02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(ICEWALK_LEFT00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso destra durante la stance di ghiaccio.
	 */
	private void iceWalkRight()
	{
		float timeFrames = 0.15f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_RIGHT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_RIGHT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICEWALK_RIGHT02)); break;
			default	:
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(ICEWALK_RIGHT00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione per il casting della folata di vento.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void windCastAnimation(float deltaTime)
	{
		body.setLinearVelocity(0,0);
		switch(direction)
		{
		case DOWN	:	windCastDown();		break;
		case IDLE	:	windCastDown();		break;
		case LEFT	:	windCastLeft();		break;
		case RIGHT	:	windCastRight();	break;
		case UP		:	windCastUp();		break;	
		}
	}
	
	/**
	 * Gestisce l'animazione per il casting della folata di vento verso il basso.
	 */
	private void windCastDown()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(windCastFrame)
			{
			case 0 	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_FRONT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_FRONT01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_FRONT02));	break;
			default	:
			{
				castWindGust();
				windCasting = false;
				windCastFrame = 0;
			}
			}			
			windCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione per il casting della folata di vento verso l'alto.
	 */
	private void windCastUp()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(windCastFrame)
			{
			case 0 	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_BACK00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_BACK01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_BACK02)); break;
			default	:
			{
				castWindGust();
				windCasting = false;
				windCastFrame = 0;
			}
			}
			windCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione per il casting della folata di vento verso sinistra.
	 */
	private void windCastLeft()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(windCastFrame)
			{
			case 0 	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_LEFT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_LEFT01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_LEFT02));	break;
			default	:
			{
				castWindGust();
				windCasting = false;
				windCastFrame = 0;
			}
			}			
			windCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione per il casting della folata di vento verso destra.
	 */
	private void windCastRight()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(windCastFrame)
			{
			case 0 	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_RIGHT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_RIGHT01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(WINDCAST_RIGHT02));	break;
			default	:
			{
				castWindGust();
				windCasting = false;
				windCastFrame = 0;
			}
			}			
			windCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione del lancio dell'incatesimo di zone
	 * infuocate o ghiacciate.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void baseCastAnimation()
	{
		body.setLinearVelocity(0,0);
		switch(direction)
		{
		case DOWN:	
		{
			if(fireStance) fireTilesDown();
			else iceTilesDown();
		}
		break;
		
		case LEFT:	
		{
			if(fireStance) fireTilesLeft();
			else iceTilesLeft();
		}
		break;
		
		case RIGHT:
		{
			if(fireStance) fireTilesRight();
			else iceTilesRight();
		}
		break;
		
		case UP:	
		{
			if(fireStance) fireTilesUp();
			else iceTilesUp();
		}
		break;
		
		default:
		{
			if(fireStance) fireTilesDown();
			else iceTilesDown();
		}
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti infuocati verso il basso.
	 */
	private void fireTilesDown()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_FRONT08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti infuocati verso l'alto.
	 */
	private void fireTilesUp()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_BACK08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti infuocati verso sinistra.
	 */
	private void fireTilesLeft()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_LEFT08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti infuocati verso destra.
	 */
	private void fireTilesRight()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(FIRECAST_RIGHT08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti ghiacciati verso il basso.
	 */
	private void iceTilesDown()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti ghiacciati verso l'alto.
	 */
	private void iceTilesUp()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_BACK08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti ghiacciati verso sinistra.
	 */
	private void iceTilesLeft()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_LEFT08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce il casting dei pavimenti ghiacciati verso destra.
	 */
	private void iceTilesRight()
	{
		float castFrames = 0.30f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(baseCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT05)); break;
			case 6	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT06)); break;
			case 7	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT07)); break;
			case 8	:	sprite.setRegion(textureAtlas.findRegion(ICECAST_FRONT08)); break;
			default :
			{
				castZone();
				baseCasting = false;
				baseCastFrame = 0;
			}
			}
			baseCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione per il casting dell'attacco di tempesta del mago.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void thunderCastAnimation()
	{
		body.setLinearVelocity(0,0);
		switch(direction)
		{
		case DOWN	:	thunderCastDown();	break;
		case LEFT	:	thunderCastLeft();	break;
		case RIGHT	:	thunderCastRight();	break;
		case UP		:	thunderCastUp();	break;
		default		:	thunderCastDown();
		}
	}
	
	/**
	 * Gestisce l'animazione dell'attacco di tuono del mago verso il basso.
	 */
	private void thunderCastDown()
	{
		float castFrames = 0.40f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(thunderCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_FRONT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_FRONT01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_FRONT02));	break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_FRONT03));	break;
			default :
			{
				thunderCasting = false;
				thunderStrikes = true;
				thunderCastFrame = 0;
				thunderCastDirection = direction;
			}
			}
			thunderCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'attacco di tuono del mago verso l'alto.
	 */
	private void thunderCastUp()
	{
		float castFrames = 0.40f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(thunderCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_BACK00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_BACK01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_BACK02));	break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_BACK03));	break;
			default :
			{
				thunderCasting = false;
				thunderStrikes = true;
				thunderCastFrame = 0;
				thunderCastDirection = direction;
			}
			}
			thunderCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'attacco di tuono del mago verso sinistra.
	 */
	private void thunderCastLeft()
	{
		float castFrames = 0.40f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(thunderCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_LEFT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_LEFT01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_LEFT02));	break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_LEFT03));	break;
			default :
			{
				thunderCasting = false;
				thunderStrikes = true;
				thunderCastFrame = 0;
				thunderCastDirection = direction;
			}
			}
			thunderCastFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione dell'attacco di tuono del mago verso destra.
	 */
	private void thunderCastRight()
	{
		float castFrames = 0.40f;
		while(animFrameDelay >= castFrames)
		{
			animFrameDelay -= castFrames;
			switch(thunderCastFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_RIGHT00));	break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_RIGHT01));	break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_RIGHT02));	break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(THUNDERCAST_RIGHT03));	break;
			default :
			{
				thunderCasting = false;
				thunderStrikes = true;
				thunderCastFrame = 0;
				thunderCastDirection = direction;
			}
			}
			thunderCastFrame++;
		}
	}
	
///////////////////////////////////METODI PER GLI ATTACCHI DEL BOSS/////////////////////////////////
	
	/**
	 * Lancia una folata di vento in base alla direzione attuale del nemico.
	 */
	private void castWindGust()
	{
		switch(direction)
		{
		case UP		: controller.getToAdd().add(new Wind(new Vector2(position.x, position.y + 28f), controller, Direction.UP));		break;
		case DOWN	: controller.getToAdd().add(new Wind(new Vector2(position.x, position.y - 28f), controller, Direction.DOWN));	break;
		case LEFT	: controller.getToAdd().add(new Wind(new Vector2(position.x - 28f, position.y), controller, Direction.LEFT));	break;
		case RIGHT	: controller.getToAdd().add(new Wind(new Vector2(position.x + 28, position.y), controller,  Direction.RIGHT));	break;
		case IDLE	: controller.getToAdd().add(new Wind(new Vector2(position.x, position.y - 28f), controller, Direction.IDLE));	break;
		}
	}
	
	/**
	 * Lancia la folata di vento.
	 */
	private void windAttack()
	{
		if(canWind) 
		{
			windCasting = true;
			canWind = false;
		}
	}
	
	/**
	 * Lancia l'incantesimo di zone ghiacciate/infuocate.
	 */
	private void castZoneAttack()
	{
		if(canBaseCast)
		{
			if(checkUpperLane() || checkBottomLane() || checkLeftLane() || checkRightLane())	decideLanesCast();
			else if(checkUpperLeft() || checkUpperRight() || checkBottomRight() || checkBottomLeft())	decideSectorsCast();
			canBaseCast = false;
		}
	}
	
	/**
	 * Gestisce il casting della zona in base alla posizione del giocatore sulla stessa lane del mago.
	 */
	private void decideLanesCast()
	{
		if(checkUpperLane())
		{
			direction = Direction.UP;
			baseCasting = true;
		}
		else if(checkBottomLane())
		{
			direction = Direction.DOWN;
			baseCasting = true;
		}
		else if(checkLeftLane())
		{
			direction = Direction.LEFT;
			baseCasting = true;
		}
		else if(checkRightLane())
		{
			direction = Direction.RIGHT;
			baseCasting = true;
		}
	}
	
	/**
	 * Gestisce il casting della zona in base alla posizione del giocatore nei quattro quadranti relativi alla posizione
	 * del mago.
	 */
	private void decideSectorsCast()
	{
		if(checkUpperLeft())
		{
			if(Math.abs(player.getPosition().y - position.y) < Math.abs(player.getPosition().x - position.x))
			{
				direction = Direction.UP;
				baseCasting = true;
			}
			else
			{
				direction = Direction.LEFT;
				baseCasting = true;
			}
		}
		else if(checkUpperRight())
		{
			if(Math.abs(player.getPosition().y - position.y) < Math.abs(player.getPosition().x - position.x))
			{
				direction = Direction.UP;
				baseCasting = true;
			}
			else
			{
				direction = Direction.RIGHT;
				baseCasting = true;
			}
		}
		else if(checkBottomLeft())
		{
			if(Math.abs(player.getPosition().y - position.y) < Math.abs(player.getPosition().x - position.x))
			{
				direction = Direction.DOWN;
				baseCasting = true;
			}
			else
			{
				direction = Direction.LEFT;
				baseCasting = true;
			}
		}
		if(checkBottomRight())
		{
			if(Math.abs(player.getPosition().y - position.y) < Math.abs(player.getPosition().x - position.x))
			{
				direction = Direction.DOWN;
				baseCasting = true;
			}
			else
			{
				direction = Direction.RIGHT;
				baseCasting = true;
			}
		}
		
	}
	
	/**
	 * Effettua l'incantesimo di zona sui tre tile di fronte a sè, e sul tile su cui è posizionato.
	 * L'orientamento cambia in base alla direzione.
	 */
	private void castZone()
	{
		switch(direction)
		{
		case DOWN	:	castDownZone();		break;
		case IDLE	:	castDownZone();		break;
		case LEFT	:	castLeftZone();		break;
		case RIGHT	:	castRightZone();	break;
		case UP		:	castUpZone();		break;
		}
	}
	
	/**
	 * Gestisce il casting della zona quando il mago è rivolto verso l'alto.
	 */
	private void castUpZone()
	{
		for(GraphicEntity block : controller.getMapRender())
		{
			if(block instanceof Floor)
			{
				Floor floor = (Floor) block;
				Vector2 blPos = floor.getPosition();
				if(xFloorAligned(blPos.x))
					if(blPos.y >= position.y - 16f && blPos.y <= position.y + 112f)	
						if(!floor.hasWall()) putFloor(blPos);
			}
		}
	}
	
	/**
	 * Gestisce il casting della zona quando il mago è rivolto verso il basso.
	 */
	private void castDownZone()
	{
		for(GraphicEntity block : controller.getMapRender())
		{
			if(block instanceof Floor)
			{
				Floor floor = (Floor) block;
				Vector2 blPos = floor.getPosition();
				if(xFloorAligned(blPos.x))
					if(blPos.y <= position.y + 16f && blPos.y >= position.y - 112f)
						if(!floor.hasWall()) putFloor(blPos);
			}
		}
	}
	
	/**
	 * Gestisce il casting della zona quando il mago è rivolto verso sinistra.
	 */
	private void castLeftZone()
	{
		for(GraphicEntity block : controller.getMapRender())
		{
			if(block instanceof Floor)
			{
				Floor floor = (Floor) block;
				Vector2 blPos = floor.getPosition();
				if(yFloorAligned(blPos.y))
					if(blPos.x <= position.x + 16f && blPos.x >= position.x - 112f)
						if(!floor.hasWall()) putFloor(blPos);
			}
		}
	}
	
	/**
	 * Gestisce il casting della zona quando il mago è rivolto verso destra.
	 */
	private void castRightZone()
	{
		for(GraphicEntity block : controller.getMapRender())
		{
			if(block instanceof Floor)
			{
				Floor floor = (Floor) block;
				Vector2 blPos = floor.getPosition();
				if(yFloorAligned(blPos.y))
					if(blPos.x >= position.x - 16f && blPos.x <= position.x + 112f)
						if(!floor.hasWall()) putFloor(blPos);
			}
		}
	}
	
	/**
	 * Controlla se la posizione del pavimento è allineata rispetto all'asse x.
	 * @param xPos Posizione x del pavimento.
	 * @return True se è allineata, false altrimenti.
	 */
	private boolean xFloorAligned(float xPos)	{return xPos >= position.x - 16f && xPos <= position.x + 16f;}
	
	/**
	 * Controlla se la posizione del pavimento è allineata rispetto all'asse y.
	 * @param yPos Posizione y del pavimento.
	 * @return True se è allineata, false altrimenti.
	 */
	private boolean yFloorAligned(float yPos)	{return yPos >= position.y - 16f && yPos <= position.y + 16f;}
	
	/**
	 * Posiziona il nuovo tipo di pavimentazione creata dal mago nella posizione indicata.
	 * @param blPos Posizione del pavimento.
	 */
	private void putFloor(Vector2 blPos)
	{
		if(fireStance) controller.getToAdd().add(new BurningTile(blPos, controller));
		else controller.getToAdd().add(new FrozenTile(blPos, controller));	
	}
	
	/**
	 * Lancia l'incantesimo dei fulmini utilizzato dal mago.
	 */
	private void castThunders()
	{
		switch(thunderCastDirection)
		{
		case DOWN 	: 	castThundersDown(); 	break;
		case IDLE 	: 	castThundersDown(); 	break;
		case LEFT 	:	castThundersLeft(); 	break;
		case RIGHT 	:	castThundersRight();	break;
		case UP		:	castThundersUp();		break;
		}
	}
	
	/**
	 * Gestisce lo spawn dei fulmini partendo dall'alto verso il basso.
	 */
	private void castThundersDown()
	{
		while(thunStrikeDelay >= thunStrikeTimes)
		{
			thunStrikeDelay -= thunStrikeTimes;
			if(downThunStart > -337) 
			{
				for(GraphicEntity block : controller.getMapRender())
					if(block instanceof Floor)
					{
						Floor fl = (Floor) block;
						if(!fl.hasWall() && fl.getPosition().y == downThunStart)
							controller.getToAdd().add(new Thunder(fl.getPosition(), controller));
					}
				downThunStart -= 64;
			}
			else
			{
				thunderStrikes = false;
				downThunStart = 304;
			}
		}
	}
	
	/**
	 * Gestisce lo spawn dei fulmini partendo dal basso verso l'alto.
	 */
	private void castThundersUp()
	{
		while(thunStrikeDelay >= thunStrikeTimes)
		{
			thunStrikeDelay -= thunStrikeTimes;
			if(upThunStart < 305) 
			{
				for(GraphicEntity block : controller.getMapRender())
					if(block instanceof Floor)
					{
						Floor fl = (Floor) block;
						if(!fl.hasWall() && fl.getPosition().y == upThunStart)
							controller.getToAdd().add(new Thunder(fl.getPosition(), controller));
					}
				upThunStart += 64;
			}
			else
			{
				thunderStrikes = false;
				upThunStart = -336;
			}
		}
	}
	
	/**
	 * Gestisce lo spawn dei fulmini partendo da destra verso sinistra.
	 */
	private void castThundersLeft()
	{
		while(thunStrikeDelay >= thunStrikeTimes)
		{
			thunStrikeDelay -= thunStrikeTimes;
			if(leftThunStart > -529) 
			{
				for(GraphicEntity block : controller.getMapRender())
					if(block instanceof Floor)
					{
						Floor fl = (Floor) block;
						if(!fl.hasWall() && fl.getPosition().x == leftThunStart)
							controller.getToAdd().add(new Thunder(fl.getPosition(), controller));
					}
				leftThunStart -= 64;
			}
			else
			{
				thunderStrikes = false;
				leftThunStart = 560;
			}
		}
	}
	
	/**
	 * Gestisce lo spawn dei fulmini partendo da destra verso sinistra.
	 */
	private void castThundersRight()
	{
		while(thunStrikeDelay >= thunStrikeTimes)
		{
			thunStrikeDelay -= thunStrikeTimes;
			if(rightThunStart < 561) 
			{
				for(GraphicEntity block : controller.getMapRender())
					if(block instanceof Floor)
					{
						Floor fl = (Floor) block;
						if(!fl.hasWall() && fl.getPosition().x == rightThunStart)
							controller.getToAdd().add(new Thunder(fl.getPosition(), controller));
					}
				rightThunStart += 64;
			}
			else
			{
				thunderStrikes = false;
				rightThunStart = -528;
			}
		}
	}
	

	/**
	 * Controlla se il nemico potrebbe colpire il bersaglio, controllando il range dell'attacco.
	 * @return True se potrebbe colpire il giocatore, false altrimenti.
	 */
	private boolean couldHit()
	{
		boolean castWind = false;
		switch(direction)
		{
		case UP		:	castWind = tryAttackUp(); 		break;
		case DOWN	:	castWind = tryAttackDown();		break;
		case IDLE	:	castWind = tryAttackDown();		break;
		case LEFT	:	castWind = tryAttackLeft();		break;
		case RIGHT	:	castWind = tryAttackRight();	break;
		}
		return castWind;
	}

	/**
	 * Controlla se il giocatore è a portata del colpo nella zona sopra stante.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackUp()
	{
		Vector2 targetPos = player.getPosition();
		
		if(xPlayerAligned())
			if(targetPos.y >= position.y + 8f && targetPos.y <= position.y + 8f + atkRange) return true;
		return false;
	}
	
	/**
	 * Controlla se il giocatore è a portata del colpo nella zona sotto stante.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackDown()
	{
		Vector2 targetPos = player.getPosition();
		
		if(xPlayerAligned())
			if(targetPos.y <= position.y - 8f && targetPos.y >= position.y - 8f - atkRange) return true;
		return false;
	}
	
	/**
	 * Controlla se il giocatore è a portata del colpo nella zona a sinistra.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackLeft()
	{
		Vector2 targetPos = player.getPosition();
		
		if(yPlayerAligned())
			if(targetPos.x >= position.x - 8f - atkRange && targetPos.x <= position.x - 8) return true;
		return false;
	}
	
	/**
	 * Controlla se il giocatore è a portata del colpo nella zona a destra.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackRight()
	{
		Vector2 targetPos = player.getPosition();
		
		if(yPlayerAligned())
			if(targetPos.x <= position.x + 8f + atkRange && targetPos.x >= position.x - 8) return true;
		return false;
	}
	
	/**
	 * Controlla se il nemico è allineato al giocatore sull'asse delle y.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean yPlayerAligned()	{return player.getPosition().y <= position.y + 8f && player.getPosition().y >= position.y -8f;}
	
	/**
	 * Controlla se il nemico è allineato al giocatore sull'asse delle x.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean xPlayerAligned()	{return player.getPosition().x <= position.x + 8f && player.getPosition().x >= position.x - 8f;}
	
	/**
	 * Gestisce i cooldown del mago.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void manageBaseCooldowns(float deltaTime)
	{
		thunStrikeCd -= deltaTime;
		timeStanceChange -= deltaTime;
		if(!canWind) windGustCooldown -= deltaTime;
		if(!canBaseCast) baseCastCooldown -= deltaTime;
		handleCooldownReset();
	}
	/**
	 * Resetta i cooldown del mago.
	 */
	private void handleCooldownReset()
	{
		if(windGustCooldown <= 0) 
		{
			canWind = true;
			windGustCooldown = 2f;
		}
		if(baseCastCooldown <= 0) 
		{
			canBaseCast = true;
			baseCastCooldown = 10f;
		}
	}
	/**
	 * Gestisce i cambi di strategia e comportamento del nemico.
	 */
	private void manageBehaviorChanges()
	{
		if(explosionAhead() || bombIsAhead())
		{
			behavior = new AvoidThreat(player, this, behavior.getMovementsCD(), 0.28f);
			behaviorTimes = 2.5f;
		}
		if(behaviorTimes <= 0)
		{
			if((isInDanger())) behavior = new AvoidThreat(player, this, behavior.getMovementsCD(), 0.28f);
			else behavior = new ApproachThreat(player, this, behavior.getMovementsCD(), 0.28f);
			behaviorTimes = 2.5f;
		}
	}
}