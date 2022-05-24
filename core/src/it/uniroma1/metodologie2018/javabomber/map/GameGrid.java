package it.uniroma1.metodologie2018.javabomber.map;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import it.uniroma1.metodologie2018.javabomber.interfaces.Block;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;

/**
 * Struttura della griglia di gioco.
 * @author Lorenzo Ricci
 */
public class GameGrid
{
	
////////////////////////////////CAMPI DELLA CLASSE///////////////////////////
	
	/**
	 * Griglia di gioco rappresentata tramite una lista di blocchi
	 * o celle.
	 */
	private ArrayList<Block> grid;

	/**
	 * WorldController associato alla mappa.
	 */
	private WorldController controller;
	
	
///////////////////////COSTRUTTORE////////////////////////
	
	/**
	 * Costruisce la griglia di gioco. 
	 * @param controller Worldcontroller associato alla mappa.
	 */
	public GameGrid(WorldController controller)	{this.controller = controller;}
	
	
////////////////////METODI GETTER E SETTER////////////////////
	
	/**
	 * Restituisce l'attuale griglia di gioco.
	 * @return Griglia della mappa attuale.
	 */
	public ArrayList<Block> getGrid()	{return grid;}

	
/////////////////////METODI PER LA COSTRUZIONE DEGLI STAGE///////////////////////
	
	/**
	 * Imposta lo stage per il testing di collisioni e danni 
	 * alle entità.
	 */
	public void setTestStage()
	{
		boolean floorLine = true;	//Alterna colonne di floor e colonne alternate.
		ArrayList<Block> tiles = new ArrayList<>();
		for(float x = -560; x < 624; x += 32)	//Scorre tutte le colonne.
		{
			if(x == -560 || x == 592)	allBorder(tiles, x); //Caso dei due bordi dello stage.
			else 
			{
				if(floorLine)
				{
					allFloor(tiles, x);
					floorLine = false;
				}
				else
				{
					alternateLine(tiles, x);
					floorLine = true;
				}
			}
		}
		tiles.addAll(generateWalls(tiles)); //Aggiunge dei muri distruttibili in maniera casuale.
		grid = tiles;
	}


/////////////////////METODI PER LA COSTRUZIONE DI SPECIFICHE LINEE DI MAPPA//////////////////////
	
	/**
	 * Costruisce una colonna dello stage che alterna il posizionamento di un SolidBlock con quello
	 * di un Floor.
	 * @param controller Controller del mondo a cui aggiungere tiles.
	 * @param tiles	Lista dei tiles della mappa da costruire.
	 * @param x	Posizione x su cui iterare la colonna.
	 */
	private void alternateLine(ArrayList<Block> tiles, float x)
	{
		boolean bPutWall = false;	//Alterna il posizionamento del muro al posizionamento del floor.
		for(float y = -368; y < 337; y += 32)
		{
			if(y == -368 || y == 336) 	tiles.add(new BorderWall(new Vector2(x, y), controller)); //Caso del bordo dello stage.
			else 
			{
				if(bPutWall)	
					{
						tiles.add(new BorderWall(new Vector2(x, y), controller));
						bPutWall = false;
					}
				else
				{
					tiles.add(new Floor(new Vector2(x, y), controller));
					bPutWall = true;
				}
			}
		} 
	}
	
	/**
	 * Costruisce una colonna dello stage composta di soli Floor.	 
	 * @param controller Controller del mondo a cui aggiungere tiles.
	 * @param tiles	Lista dei tiles della mappa da costruire.
	 * @param x	Posizione x su cui iterare la colonna.
	 */
	private void allFloor(ArrayList<Block> tiles, float x)
	{
		for(float y = -368; y < 337; y += 32)
		{
			if(y == -368 || y == 336) tiles.add(new BorderWall(new Vector2(x, y), controller)); //Caso del bordo dello stage. 
			else tiles.add(new Floor(new Vector2(x, y), controller));
		}
	}
	
