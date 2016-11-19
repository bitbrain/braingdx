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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderManager;
import de.bitbrain.braingdx.postprocessing.PostProcessor;
import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.Resizeable;

/**
 * Layered implementation of {@link RenderPipe}.
 * 
 * @TODO Fix ensure shader transparency.
 * 
 * @author Miguel Gonzalez Sanchez
 */
class LayeredRenderPipe implements RenderPipe, Disposable, Resizeable {
    private RenderLayer renderLayer;

    private ShaderManager shaderManager;

    private FrameBuffer buffer;

    private final FrameBufferFactory bufferFactory;

    private boolean enabled;

    LayeredRenderPipe(RenderLayer layer, PostProcessor processor, FrameBufferFactory factory,
	    PostProcessorEffect... effects) {
	this.renderLayer = layer;
	this.shaderManager = new ShaderManager(processor, effects);
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
	if (shaderManager.hasEffects() && buffer != null) {
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
	shaderManager.addEffects(effects);
    }

    @Override
    public void dispose() {
	buffer.dispose();
    }

    private void renderOntoBuffer(Batch batch, float delta) {
	shaderManager.begin();
	renderLayer.render(batch, delta);
	shaderManager.end(buffer);
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
