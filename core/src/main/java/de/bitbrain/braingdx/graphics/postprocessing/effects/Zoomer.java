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
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialBlur;
import de.bitbrain.braingdx.graphics.postprocessing.filters.Zoom;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;

/**
 * Implements a zooming effect: either a radial blur filter or a zoom filter is used.
 */
public final class Zoomer extends PostProcessorEffect {
   private boolean doRadial = false;
   private RadialBlur radialBlur = null;
   private Zoom zoom = null;
   private float oneOnW, oneOnH;
   private float userOriginX, userOriginY;
   private float renderScale;

   /**
    * Creating a Zoomer specifying the radial blur quality will enable radial blur
    */
   public Zoomer(int viewportWidth, int viewportHeight, RadialBlur.Quality quality) {
      this.renderScale = 1f;
      setup(viewportWidth, viewportHeight, new RadialBlur(quality));
   }

   /**
    * Creating a Zoomer without any parameter will use plain simple zooming
    */
   public Zoomer(Zoomer original, int viewportWidth, int viewportHeight, RadialBlur.Quality radialBlurQuality, float renderScale) {
      this.renderScale = renderScale;
      RadialBlur radialBlur = null;
      if (original.radialBlur != null) {
         radialBlur = new RadialBlur(radialBlurQuality);
         radialBlur.setStrength(original.getBlurStrength());
         radialBlur.setZoom(original.getZoom());
      }
      setup(viewportWidth, viewportHeight, radialBlur);
      if (this.zoom != null) {
         zoom.setZoom(original.getZoom());
      }
      setOrigin(original.getOriginX(), original.getOriginY());
   }

   public Zoomer(int viewportWidth, int viewportHeight, RadialBlur.Quality quality, float renderScale) {
      this.renderScale = renderScale;
      setup(viewportWidth, viewportHeight, new RadialBlur(quality));
   }

   private void setup(int viewportWidth, int viewportHeight, RadialBlur radialBlurFilter) {
      radialBlur = radialBlurFilter;
      if (radialBlur != null) {
         doRadial = true;
         zoom = null;
      } else {
         doRadial = false;
         zoom = new Zoom();
      }

      oneOnW = 1f / (float)viewportWidth;
      oneOnH = 1f / (float)viewportHeight;
   }

   /**
    * Specify the zoom origin, in screen coordinates.
    */
   public void setOrigin(Vector2 o) {
      setOrigin(o.x, o.y);
   }

   /**
    * Specify the zoom origin, in screen coordinates.
    */
   public void setOrigin(float x, float y) {
      userOriginX = x;
      userOriginY = y;

      if (doRadial) {
         radialBlur.setOrigin(x * oneOnW * renderScale, 1f - y * oneOnH * renderScale);
      } else {
         zoom.setOrigin(x * oneOnW * renderScale, 1f - y * oneOnH * renderScale);
      }
   }

   public float getZoom() {
      if (doRadial) {
         return 1f / radialBlur.getZoom();
      } else {
         return 1f / zoom.getZoom();
      }
   }

   public void setZoom(float zoom) {
      if (doRadial) {
         radialBlur.setZoom(1f / zoom);
      } else {
         this.zoom.setZoom(1f / zoom);
      }
   }

   public float getBlurStrength() {
      if (doRadial) {
         return radialBlur.getStrength();
      }

      return -1;
   }

   public void setBlurStrength(float strength) {
      if (doRadial) {
         radialBlur.setStrength(strength);
      }
   }

   public float getOriginX() {
      return userOriginX;
   }

   public float getOriginY() {
      return userOriginY;
   }

   @Override
   public void dispose() {
      if (radialBlur != null) {
         radialBlur.dispose();
      }

      if (zoom != null) {
         zoom.dispose();
      }
   }

   @Override
   public void rebind() {
      radialBlur.rebind();
   }

   @Override
   public void render(FrameBuffer src, FrameBuffer dest) {
      restoreViewport(dest);
      if (doRadial) {
         radialBlur.setInput(src).setOutput(dest).render();
      } else {
         zoom.setInput(src).setOutput(dest).render();
      }
   }
}
