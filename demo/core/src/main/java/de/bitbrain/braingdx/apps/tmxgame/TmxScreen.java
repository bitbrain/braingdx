package de.bitbrain.braingdx.apps.tmxgame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.apps.Assets.Sounds;
import de.bitbrain.braingdx.apps.rpg.NPC;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationFactory;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationFactory.Index;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimationSupplier;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.renderer.AnimationRenderer;
import de.bitbrain.braingdx.input.OrientationMovementController;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.tmx.TiledMapType;
import de.bitbrain.braingdx.world.GameObject;

public class TmxScreen extends AbstractScreen<TmxTest> {

   private GameObject player;

   public TmxScreen(TmxTest game) {
      super(game);
   }

   @Override
   protected void onCreate(GameContext context) {
	  context.getGameCamera().setBaseZoom(0.15f);
	  context.getGameCamera().setSpeed(3.6f);
	  context.getGameCamera().setZoomScale(0.001f);
      TiledMap map = SharedAssetManager.getInstance().get(Assets.RPG.MAP_2, TiledMap.class);
      TiledMapManager tiledMapManager = context.getTiledMapManager();
      context.getLightingManager().setAmbientLight(new Color(0.2f, 0.3f, 0.6f, 0.4f));
      tiledMapManager.getAPI().setDebug(true);
      tiledMapManager.load(map, context.getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);

      player = null;
      for (GameObject o : context.getGameWorld()) {
    	  context.getBehaviorManager().apply(new PointLightBehavior(new Color(1f, 0.7f, 0.7f, 1f), 190f, context.getLightingManager()),
               o);
         if (o.getType().equals("CLERIC_MALE")) {
            player = o;
         }
      }
      context.getGameCamera().setTarget(player);

      final Texture texture = SharedAssetManager.getInstance().get(Assets.RPG.CHARACTER_TILESET);
      SpriteSheet sheet = new SpriteSheet(texture, 12, 8);
      createAnimations(context, sheet);

      OrientationMovementController controller = new OrientationMovementController();
      RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, tiledMapManager.getAPI())
            .interval(0.3f)
            .rasterSize(tiledMapManager.getAPI().getCellWidth(), tiledMapManager.getAPI().getCellHeight());
      context.getBehaviorManager().apply(behavior, player);
      
      context.getAudioManager().spawnSoundLooped(Sounds.SOUND_TEST, 500, 500, 1f, 1f, 700f);
   }
   
   @Override
	protected Viewport getViewport(int width, int height) {
		return new FillViewport(width, height);
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
         context.getRenderManager().register(NPC.values()[type].name(), new AnimationRenderer(supplier).scale(1f, 1.3f));
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

}
