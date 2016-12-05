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

package de.bitbrain.braingdx.screens;

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
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.BehaviorManagerAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManagerAdapter;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.pipeline.CombinedRenderPipelineFactory;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipelineFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Abstract base class for screens
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public abstract class AbstractScreen<T extends BrainGdxGame> implements Screen {

    private T game;
    private GameWorld world;
    private BehaviorManager behaviorManager;
    private GameObjectRenderManager renderManager;
    private GameCamera gameCamera;
    private OrthographicCamera camera;
    private Color backgroundColor = Color.BLACK.cpy();
    private Batch batch;
    private Stage stage;
    private RenderPipeline renderPipeline;
    private LightingManager lightingManager;
    private World boxWorld;

    private boolean uiInitialized = false;

    public AbstractScreen(T game) {
	this.game = game;
    }

    public T getGame() {
	return game;
    }

    protected TweenManager tweenManager = SharedTweenManager.getInstance();
    protected InputMultiplexer input;

    @Override
    public final void show() {
	camera = new OrthographicCamera();
	world = new GameWorld(camera);
	behaviorManager = new BehaviorManager();
	batch = new SpriteBatch();
	input = new InputMultiplexer();
	boxWorld = new World(Vector2.Zero, false);
	lightingManager = new LightingManager(boxWorld, camera);
	renderManager = new GameObjectRenderManager(batch);
	gameCamera = new VectorGameCamera(camera);
	stage = new Stage();
	renderPipeline = getRenderPipelineFactory().create();
	wire();
    }

    @Override
    public final void render(float delta) {
	Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	onUpdate(delta);
	tweenManager.update(delta);
	gameCamera.update(delta);
	stage.act(delta);
	batch.setProjectionMatrix(camera.combined);
	renderPipeline.render(batch, delta);
    }

    @Override
    public final void resize(int width, int height) {
	if (!uiInitialized) {
	    input.addProcessor(stage);
	    onCreateStage(stage, width, height);
	    Gdx.input.setInputProcessor(input);
	    uiInitialized = true;
	}
	stage.getViewport().update(width, height);
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

    public GameWorld getGameWorld() {
	return world;
    }

    public Color getBackgroundColor() {
	return backgroundColor;
    }

    public Stage getStage() {
	return stage;
    }

    public RenderPipeline getRenderPipeline() {
	return renderPipeline;
    }

    public World getBoxWorld() {
	return boxWorld;
    }

    public TweenManager getTweenManager() {
	return tweenManager;
    }

    public BehaviorManager getBehaviorManager() {
	return behaviorManager;
    }

    public GameObjectRenderManager getRenderManager() {
	return renderManager;
    }

    public GameCamera getGameCamera() {
	return gameCamera;
    }

    public LightingManager getLightingManager() {
	return lightingManager;
    }

    protected void onCreateStage(Stage stage, int width, int height) {

    }

    protected void onUpdate(float delta) {

    }

    protected ShaderConfig getShaderConfig() {
	return new ShaderConfig();
    }

    protected Viewport getViewport(int width, int height) {
	return new ScreenViewport();
    }

    @Override
    public void dispose() {
	world.clear();
	stage.dispose();
	input.clear();
	renderPipeline.dispose();
    }

    public void setBackgroundColor(Color color) {
	this.backgroundColor = color;
    }
    


    private void wire() {
	world.addListener(new BehaviorManagerAdapter(behaviorManager));
	world.addListener(new GameObjectRenderManagerAdapter(renderManager));
    }

    protected RenderPipelineFactory getRenderPipelineFactory() {
	return new CombinedRenderPipelineFactory(getShaderConfig(), world, lightingManager, stage, camera);
    }
}
