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

package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.math.Vector2;

/**
 * Tween facility for vectors
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 * @version 1.0.0
 * @since 1.0.0
 */
public class VectorTween implements TweenAccessor<Vector2> {

   public static final int POS_X = 1;

   public static final int POS_Y = 2;

   @Override
   public int getValues(Vector2 target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case POS_X:
            returnValues[0] = target.x;
            return 1;
         case POS_Y:
            returnValues[0] = target.y;
            return 1;
         default:
            return 0;
      }
   }

   @Override
   public void setValues(Vector2 target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case POS_X:
            target.x = newValues[0];
            break;
         case POS_Y:
            target.y = newValues[0];
            break;
      }
   }

}
