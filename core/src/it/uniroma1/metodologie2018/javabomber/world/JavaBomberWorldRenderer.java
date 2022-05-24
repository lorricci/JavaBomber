package it.uniroma1.metodologie2018.javabomber.world;


import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.HEIGHT;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.PPM;
import static it.uniroma1.metodologie2018.javabomber.utils.BodyRelatedConstants.WIDTH;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import it.uniroma1.metodologie2018.javabomber.JavaBomber;
import it.uniroma1.metodologie2018.javabomber.interfaces.Entity;
import it.uniroma1.metodologie2018.javabomber.interfaces.GraphicEntity;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldController;
import it.uniroma1.metodologie2018.javabomber.interfaces.WorldRenderer;

/**
 * Gestore del rendering delle entità presenti nel gioco.
 * @author Federico Scozzafava + (Lorenzo Ricci)
 *
 */
public class JavaBomberWorldRenderer implements WorldRenderer 
{

	/**
	 * Gestore della simulazione fisica del gioco.
	 */
	private WorldController controller;
	
	/**
	 * Batch del gioco.
	 */
	private Batch batch;
	
	/**
	 * Istanza di gioco.
	 */
	private JavaBomber game;
	
	/**
	 * Camera utilizzata per la visuale di gioco.
	 */
	private OrthographicCamera gameCamera;
	
	/**
	 * Camera di debug per la visualizzazione di hitbox e hurtbox.
	 */
	private Box2DDebugRenderer debugRenderer;
	
	/**
	 * Costruisce una istanza del gestore di rendering del gioco.
	 * @param game Istanza del gioco.
	 * @param worldController Gestore della simulazione fisica.
	 */
	public JavaBomberWorldRenderer(JavaBomber game, WorldController worldController) 
	{
		debugRenderer = new Box2DDebugRenderer();
		gameCamera = new OrthographicCamera(WIDTH / PPM, HEIGHT / PPM);
		gameCamera.zoom /= PPM;
		gameCamera.position.set((WIDTH / PPM) / 2, (HEIGHT / PPM) / 2, 0);
		controller = worldController;
		batch = game.getBatch();
		this.game = game;
	}

	@Override
	public void dispose() 
	{
		batch.dispose();
		debugRenderer.dispose();
		controller.dispose();
	}

	@Override
	public void render() {
		batch.begin();
		for(GraphicEntity e : controller.getMapRender())
			e.draw(batch);
		for(Entity e : controller.getEntities())
			if (e instanceof GraphicEntity)	((GraphicEntity) e).draw(batch);
		controller.getHUD().draw(batch);
		batch.end();
		game.getBatch().setProjectionMatrix(gameCamera.combined);
		//debugRenderer.render(controller.getWorld(), gameCamera.combined);
	}
	
	@Override
	public void resize(int width, int height) 
	{game.getBatch().setProjectionMatrix(gameCamera.combined);}
	
	@Override
	public OrthographicCamera getGameCamera()	{return gameCamera;}
}
