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

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

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

   private final List<TiledMapListener> listeners;

   public GameObjectUpdater(TiledMapAPI api, State state, List<TiledMapListener> listeners) {
      this.api = api;
      this.state = state;
      this.listeners = listeners;
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
      // Remove last collision if object has moved
      // and last position is not occupied
      Vector2 lastPosition = object.getLastPosition();
      currentPosition.set(object.getLeft(), object.getTop());
      int lastLayerIndex = api.lastLayerIndexOf(object);
      int currentLayerIndex = api.layerIndexOf(object);
      for (TiledMapListener listener : listeners) {
         if (lastLayerIndex != currentLayerIndex) {
            listener.onLayerChange(lastLayerIndex, currentLayerIndex, object, api);
         }
         if (!currentPosition.equals(lastPosition)) {
            int xIndex = IndexCalculator.calculateIndex(currentPosition.x, api.getCellWidth());
            int yIndex = IndexCalculator.calculateIndex(currentPosition.y, api.getCellHeight());
            listener.onEnterCell(xIndex, yIndex, object, api);
         }
      }
      if (lastLayerIndex != currentLayerIndex || !currentPosition.equals(lastPosition)) {
         // Object has moved, now check if last position is already occupied
         int lastTileX = IndexCalculator.calculateIndex(lastPosition.x, api.getCellWidth());
         int lastTileY = IndexCalculator.calculateIndex(lastPosition.y, api.getCellHeight());
         GameObject occupant = api.getGameObjectAt(lastTileX, lastTileY, lastLayerIndex);

         // clear last collision
         state.getState(lastTileX, lastTileY, lastLayerIndex).setCollision(false);

         // Update current collision
         if (!object.equals(occupant) && object.isActive()) {
            CollisionCalculator.updateCollision(true, object.getLeft(), object.getTop(), currentLayerIndex, state);
         }
      }
   }

   private void updateLayerIndex(GameObject object) {
      if (object.hasAttribute(Constants.LAYER_INDEX)) {
         int layerIndex = (Integer) object.getAttribute(Constants.LAYER_INDEX);
         object.setAttribute(Constants.LAST_LAYER_INDEX, layerIndex);
      }
   }
}
