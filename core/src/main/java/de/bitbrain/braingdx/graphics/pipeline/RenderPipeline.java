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
import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.Resizeable;

import java.util.Collection;

/**
 * Handles the complete pipeline of rendering internally. The pipeline consists of render pipes.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public interface RenderPipeline extends Disposable, Resizeable {

   public void put(String id, RenderLayer layer, PostProcessorEffect... effects);
   
   public void putAfter(String existing, String id, RenderLayer layer, PostProcessorEffect... effects);
   
   public void putBefore(String existing, String id, RenderLayer layer, PostProcessorEffect... effects);

   public RenderPipe getPipe(String id);

   public Collection<String> getPipeIds();

   public void render(Batch batch, float delta);
}
