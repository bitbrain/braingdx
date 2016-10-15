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
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.PostProcessorEffect;
import com.bitfire.utils.ShaderLoader;

import de.bitbrain.braingdx.graphics.shader.ShaderConfig;

/**
 * Handles the complete pipeline of rendering internally. The pipeline consists of render pipes.
 * Each pipe consists of two layers: the rendering layer and the shader layer which gets applied
 * afterwards.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class RenderPipeline implements Disposable {

    private static final boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);

    private Map<String, RenderPipe> pipes;

    private ShaderConfig config;

    private PostProcessor processor;

    public RenderPipeline(ShaderConfig config) {
	this.pipes = new LinkedHashMap<String, RenderPipe>();
	this.config = config;
	ShaderLoader.BasePath = this.config.basePath;
	ShaderLoader.PathResolver = this.config.pathResolver;
	this.processor = new PostProcessor(true, true, isDesktop);
    }

    public void add(String id, RenderLayer layer, PostProcessorEffect... effects) {
	RenderPipe pipe = new RenderPipe(layer, processor, effects);
	pipes.put(id, pipe);
    }

    public RenderPipe getPipe(String id) {
	return pipes.get(id);
    }

    public Collection<String> getPipeIds() {
	return pipes.keySet();
    }

    public void render(Batch batch, float delta) {
	for (RenderPipe pipe : pipes.values()) {
	    if (pipe.isEnabled()) {
		pipe.render(batch, delta);
	    }
	}
    }

    public void resize(int width, int height) {
	processor.setViewport(new Rectangle(0f, 0f, width, height));
	for (RenderPipe pipe : pipes.values()) {
	    pipe.resize(width, height);
	}
    }


    @Override
    public void dispose() {
	for (RenderPipe pipe : pipes.values()) {
	    pipe.dispose();
	}
	processor.dispose();
    }
}
