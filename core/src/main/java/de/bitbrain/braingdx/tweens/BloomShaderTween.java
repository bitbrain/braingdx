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
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;

public class BloomShaderTween implements TweenAccessor<Bloom> {

   public static final int BASE_INTENSITY = 1;
   public static final int BASE_SATURATION = 2;
   public static final int BLOOM_INTENSITY = 3;
   public static final int BLOOM_SATURATION = 4;
   public static final int THRESHOLD = 5;


   @Override
   public int getValues(Bloom target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case BASE_INTENSITY:
            returnValues[0] = target.getBaseIntensity();
            return 1;
         case BASE_SATURATION:
            returnValues[0] = target.getBaseSaturation();
            return 1;
         case BLOOM_INTENSITY:
            returnValues[0] = target.getBloomIntensity();
            return 1;
         case BLOOM_SATURATION:
            returnValues[0] = target.getBloomSaturation();
            return 1;
         case THRESHOLD:
            returnValues[0] = target.getThreshold();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(Bloom target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case BASE_INTENSITY:
            target.setBaseIntesity(newValues[0]);
            break;
         case BASE_SATURATION:
            target.setBaseSaturation(newValues[0]);
            break;
         case BLOOM_INTENSITY:
            target.setBloomIntesity(newValues[0]);
            break;
         case BLOOM_SATURATION:
            target.setBloomSaturation(newValues[0]);
            break;
         case THRESHOLD:
            target.setThreshold(newValues[0]);
            break;
      }
   }

}