	/**
	 * Costruisce una colonna dello stage composta di soli Solid Blocks.
	 * @param controller Controller del mondo a cui aggiungere tiles.
	 * @param tiles	Lista dei tiles della mappa da costruire.
	 * @param x	Posizione x su cui iterare la colonna.
	 */
	private void allBorder(ArrayList<Block> tiles, float x)
	{
		for(float y = -368; y < 337; y += 32)
			tiles.add(new BorderWall(new Vector2(x, y), controller));
	}
	
	
///////////////////METODI PER LA GENERAZIONE RANDOMICA DEI MURI DISTRUTTIBILI///////////////////
	
	/**
	 * Genera automaticamente un certo numero di muri distruttibili all'interno della mappa di gioco.
	 * Divide la mappa in 4 regioni e piazza 1/4 del numero totale di muri per ogni regione, distribuendo i
	 * blocchi in maniera casuale all'interno di ogni regione.
	 * Effettua diversi controlli per evitare che i punti di partenza dei giocatori (quattro angoli dell'arena)
	 * siano bloccati o impossibili da passare senza prendere danni.
	 * @param controller Controller del mondo a cui aggiungere i muri
	 * @param tiles	Lista dei tiles della mappa
	 * @return	Lista di muri da aggiungere alla mappa
	 */
	private ArrayList<Block> generateWalls(ArrayList<Block> tiles)
	{
		int blCounter = 80; //Numero di muri
		Random random = new Random();
		ArrayList<Block> toAdd = new ArrayList<>();
		while(blCounter > 0)
		{
			//Regione angolo superiore sinistro.
			if(blCounter >= 60) 
				{if(generateTopLeft(random, tiles, toAdd, blCounter)) blCounter--;}
			//Regione angolo inferiore sinistro.
			else if(blCounter <= 59 && blCounter >= 40)
				{if(generateBottomLeft(random, tiles, toAdd, blCounter)) blCounter--;}
			//Regione in alto a destra.
			else if(blCounter <= 39 && blCounter >= 20)
				{if(generateTopRight(random, tiles, toAdd, blCounter)) blCounter--;}
			//Regione in basso a destra.
			else 
				{if(generateBottomRight(random, tiles, toAdd, blCounter)) blCounter--;}
		}
		return toAdd;
	}
	
	/**
	 * Genera una posizione casuale all'interno del quadrante top_left della mappa. 
	 * Se possibile, lo aggiunge alla lista di blocchi. 
	 * @param random Randomizzatore usato per la creazione dei blocchi.
	 * @param tiles Lista dei tiles che costituiscono la mappa.
	 * @param toAdd Lista dei blocchi che verranno aggiunti nella costruzione della mappa.
	 * @param blCounter Numero di blocchi ancora da piazzare.
	 * @return True se è stato piazzato un muro, false altrimenti.
	 */
	private boolean generateTopLeft(Random random, ArrayList<Block> tiles, ArrayList<Block> toAdd, int blCounter)
	{
		int posX = (32 * -random.nextInt(18)) - 16; //Genera una posizione X negativa casuale.
		int posY = (32 * random.nextInt(11))  + 16; //Genera una posizione Y positiva casuale.
		if(posX == -528 && posY == 304 || posX == -496 && posY == 304 || posX == -528 && posY == 272) //Angolo superiore sx
			return false;
		else return setDestructibleWalls(tiles, toAdd, posX, posY, blCounter);
			
	}
	
