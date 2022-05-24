package it.uniroma1.metodologie2018.javabomber.entities;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLCRYING00;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLCRYING01;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLCRYING02;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLCRYING03;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH00;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH01;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH02;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH03;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH04;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH05;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH06;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH07;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLDEATH08;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLIDLE;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_BACK00;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_BACK01;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_BACK02;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_BACK03;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_FRONT00;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_FRONT01;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_FRONT02;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_FRONT03;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_LEFT00;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_LEFT01;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_LEFT02;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_LEFT03;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_RIGHT00;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_RIGHT01;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_RIGHT02;
import static it.uniroma1.metodologie2018.javabomber.utils.PlayerAnimationFrames.PLWALK_RIGHT03;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.Direction;
/**
 * Costruisce il personaggio controllato dal giocatore.
 * Contiene metodi di movimento e gestione del piazzamento delle bombe.
 * TASTI DI MOVIMENTO = FRECCE DIREZIONALI
 * SPINTA DELLA BOMBA = TASTO Z + TASTO DIREZIONALE
 * @author Lorenzo Ricci
 *
 */
public class Player extends Characters implements InputProcessor
{
	
//////////////////////CAMPI DELLA CLASSE/////////////////////////////
	
	/**
	 * Frame corrente delle animazioni di camminata del personaggio.
	 */
	private int walkFrame;
	
	/**
	 * Frame corrente per l'animazione del pianto.
	 */
	private int cryingFrame;
	
	/**
	 * Frame della morte del pg.
	 */
	private int deathFrame;
	
	/**
	 * Accumulatore del delay dell'animazione. Serve a gestire più lentamente le transizioni
	 * da uno sprite all'altro durante le animazioni di camminata.
	 */
	private float animFrameDelay;
	
	/**
	 * Tempo di delay nella transizione da uno sprite all'altro.
	 */
	private float timeFrames = 0.15f;
	
	/**
	 * Indica se il personaggio è su un tile ghiacciato o meno.
	 */
	private boolean onFrozenTile;

////////////////////////////COSTRUTTORE///////////////////////////////////////
	
	/**
	 * Costruisce un nuovo giocatore.
	 * @param controller World associato al gioco.
	 * @param position	Posizione in cui spawnare il giocatore.
	 */
	public Player(WorldController controller, Vector2 position) 
	{
		//Setup dei dati.
		super(controller, position);
		lives = 3;
		size = new Vector2(16f, 16f);
		textureAtlas = new TextureAtlas(Gdx.files.internal("SpriteSheets/Player/sprites.txt"));
		//Setup del corpo.
		body = dispenser.dispenseCharacterBody(position, size, this);
		//Setup dello sprite.
		sprite = new Sprite(textureAtlas.findRegion(PLIDLE));
		sprite.setSize((sprite.getWidth() / 1.3f ) / PPM, (sprite.getHeight() / 1.3f) / PPM);
		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		handleSprite(body, sprite);
	}
	
	
//////////////////////////////METODI GETTER E SETTER////////////////////////////////
	
	/**
	 * Scambia lo stato di camminata su terreno ghiacciato del giocatore.
	 */
	public void switchOnFrozenTile()	{onFrozenTile = !onFrozenTile;}
	
	/**
	 * Adatta la velocità del giocatore in base al suo stato di terreno ghiacciato
	 * e velocità attuale.
	 */
	private void adaptCharSpeed()	{if(onFrozenTile && charSpeed > 0.50f) charSpeed *= 0.50f;}
	
	
//////////////////////////////METODI PER IL PIAZZAMENTO DELLA BOMBA/////////////////////////
	
	/**
	 * Posiziona la bomba del giocatore.
	 */
	private void setBomb() 
	{
		if(bombStats.canPlace())
		{
			Vector2 bombPos = getPosition();
			Bomb placed = new Bomb(bombPos, controller, this);
			bombStats.setBombInfos(placed);
			controller.addEntity(placed);
			bombStats.subPlacing();
			controller.getPresentBombs().add(placed);
		}
	}
	
	
////////////////////////METODI PER LA GESTIONE DELLO SPRITE E DELLE ANIMAZIONI/////////////////////
	
	@Override
	public void handleSprite(Body body, Sprite sprite)
	{sprite.setPosition(body.getPosition().x - (sprite.getWidth() / 2f), body.getPosition().y - (sprite.getHeight()) / 3.5f );}

