package it.uniroma1.metodologie2018.javabomber.utils;

import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_BOMB;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_CHARS;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_EXPLOSION;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_FLOOR;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_POWERUP;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.CATEGORY_WALLS;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_BOMB;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_CHARS;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_ENEMY;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_EXPLOSION;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_FLOOR;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_POWERUP;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.MASK_WALLS;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

import it.uniroma1.metodologie2018.javabomber.entities.Enemy;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;

/**
 * Classe di utility utilizzata nel fornire Body nella creazione di nuove entità 
 * di gioco. Esegue un metodo per ogni tipo di entità differente nel gioco.
 * @author Lorenzo Ricci
 *
 */
public class BodyDispenser 
{
	
///////////////////////CAMPI DELLA CLASSE/////////////////////
	
	/**
	 * Mondo in cui creare i Body.
	 */
	private WorldController controller;
	
	
////////////////////////COSTRUTTORE///////////////////////////
	
	/**
	 * Costruttore del dispenser.
	 * @param controller	Mondo in cui creare i Body.
	 */
	public BodyDispenser(WorldController controller)	{this.controller = controller;}
	
	
//////////////////////METODI PER IL SETTING DEI BODY//////////////
	
	/**
	 * Crea un body con le impostazioni minimali comuni a tutti i body del gioco.
	 * Utilizzato per maggiore leggibilità del codice di ciascun dispense method.
	 * @param position	Posizione del body.
	 * @param type	Tipo di body.
	 * @return	Body appena creato.
	 */
	private Body setBaseBodyInfos(Vector2 position, BodyType type)
	{
		BodyDef bDef = new BodyDef();
		bDef.position.set(position.x / PPM, position.y / PPM);
		bDef.fixedRotation = true;
		bDef.type = type;
		Body body = controller.getWorld().createBody(bDef);
		return body;
	}
	
	/**
	 * Imposta il body in input come un Sensore.
	 * @param body Body da impostare come Sensore.
	 */
	private void setSensors(Body body)
	{body.getFixtureList().forEach(fix -> fix.setSensor(true));}
	
	/**
	 * Imposta l'userdata del body, associandogli l'istanza dell'oggetto costruito.
	 * @param body Body in cui salvare lo userData.
	 * @param instance Istanza dell'oggetto associato al body.
	 */
	private void setData(Body body, GraphicEntity instance)
	{
		body.setUserData(new JavaBomberUserData(instance));
		for(Fixture fix : body.getFixtureList())
			fix.setUserData(new JavaBomberUserData(instance));
	}
	
	/**
	 * Assegna alla fixture tutti i parametri di base passati in input.
	 * @param shape Forma della fixture.
	 * @param density Densità della fixture.
	 * @param categoryBit Bit di categoria per il filtraggio della fixture.
	 * @param maskBit Maschera di collisione della fixture.
	 * @return Definizione della fixture con i settings impostati.
	 */
	private FixtureDef setupFixture(Shape shape, float density, short categoryBit, short maskBit)
	{
		FixtureDef fixture = new FixtureDef();
		fixture.shape = shape;
		fixture.density = density;
		fixture.filter.categoryBits = categoryBit;
		fixture.filter.maskBits = maskBit;
		return fixture;
	}
////////////////////////METODI PER I DIVERSI DISPENSER DI BODY/////////////////////////
	
