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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.movement.MovementController;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.tmx.State.CellState;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Extracts {@link GameObject} instances from a {@link TiledMap} provided.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class StatePopulator {

   private static final boolean DEFAULT_COLLISION = false;

   private final GameObjectRenderManager renderManager;
   private final GameWorld gameWorld;
   private final TiledMapAPI api;
   private final List<TiledMapListener> listeners;
   private final BehaviorManager behaviorManager;

   public StatePopulator(GameObjectRenderManager renderManager, GameWorld gameWorld, TiledMapAPI api,
         BehaviorManager behaviorManager, List<TiledMapListener> listeners) {
      this.renderManager = renderManager;
      this.gameWorld = gameWorld;
      this.api = api;
      this.behaviorManager = behaviorManager;
      this.listeners = listeners;
   }

   public void populate(TiledMap tiledMap, State state, Camera camera, MapLayerRendererFactory rendererFactory,
         TiledMapConfig config) {
      MapLayers mapLayers = tiledMap.getLayers();
      handleMapProperties(tiledMap.getProperties(), state, config);
      List<String> layerIds = new ArrayList<String>();
      int lastTileLayerIndex = 0;
      for (int i = 0; i < mapLayers.getCount(); ++i) {
         MapLayer mapLayer = mapLayers.get(i);
         if (mapLayer instanceof TiledMapTileLayer) {
            if (i > 0) {
               lastTileLayerIndex++;
            }
            String layerId = handleTiledMapTileLayer((TiledMapTileLayer) mapLayer, i, tiledMap, camera, rendererFactory,
                  config);
            layerIds.add(layerId);
            populateStaticMapData(lastTileLayerIndex, (TiledMapTileLayer) mapLayer, state, config);
         } else {
            // Not a tiledlayer so consider it as an object layer
            handleObjectLayer(lastTileLayerIndex, mapLayer, state, config);
         }
      }
      state.setLayerIds(layerIds);
      
      // Add debug layer
      handleDebugTileLayer(state, camera, rendererFactory, config);
   }

   private void handleMapProperties(MapProperties properties, State state, TiledMapConfig config) {
      state.setIndexDimensions(properties.get(config.get(Constants.WIDTH), Integer.class),
            properties.get(config.get(Constants.HEIGHT), Integer.class));
   }

   private void handleObjectLayer(int layerIndex, MapLayer layer, State state, TiledMapConfig config) {
      MapObjects mapObjects = layer.getObjects();
      for (int objectIndex = 0; objectIndex < mapObjects.getCount(); ++objectIndex) {
         MapObject mapObject = mapObjects.get(objectIndex);
         MapProperties objectProperties = mapObject.getProperties();
         GameObject gameObject = gameWorld.addObject();
         final float x = objectProperties.get(config.get(Constants.X), Float.class);
         final float y = objectProperties.get(config.get(Constants.Y), Float.class);
         final float w = objectProperties.get(config.get(Constants.WIDTH), Float.class);
         final float h = objectProperties.get(config.get(Constants.HEIGHT), Float.class);
         final float cellWidth = state.getCellWidth();
         final float cellHeight = state.getCellHeight();
         Object objectType = objectProperties.get(config.get(Constants.TYPE));
         boolean collision = objectProperties.get(config.get(Constants.COLLISION), false, Boolean.class);
         gameObject.setPosition(x, y);
         gameObject.setDimensions(IndexCalculator.calculateIndexedDimension(w, cellWidth),
               IndexCalculator.calculateIndexedDimension(h, cellHeight));
         Color color = objectProperties.get(config.get(Constants.COLOR), mapObject.getColor(), Color.class);
         gameObject.setLastPosition(x, y);
         gameObject.setColor(color);
         gameObject.setType(objectType);
         gameObject.setAttribute(Constants.LAYER_INDEX, layerIndex);
         gameObject.setAttribute(MapProperties.class, objectProperties);
         if (objectProperties.containsKey(config.get(Constants.MOVEMENT))) {
            String className = objectProperties.get(config.get(Constants.MOVEMENT), String.class);
            RasteredMovementBehavior behavior = createMovementBehavior(className);
            if (behavior != null) {
               behaviorManager.apply(behavior, gameObject);
            }
         }
         CollisionCalculator.updateCollision(collision, x, y, layerIndex, state);
         IndexCalculator.calculateZIndex(gameObject, state, layerIndex);
         for (TiledMapListener listener : listeners) {
            listener.onLoadGameObject(gameObject, api);
         }
      }
   }

   private RasteredMovementBehavior createMovementBehavior(String className) {
      try {
         Class<?> movementClass = Class.forName(className, true, getClass().getClassLoader());
         @SuppressWarnings("unchecked")
         MovementController<Orientation> controller = (MovementController<Orientation>) movementClass.newInstance();
         return new RasteredMovementBehavior(controller, api).rasterSize(api.getCellWidth(), api.getCellHeight());
      } catch (InstantiationException e) {
         e.printStackTrace();
         return null;
      } catch (IllegalAccessException e) {
         e.printStackTrace();
         return null;
      } catch (ClassNotFoundException e) {
         e.printStackTrace();
         return null;
      }
   }
   
   private String handleDebugTileLayer(State state, Camera camera,
         MapLayerRendererFactory rendererFactory, TiledMapConfig config) {
      GameObjectRenderer renderer = rendererFactory.createDebug(api, state, camera);
      String id = UUID.randomUUID().toString();
      renderManager.register(id, renderer);
      GameObject layerObject = gameWorld.addObject();
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
      GameObject layerObject = gameWorld.addObject();
      layerObject.setActive(false);
      layerObject.setPersistent(true);
      layerObject.setType(id);
      layerObject.setZIndex(IndexCalculator.calculateZIndex(numberOfRows, numberOfRows, index));
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
      boolean collisionLayer = Boolean
            .valueOf(layerProperties.get(config.get(Constants.COLLISION), "false", String.class));
      CellState cellState = state.getState(x, y, layerIndex);
      // Inherit the collision from the previous layer, if and only if
      // the current layer is non-collision by default
      if (layerIndex > 0 && !collisionLayer && state.getState(x, y, layerIndex - 1).isCollision()) {
         cellState.setCollision(true);
      } else if (cell != null) {
         TiledMapTile tile = cell.getTile();
         if (tile != null) {
            MapProperties properties = tile.getProperties();
            cellState.setProperties(properties);
            if (properties.containsKey(Constants.COLLISION)) {
               boolean collision = Boolean.valueOf(properties.get(Constants.COLLISION, String.class));
               cellState.setCollision(collision);
            } else {
               cellState.setCollision(DEFAULT_COLLISION);
            }
         } else {
            cellState.setCollision(DEFAULT_COLLISION);
         }
      } else {
         cellState.setCollision(DEFAULT_COLLISION);
      }
   }
}
