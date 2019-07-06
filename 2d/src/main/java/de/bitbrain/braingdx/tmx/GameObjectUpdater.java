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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.util.Factory;
import de.bitbrain.braingdx.world.GameObject;

import static de.bitbrain.braingdx.tmx.IndexCalculator.calculateIndex;

/**
 * This component updates game objects which are part of the tiledmap lifecycle.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
class GameObjectUpdater extends BehaviorAdapter {

   private static final String FALSE = "false";

   private class IndexFactory implements Factory<Integer> {

      float value;
      float cellSize;

      public IndexFactory() {

      }

      @Override
      public Integer create() {
         return calculateIndex(value, cellSize);
      }
   }

   private class LayerIndexFactory implements Factory<Integer> {

      GameObject object;

      @Override
      public Integer create() {
         return context.lastLayerIndexOf(object);
      }
   }

   private final IndexFactory indexFactory = new IndexFactory();

   private final LayerIndexFactory layerIndexFactory = new LayerIndexFactory();

   private final TiledMapContext context;

   private final State state;

   private final Vector2 currentPosition = new Vector2();

   private final GameEventManager gameEventManager;

   public GameObjectUpdater(TiledMapContext context, State state, GameEventManager gameEventManager) {
      this.context = context;
      this.state = state;
      this.gameEventManager = gameEventManager;
   }

   @Override
   public void update(GameObject object, float delta) {
      if (object.isActive()) {
         updateZIndex(object);
         updateCollision(object);
         updateLayerIndex(object);
         // Update object state
         object.setPosition(object.getLeft(), object.getTop());
         object.setAttribute(Constants.LAST_LAYER_INDEX, context.lastLayerIndexOf(object));
      }
   }

   private void updateZIndex(GameObject object) {
      int currentLayerIndex = context.layerIndexOf(object);
      object.setZIndex(IndexCalculator.calculateZIndex(object, context, currentLayerIndex));
   }

   private void updateCollision(GameObject object) {
      // Verify that object is valid for collisions
      if (object.hasAttribute(MapProperties.class)) {
         MapProperties mapProperties = (MapProperties) object.getAttribute(MapProperties.class);
         // Do not update objects which have a false collision
         if (mapProperties.containsKey(Constants.COLLISION)) {
            Object collision = mapProperties.get(Constants.COLLISION);
            if (collision.equals(Boolean.FALSE)) {
               return;
            }
            if (collision.equals(FALSE)) {
               return;
            }
         }
      }
      // Remove last collision if object has moved
      // and last position is not occupied
      Vector2 lastPosition = object.getLastPosition();
      currentPosition.set(object.getLeft(), object.getTop());
      layerIndexFactory.object = object;
      int lastLayerIndex = object.getOrSetAttribute(Constants.LAST_LAYER_INDEX, layerIndexFactory);
      int currentLayerIndex = context.layerIndexOf(object);
      if (lastLayerIndex != currentLayerIndex || !currentPosition.equals(lastPosition)) {
         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("TiledMapAPI", "Updating collision of " + object);
         }
         // Object has moved, now check if last position is already occupied
         indexFactory.value = lastPosition.x;
         indexFactory.cellSize = context.getCellWidth();
         int lastTileX = object.getOrSetAttribute(Constants.LAST_TILE_X, indexFactory);
         indexFactory.value = lastPosition.y;
         indexFactory.cellSize = context.getCellHeight();
         int lastTileY = object.getOrSetAttribute(Constants.LAST_TILE_Y, indexFactory);
         GameObject occupant = context.getGameObjectAt(lastTileX, lastTileY, lastLayerIndex);

         // clear last collision
         for (int xIndex = lastTileX; xIndex < lastTileX + getObjectIndexWidth(object); ++xIndex) {
            for (int yIndex = lastTileY; yIndex < lastTileY + getObjectIndexHeight(object); ++yIndex) {
               if (context.isInclusiveCollision(xIndex, yIndex, lastLayerIndex, object)) {
                  CollisionCalculator.updateCollision(object, false, xIndex, yIndex, lastLayerIndex, state);
               }
            }
         }

         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("TiledMapAPI", "Cleared collision at x=" + lastTileX + " y=" + lastTileY + " layer=" + lastLayerIndex);
         }
         // Update current collision
         if (!object.equals(occupant) && object.isActive()) {
            if (!context.isExclusiveCollision(object.getLeft(), object.getTop(), currentLayerIndex, object)) {
               CollisionCalculator.updateCollision(object, true, object.getLeft(), object.getTop(), currentLayerIndex, state);
            }
            int tileX = calculateIndex(object.getLeft(), state.getCellWidth());
            int tileY = calculateIndex(object.getTop(), state.getCellWidth());
            for (int xIndex = tileX; xIndex < tileX + getObjectIndexWidth(object); ++xIndex) {
               for (int yIndex = tileY; yIndex < tileY + getObjectIndexHeight(object); ++yIndex) {
                  if (!context.isExclusiveCollision(xIndex, yIndex, currentLayerIndex, object)) {
                     CollisionCalculator.updateCollision(object, true, xIndex, yIndex, currentLayerIndex, state);
                  }
                  if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
                     Gdx.app.debug("TiledMapAPI", "Applied collision at x=" + xIndex + " y=" + yIndex + " layer=" + lastLayerIndex);
                  }
               }
            }
         }
      }
      if (lastLayerIndex != currentLayerIndex) {
         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("TiledMapAPI", "Tiled map layer change of " + object + " from " + lastLayerIndex + " -> " + currentLayerIndex);
         }
         gameEventManager.publish(new TiledMapEvents.OnLayerChangeEvent(lastLayerIndex, currentLayerIndex, object, context));

      }
      if (!currentPosition.equals(lastPosition)) {
         int xIndex = calculateIndex(currentPosition.x, context.getCellWidth());
         int yIndex = calculateIndex(currentPosition.y, context.getCellHeight());
         gameEventManager.publish(new TiledMapEvents.OnEnterCellEvent(xIndex, yIndex, object, context));
      }
      object.setAttribute(Constants.LAST_TILE_X, calculateIndex(object.getLeft(), context.getCellWidth()));
      object.setAttribute(Constants.LAST_TILE_Y, calculateIndex(object.getTop(), context.getCellHeight()));
      object.setAttribute(Constants.LAST_LAYER_INDEX, context.layerIndexOf(object));
   }

   private void updateLayerIndex(GameObject object) {
      if (object.hasAttribute(Constants.LAYER_INDEX)) {
         int layerIndex = (Integer) object.getAttribute(Constants.LAYER_INDEX);
         object.setAttribute(Constants.LAST_LAYER_INDEX, layerIndex);
      }
   }

   private int getObjectIndexWidth(GameObject object) {
      return (int) Math.floor(object.getWidth() / context.getCellWidth());
   }

   private int getObjectIndexHeight(GameObject object) {
      return (int) Math.floor(object.getHeight() / context.getCellHeight());
   }
}
