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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.maps.MapProperties;

/**
 * Internal state for tiled map operations.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class State {

   public static class CellState {
      private boolean collision;
      private MapProperties properties;

      public boolean isCollision() {
         return collision;
      }

      public void setCollision(boolean collision) {
         this.collision = collision;
      }

      public MapProperties getProperties() {
         return properties;
      }

      public void setProperties(MapProperties properties) {
         this.properties = properties;
      }

      @Override
      public String toString() {
         return "CellState [collision=" + collision + ", properties=" + properties + "]";
      }

   }

   private List<String> layerIds = Collections.emptyList();

   private Integer[][] heightMap;

   private Map<Integer, CellState[][]> stateMap = Collections.emptyMap();

   private int mapIndexHeight;

   private int mapIndexWidth;

   private float cellWidth = 1f;
   private float cellHeight = 1f;

   public List<String> getLayerIds() {
      return layerIds;
   }

   public Integer[][] getHeightMap() {
      return heightMap;
   }

   public int getMapIndexWidth() {
      return mapIndexWidth;
   }

   public int getMapIndexHeight() {
      return mapIndexHeight;
   }

   public float getCellWidth() {
      return cellWidth;
   }

   public float getCellHeight() {
      return cellHeight;
   }

   public void setLayerIds(List<String> layerIds) {
      layerIds = Collections.unmodifiableList(layerIds);
   }

   public void setHeightMap(Integer[][] heightMap) {
      this.heightMap = heightMap;
   }

   public CellState getState(int tileX, int tileY, int layerIndex) {
      if (tileX >= getMapIndexWidth()) {
         tileX = getMapIndexWidth() - 1;
      } else if (tileX < 0) {
         tileX = 0;
      }
      if (tileY >= getMapIndexHeight()) {
         tileY = getMapIndexHeight() - 1;
      } else if (tileY < 0) {
         tileY = 0;
      }

      if (stateMap.isEmpty()) {
         stateMap = new HashMap<Integer, CellState[][]>();
      }
      CellState[][] states = stateMap.get(layerIndex);
      if (states == null) {
         states = new CellState[getMapIndexWidth()][getMapIndexHeight()];
         stateMap.put(layerIndex, states);
      }
      if (states[tileX][tileY] == null) {
         states[tileX][tileY] = new CellState();
      }
      return states[tileX][tileY];
   }

   public void setIndexDimensions(int indexX, int indexY) {
      this.mapIndexWidth = indexX;
      this.mapIndexHeight = indexY;
   }

   public void setCellWidth(float width) {
      if (width > 0) {
         this.cellWidth = width;
      }
   }

   public void setCellHeight(float height) {
      if (height > 0) {
         this.cellHeight = height;
      }
   }

   public void clear() {
      heightMap = null;
      layerIds = Collections.emptyList();
      stateMap.clear();
      stateMap = Collections.emptyMap();
   }

   public int getNumberOfLayers() {
      return stateMap.keySet().size();
   }
}
