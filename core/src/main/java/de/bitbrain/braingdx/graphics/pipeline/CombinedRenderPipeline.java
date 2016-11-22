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
import com.badlogic.gdx.graphics.Color;
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

    private final Map<String, CombinedRenderPipe> pipes = new LinkedHashMap<String, CombinedRenderPipe>();

    private final PostProcessor processor;

    private final FrameBufferFactory bufferFactory;

    private final ShaderConfig config;

    private FrameBuffer buffer;

    public CombinedRenderPipeline(ShaderConfig config) {
	this(config, new PostProcessor(true, true, isDesktop), new FrameBufferFactory() {

	    @Override
	    public FrameBuffer create(int width, int height) {
		return new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
	    }

	});
    }

    CombinedRenderPipeline(ShaderConfig config, PostProcessor processor, FrameBufferFactory factory) {
	this.config = config;
	ShaderLoader.BasePath = this.config.basePath;
	ShaderLoader.PathResolver = this.config.pathResolver;
	this.processor = processor;
	this.bufferFactory = factory;
    }

    @Override
    public void dispose() {
	processor.dispose();
    }

    @Override
    public void resize(int width, int height) {
	for (CombinedRenderPipe pipe : pipes.values()) {
	    pipe.resize(width, height);
	}
	processor.setViewport(new Rectangle(0f, 0f, width, height));
	if (buffer != null) {
	    buffer.dispose();
	}
	buffer = bufferFactory.create(width, height);
    }

    @Override
    public void add(String id, RenderLayer layer, PostProcessorEffect... effects) {
	CombinedRenderPipe pipe = new CombinedRenderPipe(layer, processor, effects);
	pipes.put(id, pipe);
    }

    @Override
    public RenderPipe getPipe(String id) {
	return pipes.getOrDefault(id, null);
    }

    @Override
    public Collection<String> getPipeIds() {
	return pipes.keySet();
    }

    @Override
    public void render(Batch batch, float delta) {
	for (CombinedRenderPipe pipe : pipes.values()) {
	    pipe.beforeRender();
	    pipe.render(batch, delta, buffer);
	}
	batch.begin();
	batch.setColor(Color.WHITE);
	batch.draw(buffer.getColorBufferTexture(), 0f, 0f);
	batch.end();
    }
}
