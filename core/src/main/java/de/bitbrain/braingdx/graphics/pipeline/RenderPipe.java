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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.PostProcessorEffect;

import de.bitbrain.braingdx.graphics.shader.ShaderManager;

/**
 * A renderpipe is compatible with other pipes and is responsible for rendering internal layers.
 * These layers have two types. First there is the rendering layer and afterwards the shader layer
 * is applied.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class RenderPipe implements Disposable {

    private RenderLayer renderLayer;

    private ShaderManager shaderManager;

    private FrameBuffer buffer;

    private boolean enabled;

    public RenderPipe(RenderLayer layer, PostProcessor processor, PostProcessorEffect... effects) {
	this.renderLayer = layer;
	this.shaderManager = new ShaderManager(processor, effects);
	this.setEnabled(true);
    }

    public boolean isEnabled() {
	return enabled;
    }

    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    public void render(Batch batch, float delta) {
	if (buffer != null && isEnabled()) {
	    shaderManager.begin();
	    renderLayer.render(batch, delta);
	    shaderManager.end(buffer);
	    batch.begin();
	    batch.setColor(Color.WHITE);
	    batch.draw(buffer.getColorBufferTexture(), 0f, 0f);
	    batch.end();
	}
    }

    public void resize(int width, int height) {
	if (buffer != null) {
	    buffer.dispose();
	}
	this.buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
		false);
    }

    public void addEffects(PostProcessorEffect... effects) {
	shaderManager.addEffects(effects);
    }

    @Override
    public void dispose() {
	buffer.dispose();
    }
}
