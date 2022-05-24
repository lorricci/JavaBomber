package it.uniroma1.metodologie2018.javabomber.entities;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.BowBoarAnimationFrames.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.bulletlikebehavior.Arrow;
import it.uniroma1.metodologie2018.javabomber.combatstrategies.ApproachThreat;
import it.uniroma1.metodologie2018.javabomber.combatstrategies.AvoidThreat;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;

/**
 * Un tipo di nemico base del gioco. Più debole di Bomberman, utilizza
 * un arco per attaccare a distanza in una singola direzione.
 * @author Lorenzo Ricci
 *
 */
public class BowBoar extends Enemy
{	
/////////////////////CAMPI DELLA CLASSE/////////////////////////////
	
	/**
	 * Frame di animazione della camminata.
	 */
	private int walkFrame;
	
	/**
	 * Frame di animazione della morte.
	 */
	private int deathFrame;
	
	/**
	 * Frame di animazione della mira.
	 */
	private int aimingFrame;
	
	/**
	 * Accumulatore per il delay di animazione tra un frame e l'altro.
	 */
	private float animFrameDelay;
	
	/**
	 * Indica la stance attualmente in uso dal nemico.
	 * STANCE DI MIRA: velocità dimezzata, può sparare un massimo di due frecce.
	 * STANCE DI CAMMINATA: velocità normale, ma non può sparare frecce.
	 */
	private boolean aimingStance;
	
	/**
	 * Numero di frecce rimaste al nemico.
	 */
	private int arrowsLeft = 2;
	
	/**
	 * Range dell'attacco con l'arco.
	 */
	private float atkRange = 112f; 
	
	/**
	 * Cooldown dell'attacco con l'arco.
	 */
	private float atkCooldown = 2f;
	
	/**
	 * Cooldown per lo switch della stance del nemico.
	 */
	private float stanceChangeCD = 2.5f;
	
	/**
	 * Indica se il nemico può cambiare stance.
	 */
	private boolean canSwapStance = true;
	
	/**
	 * Indica se il nemico può tirare con l'arco.
	 */
	private boolean canShoot = true;

	
///////////////////////////////////COSTRUTTORE//////////////////////////////////
	
	/**
	 * Costruisce un'istanza del nemico.
	 * @param controller Worldcontroller utilizzato dal gioco.
	 * @param position Posizione del nemico.
	 * @param player Istanza del giocatore.
	 */
	public BowBoar(WorldController controller, Vector2 position, Player player) 
	{
		//Setup dei dati.
		super(controller, position, player);
		this.lives = 2;
		size = new Vector2(16f, 16f);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/Enemies/Bow Boar/spritesheet.txt"));
		charSpeed = 0.90f;
		//Setup dei dati relativi all'Ia.
		zoneCheck = 144f;
		safeDistance = 80f;
		behaviorTimes = 2.5f;
		behavior = new ApproachThreat(player, this, 0.3f, 0.3f);
		//Setup del body.
		body = dispenser.dispenseCharacterBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion("Front_00"));
		sprite.setSize(sprite.getWidth() / PPM * 1.2f, sprite.getHeight() / PPM * 1.3f);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}

	
//////////////////////////METODI GETTER E SETTER///////////////////////////////////////
	
	/**
	 * Cambia la stance del nemico passando a quella di mira.
	 */
	private void startAiming()
	{
		if(!aimingStance)
		{
			if(charSpeed > 0.50f) charSpeed -= 0.30f;
			aimingStance = true;
		}
	}

	/**
	 * Cambia la stance del nemico passando a quella di camminata.
	 */
	private void stopAiming()
	{
		if(aimingStance)
		{
			if(charSpeed < 2.0f) charSpeed += 0.30f;
			aimingStance = false;
			canSwapStance = false;
			arrowsLeft = 2;
		}
	}
	
	
