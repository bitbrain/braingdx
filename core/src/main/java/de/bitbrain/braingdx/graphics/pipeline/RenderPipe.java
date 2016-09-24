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
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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
public class RenderPipe {

    private RenderLayer renderLayer;

    private ShaderManager shaderManager;

    private FrameBuffer buffer;

    public RenderPipe(RenderLayer layer, PostProcessorEffect... effects) {
	this.renderLayer = layer;
	this.shaderManager = new ShaderManager("postprocessing/shaders/", effects);
	this.buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
    }

    public void render(Batch batch, float delta) {
	buffer.begin();
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	renderLayer.render(batch, delta);
	buffer.end();
	this.shaderManager.begin();
	batch.begin();
	batch.draw(buffer.getColorBufferTexture(), 0f, 0f);
	batch.end();
	this.shaderManager.end();
    }

    public void addEffects(PostProcessorEffect... effects) {
	shaderManager.addEffects(effects);
    }
}
