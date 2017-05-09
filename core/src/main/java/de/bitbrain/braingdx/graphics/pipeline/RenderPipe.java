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

import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;

/**
 * A renderpipe is compatible with other pipes and is responsible for rendering internal layers.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public interface RenderPipe {

   public boolean isEnabled();

   void setEnabled(boolean enabled);

   void addEffects(PostProcessorEffect... effects);
}
