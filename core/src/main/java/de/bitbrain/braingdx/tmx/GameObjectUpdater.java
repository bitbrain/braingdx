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
import de.bitbrain.braingdx.world.GameObject;

import java.util.List;

/**
 * This component updates game objects which are part of the tiledmap lifecycle.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class GameObjectUpdater extends BehaviorAdapter {

   private final TiledMapAPI api;

   private final State state;

   private final Vector2 currentPosition = new Vector2();

   private final GameEventManager gameEventManager;

   public GameObjectUpdater(TiledMapAPI api, State state, GameEventManager gameEventManager) {
      this.api = api;
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
         object.setAttribute(Constants.LAST_LAYER_INDEX, api.lastLayerIndexOf(object));
      }
   }

   private void updateZIndex(GameObject object) {
      int currentLayerIndex = api.layerIndexOf(object);
      object.setZIndex(IndexCalculator.calculateZIndex(object, api, currentLayerIndex));
   }

   private void updateCollision(GameObject object) {
      // Verify that object is valid for collisions
      if (object.hasAttribute(MapProperties.class)) {
         MapProperties mapProperties = (MapProperties)object.getAttribute(MapProperties.class);
         // Do not update objects which have a false collision
         if (mapProperties.containsKey("collision")) {
            if (mapProperties.get("collision") instanceof Boolean && !(Boolean)mapProperties.get("collision")) {
               return;
            }
            if (mapProperties.get("collision") instanceof String && mapProperties.get("collision").equals("false")) {
               return;
            }
         }
      }
      // Remove last collision if object has moved
      // and last position is not occupied
      Vector2 lastPosition = object.getLastPosition();
      currentPosition.set(object.getLeft(), object.getTop());
      int lastLayerIndex = api.lastLayerIndexOf(object);
      int currentLayerIndex = api.layerIndexOf(object);
      if (lastLayerIndex != currentLayerIndex || !currentPosition.equals(lastPosition)) {
         Gdx.app.debug("TiledMapAPI", "Updating collision of " + object);
         // Object has moved, now check if last position is already occupied
         int lastTileX = IndexCalculator.calculateIndex(lastPosition.x, api.getCellWidth());
         int lastTileY = IndexCalculator.calculateIndex(lastPosition.y, api.getCellHeight());
         GameObject occupant = api.getGameObjectAt(lastTileX, lastTileY, lastLayerIndex);

         // clear last collision
         for (int xIndex = lastTileX; xIndex < lastTileX + getObjectIndexWidth(object); ++xIndex) {
            for (int yIndex = lastTileY; yIndex < lastTileY + getObjectIndexHeight(object); ++yIndex) {
               state.getState(xIndex, yIndex, lastLayerIndex).setCollision(false);
               state.getState(xIndex, yIndex, lastLayerIndex).setFingerprint(null);
            }
         }

         Gdx.app.debug("TiledMapAPI", "Cleared collision at x=" + lastTileX + " y=" + lastTileY + " layer=" + lastLayerIndex);
         // Update current collision
         if (!object.equals(occupant) && object.isActive()) {
            CollisionCalculator.updateCollision(true, object.getLeft(), object.getTop(), currentLayerIndex, state);
            state.getState(lastTileX, lastTileY, lastLayerIndex).setFingerprint(object.getId());
            int tileX = IndexCalculator.calculateIndex(object.getLeft(), state.getCellWidth());
            int tileY = IndexCalculator.calculateIndex(object.getTop(), state.getCellWidth());
            for (int xIndex = tileX; xIndex < tileX + getObjectIndexWidth(object); ++xIndex) {
               for (int yIndex = tileY; yIndex < tileY + getObjectIndexHeight(object); ++yIndex) {
                  state.getState(xIndex, yIndex, currentLayerIndex).setCollision(true);
                  state.getState(xIndex, yIndex, currentLayerIndex).setFingerprint(object.getId());
                  if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
                     Gdx.app.debug("TiledMapAPI", "Applied collision at x=" + xIndex + " y=" + yIndex + " layer=" + lastLayerIndex);
                  }
               }
            }
         }
      }
      if (lastLayerIndex != currentLayerIndex) {
         Gdx.app.debug("TiledMapAPI", "Tiled map layer change of " + object + " from " + lastLayerIndex + " -> " + currentLayerIndex);
         gameEventManager.publish(new TiledMapEvents.OnLayerChangeEvent(lastLayerIndex, currentLayerIndex, object, api));

      }
      if (!currentPosition.equals(lastPosition)) {
         int xIndex = IndexCalculator.calculateIndex(currentPosition.x, api.getCellWidth());
         int yIndex = IndexCalculator.calculateIndex(currentPosition.y, api.getCellHeight());
         gameEventManager.publish(new TiledMapEvents.OnEnterCellEvent(xIndex, yIndex, object, api));
      }
   }

   private void updateLayerIndex(GameObject object) {
      if (object.hasAttribute(Constants.LAYER_INDEX)) {
         int layerIndex = (Integer) object.getAttribute(Constants.LAYER_INDEX);
         object.setAttribute(Constants.LAST_LAYER_INDEX, layerIndex);
      }
   }

   private int getObjectIndexWidth(GameObject object) {
      return (int)Math.floor(object.getWidth() / api.getCellWidth());
   }

   private int getObjectIndexHeight(GameObject object) {
      return (int)Math.floor(object.getHeight() / api.getCellHeight());
   }
}
