package de.bitbrain.braingdx.apps.rpg;

import java.util.HashMap;
import java.util.Map;

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
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationFactory;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationFactory.Index;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationSupplier;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.AnimationRenderer;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapRenderer;
import de.bitbrain.braingdx.world.GameObject;

public class RPGScreen extends AbstractScreen<RPGTest> {

   private static final int BLOCK_SIZE = 16;
   private NPCFactory factory;

   public RPGScreen(RPGTest rpgTest) {
      super(rpgTest);
   }

   @Override
   protected void onCreate(GameContext context) {
	  context.getGameCamera().setBaseZoom(0.25f);
	  context.getGameCamera().setSpeed(1.6f);
	  context.getGameCamera().setZoomScale(0.001f);
      prepareResources(context);
      setupShaders(context);

   }

   private void prepareResources(GameContext context) {
      TiledMap map = SharedAssetManager.getInstance().get(Assets.RPG.MAP_1, TiledMap.class);
      context.getRenderPipeline().put(RenderPipeIds.BACKGROUND,
            new TiledMapRenderer(map, (OrthographicCamera) context.getGameCamera().getInternal()));
      context.getLightingManager().setAmbientLight(new Color(0.1f, 0.05f, 0.3f, 0.4f));
      Texture texture = SharedAssetManager.getInstance().get(Assets.RPG.CHARACTER_TILESET);
      SpriteSheet sheet = new SpriteSheet(texture, 12, 8);
      createAnimations(context, sheet);

      factory = new NPCFactory(BLOCK_SIZE, context.getGameWorld());
      GameObject player = spawnObject(context, 10, 10, NPC.CITIZEN_MALE, new OrientationMovementController());
      context.getGameCamera().setTarget(player);
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
      RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller).interval(0.3f).rasterSize(BLOCK_SIZE,
            BLOCK_SIZE);
      context.getBehaviorManager().apply(behavior, object);
      return object;
   }

   private void createAnimations(GameContext context, SpriteSheet sheet) {
      Map<Integer, Index> indices = createSpriteIndices();
      SpriteSheetAnimationFactory animationFactory = new SpriteSheetAnimationFactory(sheet, indices);
      Map<Integer, SpriteSheetAnimation> animations = new HashMap<Integer, SpriteSheetAnimation>();
      for (int type : indices.keySet()) {
         SpriteSheetAnimation animation = animationFactory.create(type);
         animations.put(type, animation);
         SpriteSheetAnimationSupplier supplier = new SpriteSheetAnimationSupplier(orientations(), animation,
               AnimationTypes.FORWARD_YOYO);
         context.getBehaviorManager().apply(supplier);
         context.getRenderManager().register(type, new AnimationRenderer(supplier));
      }
   }

   private Map<Orientation, Integer> orientations() {
      Map<Orientation, Integer> map = new HashMap<Orientation, Integer>();
      map.put(Orientation.DOWN, 0);
      map.put(Orientation.LEFT, 1);
      map.put(Orientation.RIGHT, 2);
      map.put(Orientation.UP, 3);
      return map;
   }

   private Map<Integer, Index> createSpriteIndices() {
      Map<Integer, Index> indices = new HashMap<Integer, Index>();
      indices.put(NPC.PRIEST_MALE.ordinal(), new Index(0, 0));
      indices.put(NPC.SAGE_FEMALE.ordinal(), new Index(3, 0));
      indices.put(NPC.CLERIC_MALE.ordinal(), new Index(6, 0));
      indices.put(NPC.DANCER_FEMALE.ordinal(), new Index(9, 0));
      indices.put(NPC.CITIZEN_MALE.ordinal(), new Index(0, 4));
      indices.put(NPC.DANCER_FEMALE_ALT.ordinal(), new Index(3, 4));
      indices.put(NPC.EXPLORER_MALE.ordinal(), new Index(6, 4));
      indices.put(NPC.EXPLORER_FEMALE.ordinal(), new Index(9, 4));
      return indices;
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