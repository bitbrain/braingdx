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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Extracts {@link GameObject} instances from a {@link TiledMap} provided.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class TiledMapContextFactory {

   private final GameObjectRenderManager renderManager;
   private final GameWorld gameWorld;
   private final GameEventManager gameEventManager;
   private final GameEventRouter gameEventRouter;
   private final BehaviorManager behaviorManager;

   public TiledMapContextFactory(
         GameObjectRenderManager renderManager,
         GameWorld gameWorld,
         GameEventManager gameEventManager,
         GameEventRouter gameEventRouter,
         BehaviorManager behaviorManager) {
      this.renderManager = renderManager;
      this.gameWorld = gameWorld;
      this.gameEventManager = gameEventManager;
      this.gameEventRouter = gameEventRouter;
      this.behaviorManager = behaviorManager;
   }

   public TiledMapContextImpl createContext(
         TiledMap tiledMap,
         Camera camera,
         MapLayerRendererFactory rendererFactory,
         TiledMapConfig config) {
      State state = new State();
      TiledMapContextImpl context = new TiledMapContextImpl(tiledMap, state, gameWorld, gameEventRouter, renderManager);
      MapLayers mapLayers = tiledMap.getLayers();
      state.setNumberOfLayers(mapLayers.getCount());
      handleMapProperties(tiledMap.getProperties(), state, config);
      List<String> layerIds = new ArrayList<String>();
      int lastTileLayerIndex = -1;
      String lastLayerId = null;
      for (int i = 0; i < mapLayers.getCount(); ++i) {
         MapLayer mapLayer = mapLayers.get(i);
         if (mapLayer instanceof TiledMapTileLayer) {
            lastTileLayerIndex++;
            lastLayerId = handleTiledMapTileLayer((TiledMapTileLayer) mapLayer, i, tiledMap, camera, rendererFactory,
                  config);
            layerIds.add(lastLayerId);
            populateStaticMapData(lastTileLayerIndex, (TiledMapTileLayer) mapLayer, state, config);
         } else {
            // Not a tiledlayer so consider it as an object layer
            handleObjectLayer(context, lastLayerId, lastTileLayerIndex, mapLayer, state, config);
         }
      }

      // Add debug layer
      layerIds.add(handleDebugTileLayer(context, state, camera, rendererFactory));
      state.setLayerIds(layerIds);

      // TODO Move stuff to context since this needs to be disposed
      // Apply behaviours
      behaviorManager.apply(new GameObjectUpdater(context, state, gameEventManager));

      return context;
   }

   private void handleMapProperties(MapProperties properties, State state, TiledMapConfig config) {
      state.setIndexDimensions(properties.get(config.get(Constants.WIDTH), Integer.class),
            properties.get(config.get(Constants.HEIGHT), Integer.class));
   }

   private void handleObjectLayer(TiledMapContext context, String lastLayerId, int layerIndex, MapLayer layer, State state, TiledMapConfig config) {
      if (layerIndex < 0) {
         throw new GdxRuntimeException("Unable to load tiled map! At least a single tiled layer is required!");
      }
      MapObjects mapObjects = layer.getObjects();
      for (int objectIndex = 0; objectIndex < mapObjects.getCount(); ++objectIndex) {
         MapObject mapObject = mapObjects.get(objectIndex);
         MapProperties objectProperties = mapObject.getProperties();
         GameObject gameObject = gameWorld.addObject(lastLayerId);
         final float x = objectProperties.get(config.get(Constants.X), Float.class);
         final float y = objectProperties.get(config.get(Constants.Y), Float.class);
         final float w = objectProperties.get(config.get(Constants.WIDTH), Float.class);
         final float h = objectProperties.get(config.get(Constants.HEIGHT), Float.class);
         final float cellWidth = state.getCellWidth();
         final float cellHeight = state.getCellHeight();
         Object objectType = objectProperties.get(config.get(Constants.TYPE));

         Object collisionObject = objectProperties.get(config.get(Constants.COLLISION), "false", Object.class);
         boolean collision = false;
         if (collisionObject instanceof Boolean) {
            collision = (Boolean) collisionObject;
         } else if (collisionObject instanceof String) {
            collision = Boolean.valueOf((String) collisionObject);
         }

         // issue #135 - correct positions of game objects with a collision
         if (collision) {
            gameObject.setPosition(
                  IndexCalculator.calculateIndex(x, state.getCellWidth()) * state.getCellWidth(),
                  IndexCalculator.calculateIndex(y, state.getCellHeight()) * state.getCellHeight()
            );
         } else {
            gameObject.setPosition(x, y);
         }
         gameObject.setDimensions(IndexCalculator.calculateIndexedDimension(w, cellWidth),
               IndexCalculator.calculateIndexedDimension(h, cellHeight));
         Color color = objectProperties.get(config.get(Constants.COLOR), mapObject.getColor(), Color.class);
         gameObject.setLastPosition(x, y);
         gameObject.setColor(color);
         gameObject.setType(objectType);
         gameObject.setAttribute(Constants.LAYER_INDEX, layerIndex);
         gameObject.setAttribute(MapProperties.class, objectProperties);
         if (!context.isInclusiveCollision(x, y, layerIndex, gameObject)) {
            CollisionCalculator.updateCollision(gameObject, collision, x, y, layerIndex, state);
         }
         gameEventManager.publish(new TiledMapEvents.OnLoadGameObjectEvent(gameObject, context));
      }
   }

   private String handleDebugTileLayer(TiledMapContext context, State state, Camera camera,
                                       MapLayerRendererFactory rendererFactory) {
      GameObjectRenderer renderer = rendererFactory.createDebug(context, state, camera);
      String id = UUID.randomUUID().toString();
      renderManager.register(id, renderer);
      GameObject layerObject = gameWorld.addObject(id);
      layerObject.setActive(false);
      layerObject.setPersistent(true);
      layerObject.setType(id);
      // Over the top
      layerObject.setZIndex(Integer.MAX_VALUE);
      return id;
   }

   private String handleTiledMapTileLayer(TiledMapTileLayer layer, int index, TiledMap tiledMap, Camera camera,
                                          MapLayerRendererFactory rendererFactory, TiledMapConfig config) {
      final int numberOfRows = tiledMap.getProperties().get(config.get(Constants.HEIGHT), Integer.class);
      GameObjectRenderer renderer = rendererFactory.create(index, tiledMap, camera);
      String id = UUID.randomUUID().toString();
      renderManager.register(id, renderer);
      GameObject layerObject = gameWorld.addObject(id);
      layerObject.setActive(false);
      layerObject.setPersistent(true);
      layerObject.setType(id);
      layerObject.setZIndex(IndexCalculator.calculateZIndex(numberOfRows, numberOfRows, index));
      if (!layer.isVisible()) {
         layerObject.getColor().a = 0f;
      }
      return id;
   }

   private void populateStaticMapData(int layerIndex, TiledMapTileLayer layer, State state, TiledMapConfig config) {
      Integer[][] heightMap = state.getHeightMap();
      if (heightMap == null) {
         heightMap = new Integer[state.getMapIndexWidth()][state.getMapIndexHeight()];
         state.setHeightMap(heightMap);
      }
      state.setCellWidth(layer.getTileWidth());
      state.setCellHeight(layer.getTileHeight());
      for (int x = 0; x < heightMap.length; ++x) {
         for (int y = 0; y < heightMap[x].length; ++y) {
            populateHeightMap(x, y, state, layerIndex, layer);
            populateStateMap(x, y, state, layerIndex, layer, config);
         }
      }
   }

   private void populateHeightMap(int x, int y, State state, int layerIndex, TiledMapTileLayer layer) {
      Cell cell = layer.getCell(x, y);
      if (cell != null) {
         Integer[][] heightMap = state.getHeightMap();
         heightMap[x][y] = IndexCalculator.calculateZIndex(state.getMapIndexHeight(), y, layerIndex);
      }
   }

   private void populateStateMap(int x, int y, State state, int layerIndex, TiledMapTileLayer layer,
                                 TiledMapConfig config) {
      Cell cell = layer.getCell(x, y);
      MapProperties layerProperties = layer.getProperties();
      Object collisionObject = layerProperties.get(config.get(Constants.COLLISION), "false", Object.class);
      boolean collisionLayer = false;
      if (collisionObject instanceof Boolean) {
         collisionLayer = (Boolean) collisionObject;
      } else if (collisionObject instanceof String) {
         collisionLayer = Boolean.valueOf((String) collisionObject);
      }
      // Inherit the collision from the previous layer, if and only if
      // the current layer is non-collision by default
      boolean isCollision = layerIndex > 0 && !collisionLayer && state.getState(x, y, layerIndex - 1).isCollision();
      if (isCollision) {
         State.CellState cellState = state.getState(x, y, layerIndex);
         cellState.setCollision(true);
      } else if (cell != null) {
         TiledMapTile tile = cell.getTile();
         if (tile != null) {
            MapProperties properties = tile.getProperties();
            if (!properties.getKeys().hasNext()) {
               return;
            }
            State.CellState cellState = state.getState(x, y, layerIndex);
            cellState.setProperties(properties);
            if (properties.containsKey(Constants.COLLISION)) {
               Object collisionProperty = properties.get(Constants.COLLISION);
               boolean collision = false;
               if (collisionProperty instanceof Boolean) {
                  collision = (Boolean) collisionProperty;
               } else if (collisionProperty instanceof String) {
                  collision = Boolean.valueOf(collisionProperty.toString());
               }
               cellState.setCollision(collision);
            }
         }
      }
   }
}
