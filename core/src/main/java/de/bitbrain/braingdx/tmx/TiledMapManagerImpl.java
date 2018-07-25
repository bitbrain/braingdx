/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.ai.pathfinding.AStarPathFinder;
import de.bitbrain.braingdx.ai.pathfinding.PathFinder;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.braingdx.world.SimpleWorldBounds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@link TiledMapManager}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class TiledMapManagerImpl implements TiledMapManager {

   private final BehaviorManager behaviorManager;
   private final GameWorld gameWorld;
   private final GameObjectRenderManager renderManager;
   private final TiledMapAPIImpl api;
   private final GameObjectUpdater gameObjectUpdater;
   private final State state;
   private final StatePopulator populator;
   private final Map<TiledMapType, MapLayerRendererFactory> factories;
   private final AStarPathFinder pathFinder;
   private final GameEventManager gameEventManager;
   private final GameEventRouter router;

   public TiledMapManagerImpl(BehaviorManager behaviorManager, GameWorld gameWorld,
                              GameObjectRenderManager renderManager, GameEventManager gameEventManager) {
      this.gameEventManager = gameEventManager;
      this.behaviorManager = behaviorManager;
      this.gameWorld = gameWorld;
      this.renderManager = renderManager;
      this.state = new State();
      this.router = new GameEventRouter(gameEventManager, gameWorld);
      this.api = new TiledMapAPIImpl(state, gameWorld, router, gameEventManager);
      this.populator = new StatePopulator(renderManager, gameWorld, api, behaviorManager, gameEventManager);
      this.gameObjectUpdater = new GameObjectUpdater(api, state, gameEventManager);
      this.factories = createFactories();
      this.pathFinder = new AStarPathFinder(api, 100, false);
   }

   @Override
   public void load(TiledMap tiledMap, Camera camera, TiledMapType type, TiledMapConfig config)
         throws TiledMapException {
      clear();
      validate(tiledMap);
      gameEventManager.publish(new TiledMapEvents.BeforeLoadEvent(tiledMap));
      behaviorManager.apply(gameObjectUpdater);
      behaviorManager.apply(router);
      populator.populate(tiledMap, state, camera, factories.get(type), config);
      gameWorld.setBounds(new SimpleWorldBounds(api.getWorldWidth(), api.getWorldHeight()));
      gameEventManager.publish(new TiledMapEvents.AfterLoadEvent(tiledMap, api));
      pathFinder.refresh();
   }

   @Override
   public void load(TiledMap tiledMap, Camera camera, TiledMapType type) throws TiledMapException {
      this.load(tiledMap, camera, type, new TiledMapConfig());
   }

   @Override
   public void dispose() {
      clear();
   }

   @Override
   public TiledMapAPI getAPI() {
      return api;
   }

   private void clear() {
      gameEventManager.publish(new TiledMapEvents.BeforeUnloadEvent(api));
      behaviorManager.remove(gameObjectUpdater);
      gameWorld.clear();
      for (String id : state.getLayerIds()) {
         renderManager.unregister(id);
      }
      state.clear();
      gameEventManager.publish(new TiledMapEvents.AfterUnloadEvent());
   }

   protected Map<TiledMapType, MapLayerRendererFactory> createFactories() {
      Map<TiledMapType, MapLayerRendererFactory> factories = new HashMap<TiledMapType, MapLayerRendererFactory>();
      factories.put(TiledMapType.ORTHOGONAL, new OrthogonalMapLayerRendererFactory());
      return factories;
   }

   private void validate(TiledMap map) throws TiledMapException {
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
   }

   @Override
   public PathFinder getPathFinder() {
      return this.pathFinder;
   }

}