///////////////////////////METODI PER IL DISEGNO DELLO SPRITE//////////////////////////////////////
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	@Override
	public void handleSprite(Body body, Sprite sprite) 
	{sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2f, body.getPosition().y - sprite.getHeight() / 2f);}

	@Override
	public void update(float deltaTime) 
	{
		//Gestione dei timer e cooldown.
		manageCooldowns(deltaTime);
		//Controlla se il nemico è morto.
		if(checkIfDead()) displayDeathAnimation();
		//Controlla se il tempo è finito per chiamare lo stop del nemico.
		else if(controller.getHUD().getGameTimer().isFinished()) displayTimeFinishedAnimation();
		//Gestisce le animazioni del movimento e di attacco.
		else
		{
			//Gestione velocità.
			if(bombIsAhead() || charIsAhead()) body.setLinearVelocity(0,0);
			else body.setLinearVelocity(direction.getDirVector().x * Gdx.graphics.getDeltaTime() * PPM * charSpeed, direction.getDirVector().y * Gdx.graphics.getDeltaTime() * PPM * charSpeed);
			setPosition(new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM));			
			manageBehaviorChanges();
			behavior.applyRules(deltaTime);
			if(playerInAttackRange() && !(behavior instanceof AvoidThreat)) 
				{if(canSwapStance) startAiming();}
			else stopAiming();
			if(wallsAhead() == 1)
			{
				if(canSwapStance) startAiming();
				if(canShoot) shootArrow();
			}
			//Gestione attacco.
			if(aimingStance) 
			{
				if(couldHit()) if(canShoot) shootArrow();			
				if(arrowsLeft < 0) stopAiming();
			}
			manageAnimations();
		 }
	}
	
	/**
	 * Gestisce le animazioni del personaggio.
	 */
	private void manageAnimations()
	{
		if(aimingStance) displayBowAnimation();
		else displayWalkingAnimation();
		handleSprite(body, sprite);
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
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBDEATH_00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBDEATH_01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBDEATH_02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBDEATH_03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(BBDEATH_04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(BBDEATH_05)); break;
			default :	killEntity();
			}
			deathFrame++;
		}
	}
	

	@Override
	protected void displayWalkingAnimation() 
	{
		switch(direction)
		{
		case UP 	:	walkUpAnimation();		break;
		case DOWN	:	walkDownAnimation();	break;
		case LEFT	:	walkLeftAnimation();	break;
		case RIGHT	:	walkRightAnimation();	break;
		default		:	sprite.setRegion(textureAtlas.findRegion(BBIDLE));
		}
	}

	/**
	 * Gestisce l'animazione della camminata in basso.
	 */
	private void walkDownAnimation()
	{
		float timeFrames = 0.10f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			walkFrame++;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT05)); break;
			default :
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALK_FRONT00));
			}
			}
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso l'alto.
	 */
	private void walkUpAnimation()
	{
		float timeFrames = 0.10f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			walkFrame++;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK05)); break;
			default :
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALK_BACK00));
			}
			}
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso l'alto.
	 */
	private void walkLeftAnimation()
	{
		float timeFrames = 0.10f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			walkFrame++;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT05)); break;
			default :
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALK_LEFT00));
			}
			}
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso l'alto.
	 */
	private void walkRightAnimation()
	{
		float timeFrames = 0.10f;
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			walkFrame++;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT03)); break;
			case 4	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT04)); break;
			case 5	:	sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT05)); break;
			default :
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALK_RIGHT00));
			}
			}
		}
	}
	
	/**
	 * Gestisce l'animazione completa della stance con l'arco, passando
	 * una nuova atlasKey in base alla direzione presa dal nemico.
	 */
	private void displayBowAnimation()
	{
		switch(direction)
		{
		case DOWN	:	aimDownAnimation();		break;
		case LEFT	:	aimLeftAnimation();		break;
		case RIGHT	:	aimRightAnimation();	break;
		case UP		:	aimUpAnimation();		break;
		case IDLE	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_FRONT00));
		}
	}
	
	/**
	 * Gestisce l'animazione della mira con l'arco verso il basso.
	 */
	private void aimDownAnimation()
	{
		float bowTimeFrames = 0.15f;
		while(animFrameDelay >= bowTimeFrames)
		{
			animFrameDelay -= bowTimeFrames;
			switch(aimingFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_FRONT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_FRONT03)); break;
			default	:
			{
				aimingFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_FRONT00));
			}
			}
			aimingFrame++;
		}
	}

	/**
	 * Gestisce l'animazione della mira con l'arco verso l'alto.
	 */
	private void aimUpAnimation()
	{
		float bowTimeFrames = 0.15f;
		while(animFrameDelay >= bowTimeFrames)
		{
			animFrameDelay -= bowTimeFrames;
			switch(aimingFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_BACK00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_BACK02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_BACK03)); break;
			default	:
			{
				aimingFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_BACK00));
			}
			}
			aimingFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della mira con l'arco verso sinistra.
	 */
	private void aimLeftAnimation()
	{
		float bowTimeFrames = 0.15f;
		while(animFrameDelay >= bowTimeFrames)
		{
			animFrameDelay -= bowTimeFrames;
			switch(aimingFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_LEFT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_LEFT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_LEFT03)); break;
			default	:
			{
				aimingFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_LEFT00));
			}
			}
			aimingFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della mira con l'arco verso destra.
	 */
	private void aimRightAnimation()
	{
		float bowTimeFrames = 0.15f;
		while(animFrameDelay >= bowTimeFrames)
		{
			animFrameDelay -= bowTimeFrames;
			switch(aimingFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_RIGHT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_RIGHT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_RIGHT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_RIGHT03)); break;
			default	:
			{
				aimingFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(BBWALKBOW_RIGHT00));
			}
			}
			aimingFrame++;
		}
	}
	
	@Override
	protected void displayTimeFinishedAnimation() {body.setLinearVelocity(0,0);}

	
