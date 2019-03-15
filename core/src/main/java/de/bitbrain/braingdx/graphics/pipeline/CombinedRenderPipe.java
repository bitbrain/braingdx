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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.graphics.shader.BatchPostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.Resizeable;

class CombinedRenderPipe implements RenderPipe, Resizeable {

   private final RenderLayer layer;

   private final BatchPostProcessor batchPostProcessor;
   private final SpriteBatch batch;
   private boolean enabled = true;

   public CombinedRenderPipe(RenderLayer layer, PostProcessor processor, SpriteBatch batch,
                             PostProcessorEffect... effects) {
      this.layer = layer;
      this.batchPostProcessor = new BatchPostProcessor(processor, effects);
      this.batch = batch;
   }

   @Override
   public boolean isEnabled() {
      return enabled;
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
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

   public void render(Batch batch, float delta, FrameBuffer buffer) {
      if (isEnabled()) {
         if (batchPostProcessor.hasEffects()) {
            batchPostProcessor.begin();
            this.batch.begin();
            this.batch.draw(buffer.getColorBufferTexture(), 0f, 0f);
            this.batch.end();
            layer.render(batch, delta);
            batchPostProcessor.end(buffer);
         } else {
            buffer.begin();
            layer.render(batch, delta);
            buffer.end();
         }
      }
   }

   public void beforeRender() {
      layer.beforeRender();
   }

   @Override
   public void resize(int width, int height) {
      if (layer instanceof Resizeable) {
         ((Resizeable) layer).resize(width, height);
      }
   }
}
