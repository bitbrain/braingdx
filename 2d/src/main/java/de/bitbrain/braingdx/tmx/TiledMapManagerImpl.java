package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.util.Factory;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.braingdx.world.SimpleWorldBounds;

import java.util.HashMap;
import java.util.Map;

public class TiledMapManagerImpl implements TiledMapManager, Disposable {

   private final GameWorld gameWorld;
   private final GameEventManager gameEventManager;
   private final TiledMapContextFactory contextFactory;
   private final Map<TiledMap, TiledMapContext> contextMap = new HashMap<TiledMap, TiledMapContext>();
   private final Map<TiledMapType, MapLayerRendererFactory> rendererFactoryMap;
   private final Map<TiledMapType, PositionTranslatorFactory> positionTranslatorFactoryMap;

   public TiledMapManagerImpl(
         GameWorld gameWorld,
         GameEventManager gameEventManager,
         TiledMapContextFactory tiledMapContextFactory) {
      this.gameWorld = gameWorld;
      this.gameEventManager = gameEventManager;
      this.contextFactory = tiledMapContextFactory;
      this.rendererFactoryMap = createRendererFactories();
      this.positionTranslatorFactoryMap = createPositionTranslators();
   }

   @Override
   public TiledMapContext load(
         TiledMap tiledMap,
         Camera camera) throws TiledMapException {
      return load(tiledMap, camera, new TiledMapConfig());
   }

   @Override
   public TiledMapContext load(TiledMap tiledMap, Camera camera, TiledMapConfig config) {
      validate(tiledMap);
      gameEventManager.publish(new TiledMapEvents.BeforeLoadEvent(tiledMap));
      TiledMapType type = TiledMapType.fromOrientation(
            tiledMap.getProperties().get(Constants.ORIENTATION, String.class)
      );

      TiledMapContextImpl context = contextFactory.createContext(
            tiledMap,
            camera,
            rendererFactoryMap.get(type),
            positionTranslatorFactoryMap.get(type),
            config
      );
      gameWorld.setBounds(new SimpleWorldBounds(
            context.getWorldWidth(),
            context.getWorldHeight()
      ));
      gameEventManager.publish(new TiledMapEvents.AfterLoadEvent(tiledMap, context));
      contextMap.put(tiledMap, context);
      return context;
   }

   @Override
   public void unload(TiledMapContext context) {
      if (!contextMap.containsValue(context)) {
         throw new TiledMapException("Invalid context provided! Already unloaded.");
      }
      gameEventManager.publish(new TiledMapEvents.BeforeUnloadEvent(context));
      context.dispose();
      gameEventManager.publish(new TiledMapEvents.AfterUnloadEvent());
      contextMap.remove(context.getTiledMap());
   }

   @Override
   public void dispose() {
      for (TiledMapContext context : contextMap.values()) {
         context.dispose();
      }
      contextMap.clear();
   }

   protected Map<TiledMapType, MapLayerRendererFactory> createRendererFactories() {
      Map<TiledMapType, MapLayerRendererFactory> factories = new HashMap<TiledMapType, MapLayerRendererFactory>();
      factories.put(TiledMapType.ORTHOGONAL, new OrthogonalMapLayerRendererFactory());
      factories.put(TiledMapType.ISOMETRIC, new IsometricMapLayerRendererFactory());
      return factories;
   }

   protected Map<TiledMapType, PositionTranslatorFactory> createPositionTranslators() {
      Map<TiledMapType, PositionTranslatorFactory> factories = new HashMap<TiledMapType, PositionTranslatorFactory>();
      factories.put(TiledMapType.ORTHOGONAL, new PositionTranslatorFactory() {
         @Override
         public PositionTranslator create(State state) {
            return new OrthogonalPositionTranslator(state);
         }
      });
      factories.put(TiledMapType.ISOMETRIC, new PositionTranslatorFactory() {
         @Override
         public PositionTranslator create(State state) {
            return new IsometricPositionTranslator(state);
         }
      });
      return factories;
   }

   private void validate(TiledMap map) throws TiledMapException {
      if (contextMap.containsKey(map)) {
         throw new TiledMapException("TiledMap already loaded. Unload first!");
      }
      MapProperties properties = map.getProperties();
      if (properties.get(Constants.WIDTH) == null) {
         throw new TiledMapException("Map has no width specified");
      }
      if (properties.get(Constants.HEIGHT) == null) {
         throw new TiledMapException("Map has no width specified");
      }
      if (properties.get(Constants.WIDTH, int.class) <= 0f) {
         throw new TiledMapException("Map width must be larger than 0");
      }
      if (properties.get(Constants.HEIGHT, int.class) <= 0f) {
         throw new TiledMapException("Map height must be larger than 0");
      }
      if (properties.get(Constants.ORIENTATION) == null) {
         throw new TiledMapException("Map has no orientation specified");
      }
   }
}