//////////////////////////////////METODI PER LA GESTIONE DELL'ATTACCO/////////////////////////
	
	/**
	 * Lancia una freccia in base alla direzione attuale del nemico.
	 */
	private void shootArrow()
	{
		switch(direction)
		{
		case UP		: controller.getToAdd().add(new Arrow(new Vector2(position.x, position.y + 32f), controller, Direction.UP));	arrowsLeft--; break;
		case DOWN	: controller.getToAdd().add(new Arrow(new Vector2(position.x, position.y - 32f), controller, Direction.DOWN));	arrowsLeft--; break;
		case LEFT	: controller.getToAdd().add(new Arrow(new Vector2(position.x - 32f, position.y), controller, Direction.LEFT));	arrowsLeft--; break;
		case RIGHT	: controller.getToAdd().add(new Arrow(new Vector2(position.x + 32f, position.y), controller, Direction.RIGHT));	arrowsLeft--; break;
		case IDLE	: controller.getToAdd().add(new Arrow(new Vector2(position.x, position.y - 32f), controller, Direction.IDLE));	arrowsLeft--; 
		}
		canShoot = false;
	}
	
	/**
	 * Gestisce i cambi di strategia e comportamento del nemico.
	 */
	private void manageBehaviorChanges()
	{
		if(explosionAhead() || bombIsAhead())
		{
			behavior = new AvoidThreat(player, this, behavior.getMovementsCD(), 0.3f);
			behaviorTimes = 2.5f;
		}
		if(behaviorTimes <= 0)
		{
			if((isInDanger())) behavior = new AvoidThreat(player, this, behavior.getMovementsCD(), 0.3f);
			else behavior = new ApproachThreat(player, this, behavior.getMovementsCD(), 0.3f);
			behaviorTimes = 2.5f;
		}
	}
	
	/**
	 * Gestisce il reset dei cooldown del nemico.
	 */
	private void resetCooldowns()
	{
		if(atkCooldown <= 0.0f)
		{
			canShoot = true;
			atkCooldown = 5.0f;
		}
		if(stanceChangeCD <= 0.0f)
		{
			canSwapStance = true;
			stanceChangeCD = 5.0f;
		}
	}
	
	/**
	 * Gestisce i cooldown del nemico.
	 * @param deltaTime Tempo delta del gioco.
	 */
	private void manageCooldowns(float deltaTime)
	{
		animFrameDelay += deltaTime;
		behaviorTimes -= deltaTime;
		if(!canShoot) atkCooldown -= deltaTime;
		if(!canSwapStance) stanceChangeCD -= deltaTime;
		resetCooldowns();
	}
	
	/**
	 * Controlla se il nemico potrebbe colpire il bersaglio, controllando il range dell'attacco.
	 * @return True se potrebbe colpire il giocatore, false altrimenti.
	 */
	private boolean couldHit()
	{
		boolean throwArrow = false;
		switch(direction)
		{
		case UP		:	throwArrow = tryAttackUp(); 	break;
		case DOWN	:	throwArrow = tryAttackDown();	break;
		case IDLE	:	throwArrow = tryAttackDown();	break;
		case LEFT	:	throwArrow = tryAttackLeft();	break;
		case RIGHT	:	throwArrow = tryAttackRight();
		}
		return throwArrow;
	}

	/**
	 * Controlla se il giocatore è a portata del colpo nella zona sopra stante.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackUp()
	{
		if(xIsAligned())
			if(player.getPosition().y >= position.y + 8f && player.getPosition().y <= position.y + 8f + atkRange) return true;
		return false;
	}
	
	/**
	 * Controlla se il giocatore è a portata del colpo nella zona sotto stante.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackDown()
	{
		if(xIsAligned())
			if(player.getPosition().y <= position.y - 8f && player.getPosition().y >= position.y - 8f - atkRange) return true;
		return false;
	}
	
	/**
	 * Controlla se il giocatore è a portata del colpo nella zona a sinistra.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackLeft()
	{
		if(yIsAligned())
			if(player.getPosition().x >= position.x - 8f - atkRange && player.getPosition().x <= position.x - 8) return true;
		return false;
	}
	
	/**
	 * Controlla se il giocatore è a portata del colpo nella zona a destra.
	 * @return True se il giocatore è a portata, false altrimenti.
	 */
	private boolean tryAttackRight()
	{
		if(yIsAligned())
			if(player.getPosition().x <= position.x + 8f + atkRange && player.getPosition().x >= position.x - 8) return true;
		return false;
	}
	
	/**
	 * Controlla se il nemico è allineato con il giocatore sull'asse Y.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean xIsAligned()	{return player.getPosition().x <= position.x + 8f && player.getPosition().x >= position.x - 8f;}
	
	/**
	 * Controlla se il nemico è allineato con il giocatore sull'asse X.
	 * @return True se è allineato, false altrimenti.
	 */
	private boolean yIsAligned()	{return player.getPosition().x <= position.x + 8f && player.getPosition().x >= position.x - 8f;}
}
