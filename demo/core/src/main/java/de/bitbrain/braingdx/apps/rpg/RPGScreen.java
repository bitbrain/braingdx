package de.bitbrain.braingdx.apps.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.MovementController;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RandomOrientationMovementController;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapRenderer;
import de.bitbrain.braingdx.world.GameObject;

import java.util.HashMap;
import java.util.Map;

public class RPGScreen extends AbstractScreen<RPGTest> {

   private static final int BLOCK_SIZE = 16;
   private NPCFactory factory;

   public RPGScreen(RPGTest rpgTest) {
      super(rpgTest);
   }

   @Override
   protected void onCreate(GameContext context) {
	  context.getGameCamera().setDefaultZoomFactor(0.25f);
	  context.getGameCamera().setTargetTrackingSpeed(1.6f);
	  context.getGameCamera().setZoomScalingFactor(0.001f);
      prepareResources(context);
      setupShaders(context);

   }

   private void prepareResources(GameContext context) {
      TiledMap map = SharedAssetManager.getInstance().get(Assets.RPG.MAP_1, TiledMap.class);
      context.getRenderPipeline().put(RenderPipeIds.BACKGROUND,
            new TiledMapRenderer(map, (OrthographicCamera) context.getGameCamera().getInternalCamera()));
      context.getLightingManager().setAmbientLight(new Color(0.1f, 0.05f, 0.3f, 0.4f));
      Texture texture = SharedAssetManager.getInstance().get(Assets.RPG.CHARACTER_TILESET);

      factory = new NPCFactory(BLOCK_SIZE, context.getGameWorld());
      GameObject player = spawnObject(context, 10, 10, NPC.CITIZEN_MALE, new OrientationMovementController());
      context.getGameCamera().setTrackingTarget(player);
      final int NPCS = 25;

      for (int i = 0; i < NPCS; ++i) {
         int randomX = (int) (Math.random() * 25);
         int randomY = (int) (Math.random() * 25);
         spawnObject(context, randomX, randomY, NPC.random(), new RandomOrientationMovementController());
      }

      context.getLightingManager().addPointLight("lantern", 200, 200, 500, Color.valueOf("ff9955ff"));
   }

   private GameObject spawnObject(GameContext context, int indexX, int indexY, NPC type, MovementController<Orientation> controller) {
      GameObject object = factory.spawn(indexX, indexY, type.ordinal(), controller);
      RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, context.getTiledMapManager().getAPI()).interval(0.3f).rasterSize(BLOCK_SIZE,
            BLOCK_SIZE);
      context.getBehaviorManager().apply(behavior, object);
      return object;
   }

   private void setupShaders(GameContext context) {
      RenderPipe worldPipe = context.getRenderPipeline().getPipe(RenderPipeIds.WORLD);
      Bloom bloom = new Bloom(Math.round(Gdx.graphics.getWidth() / 3.5f), Math.round(Gdx.graphics.getHeight() / 3.5f));
      bloom.setBlurAmount(0.1f);
      bloom.setBloomIntesity(1.2f);
      bloom.setBlurPasses(5);
      Vignette vignette = new Vignette(Math.round(Gdx.graphics.getWidth() / 1.5f),
            Math.round(Gdx.graphics.getHeight() / 1.5f), false);
      vignette.setIntensity(0.8f);
      worldPipe.addEffects(vignette);
      worldPipe.addEffects(bloom);
   }
}