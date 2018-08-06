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
import de.bitbrain.braingdx.postprocessing.effects.CrtMonitor;

public class CrtMonitorShaderTween implements TweenAccessor<CrtMonitor> {

   public static final int CHROMATIC_DISPERSION_BY = 1;
   public static final int CHROMATIC_DISPERSION_RC = 2;
   public static final int DISTORTION = 3;
   public static final int TIME = 4;
   public static final int ZOOM = 5;

   @Override
   public int getValues(CrtMonitor target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case CHROMATIC_DISPERSION_BY:
            returnValues[0] = target.getChromaticDispersion().x;
            return 1;
         case CHROMATIC_DISPERSION_RC:
            returnValues[0] = target.getChromaticDispersion().y;
            return 1;
         case DISTORTION:
            returnValues[0] = target.getDistortion();
            return 1;
         case TIME:
            returnValues[0] = target.getTime();
            return 1;
         case ZOOM:
            returnValues[0] = target.getZoom();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(CrtMonitor target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case CHROMATIC_DISPERSION_BY:
            target.setChromaticDispersionBY(newValues[0]);
            break;
         case CHROMATIC_DISPERSION_RC:
            target.setChromaticDispersionRC(newValues[0]);
            break;
         case DISTORTION:
            target.setDistortion(newValues[0]);
            break;
         case TIME:
            target.setTime(newValues[0]);
            break;
         case ZOOM:
            target.setZoom(newValues[0]);
            break;
      }
   }

}
