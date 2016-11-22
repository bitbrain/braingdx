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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.bitbrain.braingdx.graphics.shader.ShaderManager;
import de.bitbrain.braingdx.postprocessing.PostProcessor;
import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.Resizeable;

class CombinedRenderPipe implements RenderPipe, Resizeable {

    private final RenderLayer layer;

    private final ShaderManager shaderManager;

    private boolean enabled = true;

    public CombinedRenderPipe(RenderLayer layer, PostProcessor processor, PostProcessorEffect... effects) {
	this.layer = layer;
	this.shaderManager = new ShaderManager(processor, effects);
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
	shaderManager.addEffects(effects);
    }

    public void render(Batch batch, float delta, FrameBuffer buffer) {
	if (isEnabled()) {
	    if (shaderManager.hasEffects()) {
		shaderManager.begin();
		batch.begin();
		batch.draw(buffer.getColorBufferTexture(), 0f, 0f);
		batch.end();
		layer.render(batch, delta);
		shaderManager.end(buffer);
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
