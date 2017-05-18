package de.bitbrain.braingdx.apps.tmxgame;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bitbrain.braingdx.apps.Assets;
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
   protected void onCreateStage(Stage stage, int width, int height) {
      getGameCamera().setBaseZoom(0.15f);
      getGameCamera().setSpeed(3.6f);
      getGameCamera().setZoomScale(0.001f);
      TiledMap map = SharedAssetManager.getInstance().get(Assets.RPG.MAP_2, TiledMap.class);
      TiledMapManager tiledMapManager = getTiledMapManager();
      getLightingManager().setAmbientLight(new Color(0.2f, 0.3f, 0.6f, 0.4f));
      tiledMapManager.load(map, getGameCamera().getInternal(), TiledMapType.ORTHOGONAL);

      player = null;
      for (GameObject o : getGameWorld()) {
         getBehaviorManager().apply(new PointLightBehavior(new Color(1f, 0.7f, 0.7f, 1f), 190f, getLightingManager()),
               o);
         if (o.getType().equals("CLERIC_MALE")) {
            player = o;
         }
      }
      getGameCamera().setTarget(player);

      final Texture texture = SharedAssetManager.getInstance().get(Assets.RPG.CHARACTER_TILESET);
      SpriteSheet sheet = new SpriteSheet(texture, 12, 8);
      createAnimations(sheet);

      OrientationMovementController controller = new OrientationMovementController();
      RasteredMovementBehavior behavior = new RasteredMovementBehavior(controller, tiledMapManager.getAPI())
            .interval(0.3f)
            .rasterSize(tiledMapManager.getAPI().getCellWidth(), tiledMapManager.getAPI().getCellHeight());
      getBehaviorManager().apply(behavior, player);
   }

   private void createAnimations(SpriteSheet sheet) {
      Map<Integer, Index> indices = createSpriteIndices();
      SpriteSheetAnimationFactory animationFactory = new SpriteSheetAnimationFactory(sheet, indices);
      Map<Integer, SpriteSheetAnimation> animations = new HashMap<Integer, SpriteSheetAnimation>();
      for (int type : indices.keySet()) {
         SpriteSheetAnimation animation = animationFactory.create(type);
         animations.put(type, animation);
         SpriteSheetAnimationSupplier supplier = new SpriteSheetAnimationSupplier(orientations(), animation,
               AnimationTypes.FORWARD_YOYO);
         getBehaviorManager().apply(supplier);
         getRenderManager().register(NPC.values()[type].name(), new AnimationRenderer(supplier).scale(1f, 1.3f));
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
