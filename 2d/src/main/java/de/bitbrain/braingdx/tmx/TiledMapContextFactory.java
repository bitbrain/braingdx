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
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.GdxRuntimeException;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.physics.PhysicsManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import static de.bitbrain.braingdx.physics.PhysicsBodyFactory.*;

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
   private final PhysicsManager physicsManager;

   public TiledMapContextFactory(
         GameObjectRenderManager renderManager,
         GameWorld gameWorld,
         GameEventManager gameEventManager,
         GameEventRouter gameEventRouter,
         BehaviorManager behaviorManager,
         PhysicsManager physicsManager) {
      this.renderManager = renderManager;
      this.gameWorld = gameWorld;
      this.gameEventManager = gameEventManager;
      this.gameEventRouter = gameEventRouter;
      this.behaviorManager = behaviorManager;
      this.physicsManager = physicsManager;
   }

   public TiledMapContextImpl createContext(
         TiledMap tiledMap,
         Camera camera,
         MapLayerRendererFactory rendererFactory,
         PositionTranslatorFactory positionTranslatorFactory,
         TiledMapConfig config) {
      State state = new State();
      PositionTranslator positionTranslator = positionTranslatorFactory.create(state);
      TiledMapContextImpl context = new TiledMapContextImpl(
            tiledMap,
            state,
            gameWorld,
            gameEventRouter,
            renderManager,
            behaviorManager,
            gameEventManager,
            positionTranslator
      );
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
            handleObjectLayer(context, lastLayerId, lastTileLayerIndex, mapLayer, state, config, positionTranslator);
         }
      }

      // Add debug layer
      layerIds.add(handleDebugTileLayer(context, state, camera, rendererFactory));
      state.setLayerIds(layerIds);

      return context;
   }

   private void handleMapProperties(MapProperties properties, State state, TiledMapConfig config) {
      state.setIndexDimensions(properties.get(config.get(Constants.WIDTH), Integer.class),
            properties.get(config.get(Constants.HEIGHT), Integer.class));
   }

   private void handleObjectLayer(TiledMapContext context, String lastLayerId, int layerIndex, MapLayer layer, State state, TiledMapConfig config, PositionTranslator positionTranslator) {
      if (layerIndex < 0) {
         throw new GdxRuntimeException("Unable to load tiled map! At least a single tiled layer is required!");
      }
      MapObjects mapObjects = layer.getObjects();
      for (int objectIndex = 0; objectIndex < mapObjects.getCount(); ++objectIndex) {
         MapObject mapObject = mapObjects.get(objectIndex);
         MapProperties objectProperties = mapObject.getProperties();
         if (objectProperties.get("physics.enabled", false, Boolean.class)) {
            handlePhysicsObject(mapObject, objectProperties);
         } else {
            handleGameObject(context, lastLayerId, layerIndex, state, config, positionTranslator, mapObject, objectProperties);
         }
      }
   }

   private void handlePhysicsObject(MapObject mapObject, MapProperties objectProperties) {
      List<Shape> shapes = new ArrayList<Shape>();
      if (mapObject instanceof RectangleMapObject) {
         shapes.add(getRectangle(((RectangleMapObject) mapObject).getRectangle()));
      } else if (mapObject instanceof PolygonMapObject) {
         shapes.addAll(getPolygons(((PolygonMapObject) mapObject).getPolygon()));
      } else if (mapObject instanceof PolylineMapObject) {
         shapes.add(getPolyline(((PolylineMapObject) mapObject).getPolyline()));
      } else if (mapObject instanceof CircleMapObject) {
         shapes.add(getCircle(((CircleMapObject) mapObject).getCircle()));
      } else {
         throw new GdxRuntimeException("Shape of " + mapObject + " not supported by braingdx");
      }
      BodyDef bd = new BodyDef();
      bd.position.set(objectProperties.get(Constants.X, 0f, Float.class), objectProperties.get(Constants.Y, 0f, Float.class));
      bd.type = BodyDef.BodyType.StaticBody;
      Body body = physicsManager.getPhysicsWorld().createBody(bd);
      for (Shape shape : shapes) {
         body.createFixture(shape, 1);
         shape.dispose();
      }
   }

   private void handleGameObject(TiledMapContext context, String lastLayerId, int layerIndex, State state, TiledMapConfig config, PositionTranslator positionTranslator, MapObject mapObject, MapProperties objectProperties) {
      GameObject gameObject = gameWorld.addObject(lastLayerId);
      final float mapX = objectProperties.get(config.get(Constants.X), Float.class);
      final float mapY = objectProperties.get(config.get(Constants.Y), Float.class);
      final Vector2 worldPos = positionTranslator.toWorld(mapX, mapY);
      final float worldX = worldPos.x;
      final float worldY = worldPos.y;
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
         final int xIndex = positionTranslator.toIndexX(worldX);
         final int yIndex = positionTranslator.toIndexY(worldY);
         final float newMapX = xIndex * state.getCellWidth();
         final float newMapY = yIndex * state.getCellHeight();
         final Vector2 newWorldPos = positionTranslator.toWorld(newMapX, newMapY);
         gameObject.setPosition(newWorldPos.x, newWorldPos.y);
      } else {
         gameObject.setPosition(worldX, worldY);
      }
      gameObject.setDimensions(
            calculateIndexedDimension(w, cellWidth),
            calculateIndexedDimension(h, cellHeight)
      );
      Color color = objectProperties.get(config.get(Constants.COLOR), mapObject.getColor(), Color.class);
      gameObject.setLastPosition(gameObject.getLeft(), gameObject.getTop());
      gameObject.setColor(color);
      gameObject.setType(objectType);
      gameObject.setAttribute(Constants.LAYER_INDEX, layerIndex);

      // [#205] Load attributes directly into game object
      Iterator<String> objectKeyIterator = objectProperties.getKeys();
      while (objectKeyIterator.hasNext()) {
         String key = objectKeyIterator.next();
         gameObject.setAttribute(key, objectProperties.get(key));
      }

      if (!context.isInclusiveCollision(gameObject.getLeft(), gameObject.getTop(), layerIndex, gameObject)) {
         CollisionCalculator.updateCollision(
               positionTranslator,
               gameObject,
               collision,
               gameObject.getLeft(),
               gameObject.getTop(),
               layerIndex,
               state
         );
      }
      gameEventManager.publish(new TiledMapEvents.OnLoadGameObjectEvent(gameObject, context));
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
      layerObject.setZIndex(ZIndexCalculator.calculateZIndex(numberOfRows, numberOfRows, index));
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
         heightMap[x][y] = ZIndexCalculator.calculateZIndex(state.getMapIndexHeight(), y, layerIndex);
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

   public static float calculateIndexedDimension(float size, float cellSize) {
      return (float) (Math.ceil(size / cellSize) * cellSize);
   }
}