	/**
	 * Crea un corpo con fixture e filtraggio apposito per i muri del gioco.
	 * @param position Posizione del muro.
	 * @param size	Grandezza del muro.
	 * @param instance Istanza dell'oggetto a cui associare il body in costruzione.
	 * @return	Body del muro.
	 */
	public Body dispenseWallBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.StaticBody);
		//Creazione della fixture.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((size.x / 2) / PPM, (size.y / 2) / PPM);
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_WALLS, MASK_WALLS);
		body.createFixture(fixture);
		//Dispose delle risorse.
		shape.dispose();
		
		//Setting dello userData.
		setData(body, instance);
		return body;
	}
	
	/**
	 * Crea un corpo con fixture e filtraggio apposito per la pavimentazione di gioco. Il pavimento
	 * è considerato un sensore.
	 * @param position Posizione del pavimento.
	 * @param size	Grandezza del pavimento.
	 * @param instance Istanza dell'oggetto a cui associare il body in costruzione.
	 * @return	Body del pavimento.
	 */
	public Body dispenseFloorBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.StaticBody);
		//Creazione della fixture.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((size.x / 2) / PPM, (size.y / 2) / PPM);
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_FLOOR, MASK_FLOOR);
		body.createFixture(fixture);
		//Dispose delle risorse.
		shape.dispose();
		
		//Setup dello userData e del sensor.
		setSensors(body);
		setData(body, instance);
		return body;
	}
	
	/**
	 * Crea un corpo con fixture e filtraggio apposito per i personaggi del gioco.
	 * @param position Posizione del personaggio.
	 * @param size	Grandezza del personaggio.
	 * @param instance Istanza dell'oggetto a cui associare il body in costruzione.
	 * @return	Body del personaggio. 
	 */
	public Body dispenseCharacterBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.DynamicBody);
		//Creazione della fixture.
		CircleShape shape = new CircleShape();
		shape.setRadius(size.x / 1.5f / PPM);
		short mask = instance instanceof Enemy ? MASK_ENEMY : MASK_CHARS;
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_CHARS, mask);
		body.createFixture(fixture);
		//Dispose delle risorse.
		shape.dispose();
		
		//Setup dello userData.
		setData(body, instance);
		return body;
	}
	
	/**
	 * Crea un corpo con fixture e filtraggio apposito per le bombe.
	 * @param position Posizione della bomba.
	 * @param size	Grandezza della bomba.
	 * @param instance Istanza dell'oggetto a cui associare il body in costruzione.
	 * @return Body della bomba.
	 */
	public Body dispenseBombBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.DynamicBody);
		//Creazione della fixture.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((size.x / 2f) / PPM, (size.y / 2f) / PPM);
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_BOMB, MASK_BOMB);
		body.createFixture(fixture);
		//Dispose delle risorse.
		shape.dispose();
		
		//Setup dello userData.
		setData(body, instance);
		return body;
	}
	
	/**
	 * Crea il body associato ad una esplosione del gioco. L'esplosione è impostata come Sensore.
	 * @param position Posizione dell'esplosione.
	 * @param size Grandezza dell'esplosione.
	 * @param instance Istanza dell'oggetto a cui associare il body in costruzione.
	 * @return Body dell'esplosione.
	 */
	public Body dispenseExplosionBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.DynamicBody);
		//Creazione della fixture.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((size.x / 2) / PPM, (size.y / 2) / PPM);
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_EXPLOSION, MASK_EXPLOSION);
		body.createFixture(fixture);
		//Dispose delle risorse.
		shape.dispose();

		//Setup dello userData e del sensor.
		setSensors(body);
		setData(body, instance);
		return body;
	}
	
	/**
	 * Crea il body associato ad un proiettile del gioco.
	 * @param position Posizione dell'esplosione.
	 * @param size Grandezza dell'esplosione.
	 * @param instance Istanza dell'oggetto a cui associare il body in costruzione.
	 * @return Body del proiettile.
	 */
	public Body dispenseBulletBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.DynamicBody);
		//Creazione della fixture.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox((size.x / 2) / PPM, (size.y / 2) / PPM);
		//FILTRATI COME ESPLOSIONI POICHE' DEVONO COLLIDERE ESATTAMENTE CON LE STESSE COSE.
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_EXPLOSION, MASK_EXPLOSION);
		body.createFixture(fixture);
		//Dispose delle risorse.
		shape.dispose();

		//Setup dello userData e del sensor.
		setData(body, instance);
		return body;
	}
	
	/**
	 * Crea il body associato ad un PowerUp, impostato come un sensor.
	 * @param position Posizione del PowerUp.
	 * @param size	Grandezza del PowerUp.
	 * @param instance Istanza del PowerUp.
	 * @return Body del PowerUp.
	 */
	public Body dispensePowerUpBody(Vector2 position, Vector2 size, GraphicEntity instance)
	{
		//Definizione e setup del corpo.
		Body body = setBaseBodyInfos(position, BodyType.DynamicBody);
		//Creazione della fixture.
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(size.x / 2f / PPM, size.y / 2f / PPM);
		FixtureDef fixture = setupFixture(shape, 1.0f, CATEGORY_POWERUP, MASK_POWERUP);
		body.createFixture(fixture);
		//Dispense delle risorse.
		shape.dispose();
	
		//Setup dello userData e del sensor.
		setSensors(body);
		setData(body, instance);
		return body;
	}	
}
