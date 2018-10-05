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
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;

public class ZoomerShaderTween implements TweenAccessor<Zoomer> {

   public static final int BLUR_STRENGTH = 1;
   public static final int ZOOM_AMOUNT = 2;


   @Override
   public int getValues(Zoomer target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case BLUR_STRENGTH:
            returnValues[0] = target.getBlurStrength();
            return 1;
         case ZOOM_AMOUNT:
            returnValues[0] = target.getZoom();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(Zoomer target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case BLUR_STRENGTH:
            target.setBlurStrength(newValues[0]);
            break;
         case ZOOM_AMOUNT:
            target.setZoom(newValues[0]);
            break;
      }
   }

}
