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

import com.badlogic.gdx.maps.MapProperties;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Implementation of {@link TiledMapAPI}.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
class TiledMapAPIImpl implements TiledMapAPI {

   private final State state;
   private final GameEventManager gameEventManager;
   private final GameWorld gameWorld;
   private final GameEventRouter eventRouter;
   private boolean debug;

   public TiledMapAPIImpl(State state, GameWorld gameWorld, GameEventRouter router, GameEventManager gameEventManager) {
      this.state = state;
      this.gameEventManager = gameEventManager;
      this.gameWorld = gameWorld;
      this.eventRouter = router;
   }

   @Override
   public void setEventFactory(GameEventFactory eventFactory) {
      this.eventRouter.setEventFactory(eventFactory);
   }

   @Override
   public int highestZIndexAt(int tileX, int tileY) {
      Integer[][] heightMap = state.getHeightMap();
      if (verifyIndex(tileX, tileY) && heightMap != null) {
         return heightMap[tileX][tileY];
      } else {
         return -1;
      }
   }

   @Override
   public int highestZIndexAt(float x, float y) {
      int tileX = (int) Math.floor(x / (float) state.getCellWidth());
      int tileY = (int) Math.floor(y / (float) state.getCellWidth());
      return highestZIndexAt(tileX, tileY);
   }

   @Override
   public int layerIndexOf(GameObject object) {
      if (object.hasAttribute(Constants.LAYER_INDEX)) {
         return (Integer) object.getAttribute(Constants.LAYER_INDEX);
      } else {
         return -1;
      }
   }

   @Override
   public int lastLayerIndexOf(GameObject object) {
      if (object.hasAttribute(Constants.LAST_LAYER_INDEX)) {
         return (Integer) object.getAttribute(Constants.LAST_LAYER_INDEX);
      } else {
         return layerIndexOf(object);
      }
   }

   @Override
   public void setLayerIndex(GameObject object, int layerIndex) {
      if (layerIndex > state.getNumberOfLayers() - 1) {
         throw new TiledMapException("Layer index is too high: " + layerIndex);
      }
      object.setAttribute(Constants.LAYER_INDEX, layerIndex);
   }

   @Override
   public int getNumberOfRows() {
      return state.getMapIndexHeight();
   }

   @Override
   public int getNumberOfColumns() {
      return state.getMapIndexWidth();
   }

   @Override
   public GameObject getGameObjectAt(int tileX, int tileY, int layer) {
      for (GameObject worldObject : gameWorld) {
         int objectTileX = IndexCalculator.calculateXIndex(worldObject, state);
         int objectTileY = IndexCalculator.calculateYIndex(worldObject, state);
         int layerIndex = layerIndexOf(worldObject);
         if (objectTileX == tileX && objectTileY == tileY && layer == layerIndex) {
            return worldObject;
         }
      }
      return null;
   }

   @Override
   public float getCellWidth() {
      return state.getCellWidth();
   }

   @Override
   public float getCellHeight() {
      return state.getCellHeight();
   }

   @Override
   public boolean isCollision(int tileX, int tileY, int layer) {
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision();
   }

   @Override
   public boolean isExclusiveCollision(int tileX, int tileY, int layer, GameObject source) {
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && !state.getState(tileX, tileY, layer).isFingerprint(source.getId());
   }

   @Override
   public boolean isInclusiveCollision(int tileX, int tileY, int layer, GameObject source) {
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && state.getState(tileX, tileY, layer).isFingerprint(source.getId());
   }

   @Override
   public boolean isCollision(float x, float y, int layer) {
      int tileX = IndexCalculator.calculateIndex(x, state.getCellWidth());
      int tileY = IndexCalculator.calculateIndex(y, state.getCellHeight());
      return isCollision(tileX, tileY, layer);
   }

   @Override
   public boolean isExclusiveCollision(GameObject object) {
      return isExclusiveCollision(object.getLeft(), object.getHeight(), layerIndexOf(object), object);
   }

   @Override
   public boolean isInclusiveCollision(GameObject object) {
      return isInclusiveCollision(object.getLeft(), object.getHeight(), layerIndexOf(object), object);
   }

   @Override
   public boolean isExclusiveCollision(float x, float y, int layer, GameObject object) {
      int tileX = IndexCalculator.calculateIndex(x, state.getCellWidth());
      int tileY = IndexCalculator.calculateIndex(y, state.getCellHeight());
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && !state.getState(tileX, tileY, layer).isFingerprint(object.getId());
   }

   @Override
   public boolean isInclusiveCollision(float x, float y, int layer, GameObject object) {
      int tileX = IndexCalculator.calculateIndex(x, state.getCellWidth());
      int tileY = IndexCalculator.calculateIndex(y, state.getCellHeight());
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && state.getState(tileX, tileY, layer).isFingerprint(object.getId());
   }

   @Override
   public boolean isCollision(GameObject object, int tileOffsetX, int tileOffsetY) {
      int layer = layerIndexOf(object);
      int tileX = IndexCalculator.calculateXIndex(object, state) + tileOffsetX;
      int tileY = IndexCalculator.calculateYIndex(object, state) + tileOffsetY;
      return isCollision(tileX, tileY, layer);
   }

   @Override
   public MapProperties getPropertiesAt(int tileX, int tileY, int layer) {
      return state.getState(tileX, tileY, layer).getProperties();
   }

   @Override
   public float getWorldWidth() {
      return getCellWidth() * getNumberOfColumns();
   }

   @Override
   public float getWorldHeight() {
      return getCellHeight() * getNumberOfRows();
   }

   private boolean verifyIndex(int indexX, int indexY) {
      return indexX >= 0 && indexY >= 0 && indexX < state.getMapIndexWidth() && indexY < state.getMapIndexHeight();
   }

   @Override
   public boolean isDebug() {
      return debug;
   }

   @Override
   public void setDebug(boolean enabled) {
      this.debug = enabled;
   }

}
