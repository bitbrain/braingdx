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
import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Rectangle;

import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.postprocessing.PostProcessor;
import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.util.ShaderLoader;

/**
 * Layered render pipeline implementation.
 * 
 * @TODO Fix ensure shader transparency.
 * 
 * @author Miguel Gonzalez Sanchez
 */
public class LayeredRenderPipeline implements RenderPipeline {

    private static final boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);

    private final Map<String, LayeredRenderPipe> pipes;

    private final ShaderConfig config;

    private final PostProcessor processor;

    private final FrameBufferFactory bufferFactory;

    public LayeredRenderPipeline(ShaderConfig config) {
	this(config, new PostProcessor(true, true, isDesktop), new FrameBufferFactory() {

	    @Override
	    public FrameBuffer create(int width, int height) {
		return new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	    }

	});
    }

    LayeredRenderPipeline(ShaderConfig config, PostProcessor processor, FrameBufferFactory factory) {
	this.pipes = new LinkedHashMap<String, LayeredRenderPipe>();
	this.config = config;
	ShaderLoader.BasePath = this.config.basePath;
	ShaderLoader.PathResolver = this.config.pathResolver;
	this.processor = processor;
	this.bufferFactory = factory;
    }

    @Override
    public void add(String id, RenderLayer layer, PostProcessorEffect... effects) {
	LayeredRenderPipe pipe = new LayeredRenderPipe(layer, processor, bufferFactory, effects);
	pipes.put(id, pipe);
    }

    @Override
    public RenderPipe getPipe(String id) {
	return pipes.get(id);
    }

    @Override
    public Collection<String> getPipeIds() {
	return pipes.keySet();
    }

    @Override
    public void render(Batch batch, float delta) {
	for (LayeredRenderPipe pipe : pipes.values()) {
	    if (pipe.isEnabled()) {
		pipe.render(batch, delta);
	    }
	}
    }

    @Override
    public void resize(int width, int height) {
	processor.setViewport(new Rectangle(0f, 0f, width, height));
	for (LayeredRenderPipe pipe : pipes.values()) {
	    pipe.resize(width, height);
	}
    }

    @Override
    public void dispose() {
	for (LayeredRenderPipe pipe : pipes.values()) {
	    pipe.dispose();
	}
	processor.dispose();
    }
}
