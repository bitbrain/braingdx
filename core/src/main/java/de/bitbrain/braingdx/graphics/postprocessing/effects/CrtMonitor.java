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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.graphics.postprocessing.filters.Blur;
import de.bitbrain.braingdx.graphics.postprocessing.filters.Combine;
import de.bitbrain.braingdx.graphics.postprocessing.filters.CrtScreen;
import de.bitbrain.braingdx.graphics.postprocessing.filters.CrtScreen.RgbMode;
import de.bitbrain.braingdx.graphics.postprocessing.utils.PingPongBuffer;

public final class CrtMonitor extends PostProcessorEffect {
   private PingPongBuffer pingPongBuffer = null;
   private FrameBuffer buffer = null;
   private CrtScreen crt;
   private Blur blur;
   private Combine combine;
   private boolean doblur;

   private boolean blending = false;
   private int sfactor, dfactor;

   // the effect is designed to work on the whole screen area, no small/mid size tricks!
   public CrtMonitor(int fboWidth, int fboHeight, boolean barrelDistortion, boolean performBlur, RgbMode mode,
                     int effectsSupport) {
      doblur = performBlur;

      if (doblur) {
         pingPongBuffer = PostProcessor.newPingPongBuffer(fboWidth, fboHeight, PostProcessor.getFramebufferFormat(),
               false);
         blur = new Blur(fboWidth, fboHeight);
         blur.setPasses(1);
         blur.setAmount(1f);
         // blur.setType( BlurType.Gaussian3x3b ); // high defocus
         blur.setType(Blur.BlurType.Gaussian3x3); // modern machines defocus
      } else {
         buffer = new FrameBuffer(PostProcessor.getFramebufferFormat(), fboWidth, fboHeight, false);
      }

      combine = new Combine();
      crt = new CrtScreen(barrelDistortion, mode, effectsSupport);
   }

   @Override
   public void dispose() {
      crt.dispose();
      combine.dispose();
      if (doblur) {
         blur.dispose();
      }

      if (buffer != null) {
         buffer.dispose();
      }

      if (pingPongBuffer != null) {
         pingPongBuffer.dispose();
      }
   }

   public void enableBlending(int sfactor, int dfactor) {
      this.blending = true;
      this.sfactor = sfactor;
      this.dfactor = dfactor;
   }

   public void disableBlending() {
      this.blending = false;
   }

   public void setColorOffset(float offset) {
      crt.setColorOffset(offset);
   }

   public void setChromaticDispersion(float redCyan, float blueYellow) {
      crt.setChromaticDispersion(redCyan, blueYellow);
   }

   public void setChromaticDispersionRC(float redCyan) {
      crt.setChromaticDispersionRC(redCyan);
   }

   public void setChromaticDispersionBY(float blueYellow) {
      crt.setChromaticDispersionBY(blueYellow);
   }

   public void setTint(float r, float g, float b) {
      crt.setTint(r, g, b);
   }

   // getters
   public Combine getCombinePass() {
      return combine;
   }

   public float getOffset() {
      return crt.getOffset();
   }

   public Vector2 getChromaticDispersion() {
      return crt.getChromaticDispersion();
   }

   public float getZoom() {
      return crt.getZoom();
   }

   public void setZoom(float zoom) {
      crt.setZoom(zoom);
   }

   public Color getTint() {
      return crt.getTint();
   }

   public void setTint(Color tint) {
      crt.setTint(tint);
   }

   public float getTime() {
      return crt.getTime();
   }

   // setters
   public void setTime(float elapsedSecs) {
      crt.setTime(elapsedSecs);
   }

   public float getDistortion() {
      return crt.getDistortion();
   }

   public void setDistortion(float distortion) {
      crt.setDistortion(distortion);
   }

   public RgbMode getRgbMode() {
      return crt.getRgbMode();
   }

   public void setRgbMode(RgbMode mode) {
      crt.setRgbMode(mode);
   }

   @Override
   public void rebind() {
      crt.rebind();
   }

   @Override
   public void render(FrameBuffer src, FrameBuffer dest) {
      // the original scene
      Texture in = src.getColorBufferTexture();

      boolean blendingWasEnabled = PostProcessor.isStateEnabled(GL20.GL_BLEND);
      Gdx.gl.glDisable(GL20.GL_BLEND);

      Texture out = null;

      if (doblur) {

         pingPongBuffer.begin();
         {
            // crt pass
            crt.setInput(in).setOutput(pingPongBuffer.getSourceBuffer()).render();

            // blur pass
            blur.render(pingPongBuffer);
         }
         pingPongBuffer.end();

         out = pingPongBuffer.getResultTexture();
      } else {
         // crt pass
         crt.setInput(in).setOutput(buffer).render();

         out = buffer.getColorBufferTexture();
      }

      if (blending || blendingWasEnabled) {
         Gdx.gl.glEnable(GL20.GL_BLEND);
      }

      if (blending) {
         Gdx.gl.glBlendFunc(sfactor, dfactor);
      }

      restoreViewport(dest);

      // do combine pass
      combine.setOutput(dest).setInput(in, out).render();
   }
}
