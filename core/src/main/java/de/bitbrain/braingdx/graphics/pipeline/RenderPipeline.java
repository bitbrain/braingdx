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
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.Resizeable;

import java.util.Collection;

/**
 * Handles the complete pipeline of rendering internally. The pipeline consists of render pipes.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public interface RenderPipeline extends Disposable, Resizeable {

   /**
    * Registers a new {@link RenderLayer} to this pipeline
    *
    * @param id a unique id of the layer
    * @param layer the layer object
    * @param effects additional shader effects to apply to the given layer
    */
   void put(String id, RenderLayer layer, PostProcessorEffect... effects);

   /**
    * Registers a new {@link RenderLayer} to this pipeline, placed above the existing layer
    *
    * @param existingSourceId an existing layer
    * @param id a unique id of the layer
    * @param layer the layer object
    * @param effects additional shader effects to apply to the given layer
    */
   void putAfter(String existingSourceId, String id, RenderLayer layer, PostProcessorEffect... effects);

   /**
    * Registers a new {@link RenderLayer} to this pipeline, placed beneath the existing layer
    *
    * @param existingSourceId an existing layer
    * @param id a unique id of the layer
    * @param layer the layer object
    * @param effects additional shader effects to apply to the given layer
    */
   void putBefore(String existingSourceId, String id, RenderLayer layer, PostProcessorEffect... effects);

   /**
    * Removes an existing render layer from this pipeline.
    *
    * @param existingSourceId an existing layer id
    */
   void remove(String existingSourceId);

   /**
    * Override all effects on a given layer
    *
    * @param existingSourceId an existing layer
    * @param effects additional shader effects to apply to the given layer
    */
   void setEffects(String existingSourceId, PostProcessorEffect... effects);

   /**
    * Moves an existing layer right beneath another existing layer
    *
    * @param existingSourceId an existing layer
    * @param existingTargetId an existing layer
    */
   void moveBefore(String existingSourceId, String existingTargetId);

   /**
    * Moves an existing layer right above another existing layer
    *
    * @param existingSourceId an existing layer
    * @param existingTargetId an existing layer
    */
   void moveAfter(String existingSourceId, String existingTargetId);

   /**
    * Returns an registered render pipe
    *
    * @param existingSourceId an existing layer
    * @return an existing {@link RenderPipe}
    */
   RenderPipe getPipe(String existingSourceId);

   /**
    * Returns an ordered collection of all registered layers
    *
    * @return an ordered collection of Strings
    */
   Collection<String> getPipeIds();

   /**
    * Renders this pipeline onto the screen
    *
    * @param batch a given batch
    * @param delta the frame delta
    */
   void render(Batch batch, float delta);
}