	/**
	 * Genera una posizione casuale all'interno del quadrante top_right della mappa. 
	 * Se possibile, lo aggiunge alla lista di blocchi. 
	 * @param random Randomizzatore usato per la creazione dei blocchi.
	 * @param tiles Lista dei tiles che costituiscono la mappa.
	 * @param toAdd Lista dei blocchi che verranno aggiunti nella costruzione della mappa.
	 * @param blCounter Numero di blocchi ancora da piazzare.
	 * @return True se è stato piazzato un muro, false altrimenti.
	 */
	private boolean generateTopRight(Random random, ArrayList<Block> tiles, ArrayList<Block> toAdd, int blCounter)
	{
		int posX = (32 * random.nextInt(18)) + 16; //Genera una posizione X positiva casuale.
		int posY = (32 * random.nextInt(11)) + 16; //Genera una posizione Y positiva casuale.
		if(posX == 528 && posY == 304 || posX == 496 && posY == 304 || posX == 528 && posY == 272) //Angolo superiore dx 
				return false;
		else return setDestructibleWalls(tiles, toAdd, posX, posY, blCounter);
	}
	
	/**
	 * Genera una posizione casuale all'interno del quadrante bottom_left della mappa. 
	 * Se possibile, lo aggiunge alla lista di blocchi. 
	 * @param random Randomizzatore usato per la creazione dei blocchi.
	 * @param tiles Lista dei tiles che costituiscono la mappa.
	 * @param toAdd Lista dei blocchi che verranno aggiunti nella costruzione della mappa.
	 * @param blCounter Numero di blocchi ancora da piazzare.
	 * @return True se è stato piazzato un muro, false altrimenti.
	 */
	private boolean generateBottomLeft(Random random, ArrayList<Block> tiles, ArrayList<Block> toAdd, int blCounter)
	{
		int posX = (32 * -random.nextInt(18)) - 16; //Genera una posizione X negativa casuale.
		int posY = (32 * -random.nextInt(11)) - 16; //Genera una posizione Y negativa casuale.
		if(posX == -528 && posY == -304 || posX == -496 && posY == -304 || posX == -528 && posY == -272) //Angolo inferiore sx
			return false;
		else
		{
			return setDestructibleWalls(tiles, toAdd, posX, posY, blCounter);
		}
	}
	
	/**
	 * Genera una posizione casuale all'interno del quadrante bottom_right della mappa. 
	 * Se possibile, lo aggiunge alla lista di blocchi. 
	 * @param random Randomizzatore usato per la creazione dei blocchi.
	 * @param tiles Lista dei tiles che costituiscono la mappa.
	 * @param toAdd Lista dei blocchi che verranno aggiunti nella costruzione della mappa.
	 * @param blCounter Numero di blocchi ancora da piazzare.
	 * @return True se è stato piazzato un muro, false altrimenti.
	 */
	private boolean generateBottomRight(Random random, ArrayList<Block> tiles, ArrayList<Block> toAdd, int blCounter)
	{
		int posX = (32 * random.nextInt(18)) + 16; //Genera una posizione X positiva casuale.
		int posY = (32 * -random.nextInt(11)) - 16; //Genera una posizione Y negativa casuale.
		if(posX == 560 && posY == -336 || posX == 528 && posY == -336 || posX == 560 && posY == -304) //Angolo inferiore dx 
				return false;
		else return setDestructibleWalls(tiles, toAdd, posX, posY, blCounter);
		
	}
	
	/**
	 * Controlla se nella posizione data è possibile piazzare un blocco.
	 * Per farlo controlla se la zona corrisponde ad una istanza di Floor.
	 * @param tiles Lista dei blocchi che costituiscono la mappa.
	 * @param toAdd Lista dei blocchi da aggiungere alla mappa.
	 * @param posX Posizione X da controllare.
	 * @param posY Posizione Y da controllare.
	 * @param blCounter Numero di blocchi.
	 * @return True se il muro è stato piazzato, false altrimenti.
	 */
	private boolean setDestructibleWalls(ArrayList<Block> tiles, ArrayList<Block> toAdd, float posX, float posY, int blCounter)
	{
		for(Block block : tiles)
			if(block instanceof Floor)
				if(block.getPosition().x == posX && block.getPosition().y == posY)	
				{
					Floor fl = (Floor) block;
					fl.spawnWall();
					toAdd.add(new BaseWall(new Vector2(posX, posY), controller));
					return true;
				}
		return false;
	}
	
}
