/* Copyright 2016 Miguel Gonzalez Sanchez
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

import java.util.Collection;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;

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

    @Override
    public void dispose() {
	// TODO Auto-generated method stub

    }

    @Override
    public void resize(int width, int height) {
	// TODO Auto-generated method stub

    }

    @Override
    public void add(String id, RenderLayer layer, PostProcessorEffect... effects) {
	// TODO Auto-generated method stub

    }

    @Override
    public RenderPipe getPipe(String id) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public Collection<String> getPipeIds() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void render(Batch batch, float delta) {
	// TODO Auto-generated method stub

    }
}