	/**
	 * Gestisce l'animazione della camminata in base alla direzione
	 * del personaggio.
	 * @param deltaTime Tempo delta del gioco.
	 */
	@Override
	protected void displayWalkingAnimation()
	{
		switch(direction)
		{
		case UP		:	walkUpAnimation();		break;
		case DOWN	:  	walkDownAnimation(); 	break;
		case LEFT	: 	walkLeftAnimation(); 	break;
		case RIGHT	:  	walkRightAnimation(); 	break;
		case IDLE	:	sprite.setRegion(textureAtlas.findRegion(PLIDLE)); break;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso il basso.
	 */
	private void walkDownAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_FRONT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_FRONT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_FRONT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_FRONT03)); break;
			default :	
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(PLWALK_FRONT00));
			}
			}
			walkFrame++;
		}
	}

	/**
	 * Gestisce l'animazione della camminata verso l'alto.
	 */
	private void walkUpAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_BACK00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_BACK01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_BACK02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_BACK03)); break;
			default :	
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(PLWALK_BACK00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso sinistra.
	 */
	private void walkLeftAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_LEFT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_LEFT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_LEFT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_LEFT03)); break;
			default :	
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(PLWALK_LEFT00));
			}
			}
			walkFrame++;
		}
	}
	
	/**
	 * Gestisce l'animazione della camminata verso destra.
	 */
	private void walkRightAnimation()
	{
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(walkFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_RIGHT00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_RIGHT01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_RIGHT02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(PLWALK_RIGHT03)); break;
			default :	
			{
				walkFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(PLWALK_RIGHT00));
			}
			}
			walkFrame++;
		}
	}
	
	@Override
	protected void displayDeathAnimation() 
	{
		float deathTimeFrames = 0.18f;
		while(animFrameDelay >= deathTimeFrames)
		{
			animFrameDelay -= deathTimeFrames;
			if(deathFrame > 8)	killEntity();
			else 
			{
				switch(deathFrame)
				{
				case 0	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH00)); break;
				case 1	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH01)); break;
				case 2	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH02)); break;
				case 3	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH03)); break;
				case 4	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH04)); break;
				case 5	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH05)); break;
				case 6	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH06)); break;
				case 7	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH07)); break;
				case 8	:	sprite.setRegion(textureAtlas.findRegion(PLDEATH08)); break;
				}
				deathFrame++;
			}
		}
	}
	
	@Override
	protected void displayTimeFinishedAnimation()
	{
		body.setLinearVelocity(0,0);
		while(animFrameDelay >= timeFrames)
		{
			animFrameDelay -= timeFrames;
			switch(cryingFrame)
			{
			case 0	:	sprite.setRegion(textureAtlas.findRegion(PLCRYING00)); break;
			case 1	:	sprite.setRegion(textureAtlas.findRegion(PLCRYING01)); break;
			case 2	:	sprite.setRegion(textureAtlas.findRegion(PLCRYING02)); break;
			case 3	:	sprite.setRegion(textureAtlas.findRegion(PLCRYING03)); break;
			default :
			{
				cryingFrame = 0;
				sprite.setRegion(textureAtlas.findRegion(PLCRYING00));
			}
			}
			cryingFrame++;
		}
	}
	
	@Override
	public synchronized void update(float deltaTime) 
	{
		animFrameDelay += deltaTime;
		//Controlla se il pg è morto per gestire l'animazione della morte e il dispose della texture.
		if(checkIfDead()) displayDeathAnimation();
		//Controlla l'animazione del personaggio nel momento in cui si conclude il tempo di gioco.
		else if(controller.getHUD().getGameTimer().isFinished()) displayTimeFinishedAnimation();
		//Controlla le diverse animazioni del personaggio.
		else
		{
			adaptCharSpeed();
			if(bombIsAhead()) {if(!canPassBombs()) body.setLinearVelocity(0,0);}
			else if(charIsAhead()) body.setLinearVelocity(0,0);
			else body.setLinearVelocity(direction.getDirVector().x * Gdx.graphics.getDeltaTime() * PPM * charSpeed, direction.getDirVector().y * Gdx.graphics.getDeltaTime() * PPM * charSpeed);
			setPosition(new Vector2(body.getPosition().x * PPM, body.getPosition().y * PPM));
			displayWalkingAnimation();
			handleSprite(body, sprite);
		}
	}
	
	@Override
	public void draw(Batch batch) {sprite.draw(batch);}

	
//////////////////////METODI PER LA GESTIONE DEL MOVIMENTO////////////////////////////
	
	@Override
	public boolean keyDown(int keycode) 
	{
		switch(keycode)
		{
		case Keys.UP	: direction = Direction.UP; break;
		case Keys.DOWN	: direction = Direction.DOWN; break;
		case Keys.LEFT	: direction = Direction.LEFT; break;
		case Keys.RIGHT	: direction = Direction.RIGHT; break;
		case Keys.SPACE	: setBomb(); break;
		case Keys.Z		: wantToPush();
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) 
	{
		switch(keycode)
		{
		case Keys.UP:		direction = Direction.IDLE; break;
		case Keys.DOWN:		direction = Direction.IDLE; break;
		case Keys.LEFT:		direction = Direction.IDLE; break;
		case Keys.RIGHT:	direction = Direction.IDLE; break;
		case Keys.Z:		pushing = false; 
		}
		return true;
	}
	
	/**
	 * Gestisce l'attivazione della spinta.
	 */
	private void wantToPush()	{if(canPush && bombIsAhead()) pushing = true;}
	
	@Override
	public boolean keyTyped(char character) {return false;}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) 	{return false;}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}

	@Override
	public boolean scrolled(int amount) {return false;}
}
