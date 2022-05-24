package it.uniroma1.metodologie2018.javabomber.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import it.uniroma1.metodologie2018.javabomber.interfaces.Block;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.utils.BodyDispenser;

/**
 * Rappresentazione astratta di una cella di gioco. Struttura le informazioni 
 * fondamentali di ogni cella della mappa, a prescindere dal loro tipo.
 * @author Lorenzo Ricci
 *
 */
public abstract class Tile implements Block 
{
	
//////////////////CAMPI DELLA CLASSE///////////////////////////
	
	/**
	 * Posizione della cella.
	 */
	protected Vector2 position;
	
	/**
	 * Grandezza della cella.
	 */
	protected Vector2 size = new Vector2(32.0f, 32.0f);
	
	/**
	 * Sprite della cella.
	 */
	protected Sprite sprite;
	
	/**
	 * Body della cella.
	 */
	protected Body body;
	
	/**
	 * Dispenser del corpo di ogni cella.
	 */
	protected BodyDispenser dispenser;
	
	/**
	 * World legato a ogni tile del gioco.
	 */
	protected WorldController controller;
	
	/**
	 * TextureAtlas per la gestione delle texture di ogni tile.
	 */
	protected TextureAtlas textureAtlas;
	
/////////////////////////COSTRUTTORE//////////////////////////////
	
	/**
	 * Supercostruttore di qualsiasi cella della mappa.
	 * @param position	Posizione della cella.
	 * @param controller Gestore della simulazione fisica del gioco.
	 */
	public Tile(Vector2 position, WorldController controller)	
	{
		this.position = position;
		this.controller = controller;
		dispenser = new BodyDispenser(controller);
		textureAtlas = new TextureAtlas(Gdx.files.internal("Spritesheets/Tiles/sprites.txt"));
	}

	
/////////////////////////METODI GETTER E SETTER//////////////////////////
	
	@Override
	public Vector2 getSize() {return size;}

	@Override
	public Vector2 getPosition() {return position;}

	@Override
	public void setPosition(Vector2 position) {this.position = position;}

	@Override
	public Body getBody() {return body;}

//////////////////////METODI PER LA GESTIONE DEL BODY////////////////////
	
	@Override
	public void disposeTexture()	{textureAtlas.dispose();}
}
