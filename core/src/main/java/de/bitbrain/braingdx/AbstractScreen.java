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

package de.bitbrain.braingdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.ui.Tooltip;

/**
 * Abstract base class for screens
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public abstract class AbstractScreen<T extends BrainGdxGame> implements Screen {

    public static final String PIPE_LIGHTING = "lighting";
    public static final String PIPE_WORLD = "world";
    public static final String PIPE_UI = "ui";

    protected T game;

    protected GameWorld world;

    protected OrthographicCamera camera;

    private Color backgroundColor = Color.BLACK.cpy();

    private Batch batch;

    private Stage stage;

    protected RenderPipeline renderPipeline;

    protected LightingManager lightingManager;

    protected World boxWorld;

    public AbstractScreen(T game) {
	this.game = game;
    }

    public T getGame() {
	return game;
    }

    protected TweenManager tweenManager = SharedTweenManager.getInstance();
    protected Tooltip tooltip = Tooltip.getInstance();
    protected InputMultiplexer input;

    @Override
    public final void show() {
	camera = new OrthographicCamera();
	world = new GameWorld(camera);
	batch = new SpriteBatch();
	input = new InputMultiplexer();
	renderPipeline = new RenderPipeline(getShaderConfig());
	boxWorld = new World(Vector2.Zero, false);
	lightingManager = new LightingManager(boxWorld, camera);
	buildLayers();
    }

    @Override
    public final void render(float delta) {
	Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	tweenManager.update(delta);
	camera.update();
	stage.act(delta);
	batch.setProjectionMatrix(camera.combined);
	renderPipeline.render(batch, delta);
    }

    @Override
    public final void resize(int width, int height) {
	if (stage == null) {
	    stage = new Stage(getViewport(width, height));
	    input.addProcessor(stage);
	    tooltip.init(stage, camera);
	    onCreateStage(stage, width, height);
	    Gdx.input.setInputProcessor(input);
	} else {
	    stage.getViewport().update(width, height);
	}
	renderPipeline.resize(width, height);
	camera.setToOrtho(true, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    protected void beforeWorldRender(Batch batch, float delta) {

    }

    protected void afterWorldRender(Batch batch, float delta) {

    }

    protected void onCreateStage(Stage stage, int width, int height) {

    }

    protected ShaderConfig getShaderConfig() {
	return new ShaderConfig();
    }

    protected Viewport getViewport(int width, int height) {
	return new ScreenViewport();
    }

    @Override
    public void dispose() {
	world.reset();
	stage.dispose();
	input.clear();
	renderPipeline.dispose();
    }

    public void setBackgroundColor(Color color) {
	this.backgroundColor = color;
    }

    private void buildLayers() {
	// 1. World layer
	renderPipeline.add(PIPE_WORLD, new RenderLayer() {
	    @Override
	    public void render(Batch batch, float delta) {
		batch.begin();
		beforeWorldRender(batch, delta);
		world.updateAndRender(batch, delta);
		afterWorldRender(batch, delta);
		batch.end();
	    }
	});
	// 2. Lighting layer
	renderPipeline.add(PIPE_LIGHTING, lightingManager);
	// 3. UI layer
	renderPipeline.add(PIPE_UI, new RenderLayer() {
	    @Override
	    public void render(Batch batch, float delta) {
		stage.draw();
	    }

	});
    }
}
