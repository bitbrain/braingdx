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

package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.BatchPostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.Resizeable;

/**
 * Layered implementation of {@link RenderPipe}.
 *
 * @author Miguel Gonzalez Sanchez
 * @TODO Fix ensure shader transparency.
 */
class LayeredRenderPipe implements RenderPipe, Disposable, Resizeable {
   private final FrameBufferFactory bufferFactory;
   private RenderLayer renderLayer;
   private BatchPostProcessor batchPostProcessor;
   private FrameBuffer buffer;
   private boolean enabled;

   LayeredRenderPipe(RenderLayer layer, PostProcessor processor, FrameBufferFactory factory,
                     PostProcessorEffect... effects) {
      this.renderLayer = layer;
      this.batchPostProcessor = new BatchPostProcessor(processor, effects);
      this.bufferFactory = factory;
      this.setEnabled(true);
   }

   @Override
   public boolean isEnabled() {
      return enabled;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   public void render(Batch batch, float delta) {
      if (batchPostProcessor.hasEffects() && buffer != null) {
         renderOntoBuffer(batch, delta);
      } else {
         draw(batch, delta);
      }
      blendAndDraw(batch);
   }

   @Override
   public void resize(int width, int height) {
      if (buffer != null) {
         buffer.dispose();
      }
      this.buffer = bufferFactory.create(width, height);
   }

   @Override
   public void addEffects(PostProcessorEffect... effects) {
      batchPostProcessor.addEffects(effects);
   }

   @Override
   public void setEffects(PostProcessorEffect[] effects) {
      batchPostProcessor.clear();
      batchPostProcessor.addEffects(effects);
   }

   @Override
   public boolean hasEffects() {
      return batchPostProcessor.hasEffects();
   }

   @Override
   public void dispose() {
      buffer.dispose();
      if (renderLayer instanceof Disposable) {
         ((Disposable) renderLayer).dispose();
      }
   }

   private void renderOntoBuffer(Batch batch, float delta) {
      batchPostProcessor.begin();
      renderLayer.render(batch, delta);
      batchPostProcessor.end(buffer);
   }

   private void blendAndDraw(Batch batch) {
      int srcFunc = batch.getBlendSrcFunc();
      int dstFunc = batch.getBlendDstFunc();
      batch.begin();
      batch.setColor(Color.WHITE);
      batch.draw(buffer.getColorBufferTexture(), 0f, 0f);
      batch.end();
      batch.setBlendFunction(srcFunc, dstFunc);
   }

   private void draw(Batch batch, float delta) {
      batch.setColor(Color.WHITE);
      renderLayer.render(batch, delta);
   }
}
