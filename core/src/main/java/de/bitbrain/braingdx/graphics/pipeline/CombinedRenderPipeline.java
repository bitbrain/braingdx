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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;
import de.bitbrain.braingdx.graphics.BatchResolver;
import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.util.ShaderLoader;
import org.apache.commons.collections.map.ListOrderedMap;

import java.util.*;

/**
 * Combined implementation of {@link RenderPipeline}. This pipeline will bake together all layers
 * and apply shaders for all layers underneath: <br/>
 *
 * <pre>
 * <code>{layer1}{layer2}{layer3}{end-layer3}{end-layer2}{end-layer1}</code>
 * </pre>
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class CombinedRenderPipeline implements RenderPipeline {

   private static final boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);

   private final ListOrderedMap orderedPipes = new ListOrderedMap();
   private final List<CombinedRenderPipe> pipes = new ArrayList<CombinedRenderPipe>();

   private final PostProcessor processor;

   private final FrameBufferFactory bufferFactory;

   private final ShaderConfig config;
   private final SpriteBatch internalBatch;
   private FrameBuffer buffer;
   private OrthographicCamera camera;
   private boolean hasEffects;
   private int bufferWidth;
   private int bufferHeight;
   private final Map<Class<?>, BatchResolver<?>> batchResolverMap = new HashMap<Class<?>, BatchResolver<?>>();

   CombinedRenderPipeline(ShaderConfig config, SpriteBatch internalBatch, OrthographicCamera camera, BatchResolver[] batchResolvers) {
      this(config, new PostProcessor(true, true, isDesktop), new FrameBufferFactory() {

         @Override
         public FrameBuffer create(int width, int height) {
            if (width < 1 || height < 1) {
               return null;
            }
            return new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
         }

      }, internalBatch, camera, batchResolvers);
   }

   public CombinedRenderPipeline(ShaderConfig config, BatchResolver[] batchResolvers) {
      this(config, new PostProcessor(true, true, isDesktop), new FrameBufferFactory() {

         @Override
         public FrameBuffer create(int width, int height) {
            if (width < 1 || height < 1) {
               return null;
            }
            return new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
         }

      }, new SpriteBatch(), new OrthographicCamera(), batchResolvers);
   }

   CombinedRenderPipeline(ShaderConfig config, PostProcessor processor, FrameBufferFactory factory,
                          SpriteBatch internalBatch, OrthographicCamera camera, BatchResolver[] batchResolvers) {
      this.config = config;
      ShaderLoader.BasePath = this.config.basePath;
      ShaderLoader.PathResolver = this.config.pathResolver;
      this.processor = processor;
      this.bufferFactory = factory;
      this.internalBatch = internalBatch;
      this.camera = camera;
      for (BatchResolver resolver : batchResolvers) {
         batchResolverMap.put(resolver.getBatchClass(), resolver);
      }
   }

   @Override
   public void dispose() {
      processor.dispose();
   }

   @SuppressWarnings("unchecked")
   @Override
   public void resize(int width, int height) {
      for (int i = 0; i < pipes.size(); ++i) {
         pipes.get(i).resize(width, height);
      }
      processor.setViewport(new Rectangle(0f, 0f, width, height));
      if (buffer != null) {
         buffer.dispose();
      }
      this.bufferWidth = width;
      this.bufferHeight = height;
      camera.setToOrtho(true, width, height);
   }

   @Override
   public void put(String id, RenderLayer layer, PostProcessorEffect... effects) {
      CombinedRenderPipe pipe = new CombinedRenderPipe(layer, processor, internalBatch, batchResolverMap, effects);
      orderedPipes.put(id, pipe);
      this.hasEffects = hasEffects || effects.length > 0;
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void putAfter(String existing, String id, RenderLayer layer, PostProcessorEffect... effects) {
      int index = orderedPipes.indexOf(existing);
      if (index < 0) {
         Gdx.app.error("FATAL", "Unable add layer '" + id + "'!");
         return;
      }
      orderedPipes.put(index + 1, id, new CombinedRenderPipe(layer, processor, internalBatch, batchResolverMap, effects));
      this.hasEffects = hasEffects || effects.length > 0;
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void putBefore(String existing, String id, RenderLayer layer, PostProcessorEffect... effects) {
      int index = orderedPipes.indexOf(existing);
      if (index < 0) {
         Gdx.app.error("FATAL", "Unable add layer '" + id + "'!");
         return;
      }
      orderedPipes.put(index > 0 ? index - 1 : index, id, new CombinedRenderPipe(layer, processor, internalBatch, batchResolverMap, effects));
      this.hasEffects = hasEffects || effects.length > 0;
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void remove(String existingSourceId) {
      int index = orderedPipes.indexOf(existingSourceId);
      if (index < 0) {
         Gdx.app.error("FATAL", "Unable remove layer '" + existingSourceId + "'!");
         return;
      }
      orderedPipes.remove(existingSourceId);
      for (Object o : orderedPipes.valueList()) {
         CombinedRenderPipe pipe = (CombinedRenderPipe)o;
         if (pipe.hasEffects()) {
            return;
         }
      }
      this.hasEffects = false;
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void setEffects(String existingSourceId, PostProcessorEffect... effects) {
      int index = orderedPipes.indexOf(existingSourceId);
      if (index < 0) {
         Gdx.app.error("FATAL", "Unable remove layer '" + existingSourceId + "'!");
         return;
      }
      getPipe(existingSourceId).setEffects(effects);
      orderedPipes.remove(existingSourceId);
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void moveBefore(String existingSourceId, String existingTargetId) {
      RenderPipe sourcePipe = getPipe(existingSourceId);
      if (sourcePipe == null) {
         Gdx.app.error("FATAL", "source pipe does not exist!");
         return;
      }
      RenderPipe targetPipe = getPipe(existingTargetId);
      if (targetPipe == null) {
         Gdx.app.error("FATAL", "target pipe does not exist!");
         return;
      }
      remove(existingSourceId);
      int index = orderedPipes.indexOf(existingTargetId);
      orderedPipes.put(index, existingSourceId, sourcePipe);
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void moveAfter(String existingSourceId, String existingTargetId) {
      RenderPipe sourcePipe = getPipe(existingSourceId);
      if (sourcePipe == null) {
         Gdx.app.error("FATAL", "source pipe does not exist!");
         return;
      }
      RenderPipe targetPipe = getPipe(existingTargetId);
      if (targetPipe == null) {
         Gdx.app.error("FATAL", "target pipe does not exist!");
         return;
      }
      remove(existingSourceId);
      int index = orderedPipes.indexOf(existingTargetId);
      orderedPipes.put(index + 1, existingSourceId, sourcePipe);
      pipes.clear();
      pipes.addAll(orderedPipes.valueList());
   }

   @Override
   public void addEffects(String existingSourceId, PostProcessorEffect... effects) {
      RenderPipe pipe = getPipe(existingSourceId);
      if (pipe != null) {
         pipe.addEffects(effects);
         this.hasEffects = hasEffects || effects.length > 0;
      }
   }

   @SuppressWarnings("unchecked")
   @Override
   public Collection<String> getPipeIds() {
      return orderedPipes.keySet();
   }

   @SuppressWarnings("unchecked")
   @Override
   public void render(float delta) {
      if (buffer == null && hasEffects) {
         buffer = bufferFactory.create(this.bufferWidth, this.bufferHeight);
      } else if (buffer != null && !hasEffects) {
         buffer.dispose();
         buffer = null;
      }
      clearBuffer();
      for (int i = 0; i < pipes.size(); ++i) {
         CombinedRenderPipe pipe = pipes.get(i);
         pipe.beforeRender();
         pipe.render(delta, buffer);
      }
      if (hasEffects) {
         internalBatch.setProjectionMatrix(camera.combined);
         internalBatch.begin();
         internalBatch.setColor(Color.WHITE);
         internalBatch.draw(buffer.getColorBufferTexture(), 0f, 0f);
         internalBatch.end();
      }
   }

   private void clearBuffer() {
      if (buffer != null) {
         buffer.begin();
         Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
         Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
         buffer.end();
      }
   }

   private RenderPipe getPipe(String id) {
      return (RenderPipe) (orderedPipes.containsKey(id) ? orderedPipes.get(id) : null);
   }
}
