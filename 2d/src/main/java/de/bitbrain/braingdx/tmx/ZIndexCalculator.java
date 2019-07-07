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

import de.bitbrain.braingdx.world.GameObject;

/**
 * This component calculates the Z index for instances of type {@link GameObject}.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class ZIndexCalculator {

   public static int calculateZIndex(GameObject object, TiledMapContext api, PositionTranslator positionTranslator, int currentLayerIndex) {
      int rows = api.getNumberOfRows();
      int yIndex = positionTranslator.toIndexY(object.getTop());
      return calculateZIndex(rows, yIndex, currentLayerIndex);
   }

   public static int calculateZIndex(GameObject object, State state, PositionTranslator positionTranslator, int currentLayerIndex) {
      int rows = state.getMapIndexHeight();
      int yIndex = positionTranslator.toIndexY(object.getTop());
      return calculateZIndex(rows, yIndex, currentLayerIndex);
   }

   public static int calculateZIndex(int rows, int yIndex, int currentLayerIndex) {
      return (currentLayerIndex + 1) * rows - yIndex;
   }
}
