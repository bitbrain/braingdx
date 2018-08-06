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
import de.bitbrain.braingdx.postprocessing.effects.Vignette;

public class VignetteShaderTween implements TweenAccessor<Vignette> {

   public static final int CENTER_X = 1;
   public static final int CENTER_Y = 2;
   public static final int INTENSITY = 3;
   public static final int SATURATION = 4;
   public static final int SATURATION_MULTIPLIER = 5;
   public static final int LUT_INTENSITY = 6;

   @Override
   public int getValues(Vignette target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case CENTER_X:
            returnValues[0] = target.getCenterX();
            return 1;
         case CENTER_Y:
            returnValues[0] = target.getCenterY();
            return 1;
         case INTENSITY:
            returnValues[0] = target.getIntensity();
            return 1;
         case SATURATION:
            returnValues[0] = target.getSaturation();
            return 1;
         case SATURATION_MULTIPLIER:
            returnValues[0] = target.getSaturationMul();
            return 1;
         case LUT_INTENSITY:
            returnValues[0] = target.getLutIntensity();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(Vignette target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case CENTER_X:
            target.setCenter(newValues[0], target.getCenterY());
            break;
         case CENTER_Y:
            target.setCenter(target.getCenterX(), newValues[0]);
            break;
         case INTENSITY:
            target.setIntensity(newValues[0]);
            break;
         case SATURATION:
            target.setSaturation(newValues[0]);
            break;
         case SATURATION_MULTIPLIER:
            target.setSaturationMul(newValues[0]);
            break;
         case LUT_INTENSITY:
            target.setLutIntensity(newValues[0]);
            break;
      }
   }

}
