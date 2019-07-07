/*******************************************************************************
 * Copyright 2012 bmanuel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.bitbrain.braingdx.graphics.postprocessing.effects;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialDistortion;

public final class Curvature extends PostProcessorEffect {
   private RadialDistortion distort;

   public Curvature() {
      distort = new RadialDistortion();
   }

   @Override
   public void dispose() {
      distort.dispose();
   }

   public float getDistortion() {
      return distort.getDistortion();
   }

   public void setDistortion(float distortion) {
      distort.setDistortion(distortion);
   }

   public float getZoom() {
      return distort.getZoom();
   }

   public void setZoom(float zoom) {
      distort.setZoom(zoom);
   }

   @Override
   public void rebind() {
      distort.rebind();
   }

   @Override
   public void render(FrameBuffer src, FrameBuffer dest) {
      restoreViewport(dest);
      distort.setInput(src).setOutput(dest).render();
   }

   ;

}
