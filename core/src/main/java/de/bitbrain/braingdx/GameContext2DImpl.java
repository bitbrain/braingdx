/* Copyright 2017 Miguel Gonzalez Sanchez
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

import aurelienribon.tweenengine.TweenManager;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.audio.AudioManagerImpl;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.BehaviorManagerAdapter;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventManagerImpl;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManagerAdapter;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.pipeline.CombinedRenderPipelineFactory;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.postprocessing.ShaderManager;
import de.bitbrain.braingdx.graphics.shader.BatchPostProcessor;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screens.ScreenTransitions;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.tmx.TiledMapManagerImpl;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.Resizeable;
import de.bitbrain.braingdx.util.ViewportFactory;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * 2D Implementation of {@link GameContext}.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.1.0
 */
public class GameContext2DImpl implements GameContext, Disposable, Resizeable {

   private final GameWorld world;
   private final BehaviorManager behaviorManager;
   private final GameObjectRenderManager renderManager;
   private final GameCamera gameCamera;
   private final OrthographicCamera camera;
   private final Batch batch;
   private final Stage stage, worldStage;
   private final RenderPipeline renderPipeline;
   private final LightingManager lightingManager;
   private final ParticleManager particleManager;
   private final World boxWorld;
   private final TiledMapManager tiledMapManager;
   private final TweenManager tweenManager = SharedTweenManager.getInstance();
   private final InputMultiplexer input;
   private final GameEventManager eventManager;
   private final AudioManager audioManager;
   private final GameSettings settings;
   private final ShaderManager shaderManager;

   public GameContext2DImpl(ViewportFactory viewportFactory, ShaderConfig shaderConfig) {
      eventManager = new GameEventManagerImpl();
      settings = new GameSettings(eventManager);
      shaderManager = new ShaderManager(eventManager, settings.getGraphics());
      camera = new OrthographicCamera();
      world = new GameWorld(camera);
      behaviorManager = new BehaviorManager(world);
      batch = new SpriteBatch();
      input = new InputMultiplexer();
      boxWorld = new World(Vector2.Zero, false);
      lightingManager = new LightingManager(new RayHandler(boxWorld), camera);
      renderManager = new GameObjectRenderManager(batch);
      gameCamera = new VectorGameCamera(camera, world);
      particleManager = new ParticleManager(behaviorManager);
      stage = new Stage(viewportFactory.create(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
      Viewport worldStageViewport = viewportFactory.create(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      worldStageViewport.setCamera(camera);
      worldStage = new Stage(worldStageViewport);
      renderPipeline = new CombinedRenderPipelineFactory(//
            shaderConfig,//
            world,//
            lightingManager,//
            particleManager,//
            stage,//
            worldStage,//
            viewportFactory//
      ).create();
      tiledMapManager = new TiledMapManagerImpl(behaviorManager, world, renderManager, eventManager);
      audioManager = new AudioManagerImpl(//
            gameCamera,//
            tweenManager,//
            SharedAssetManager.getInstance(),//
            world,//
            behaviorManager//
      );      
      wire();
   }

   @Override
   public GameWorld getGameWorld() {
      return world;
   }

   @Override
   public Stage getWorldStage() {
      return worldStage;
   }

   @Override
   public Stage getStage() {
      return stage;
   }

   @Override
   public RenderPipeline getRenderPipeline() {
      return renderPipeline;
   }

   @Override
   public World getBox2DWorld() {
      return boxWorld;
   }

   @Override
   public ParticleManager getParticleManager() {
      return particleManager;
   }

   @Override
   public TweenManager getTweenManager() {
      return tweenManager;
   }

   @Override
   public BehaviorManager getBehaviorManager() {
      return behaviorManager;
   }

   @Override
   public GameObjectRenderManager getRenderManager() {
      return renderManager;
   }

   @Override
   public GameCamera getGameCamera() {
      return gameCamera;
   }

   @Override
   public LightingManager getLightingManager() {
      return lightingManager;
   }

   @Override
   public InputMultiplexer getInput() {
      return input;
   }

   @Override
   public TiledMapManager getTiledMapManager() {
      return tiledMapManager;
   }

   @Override
   public ScreenTransitions getScreenTransitions() {
      return ScreenTransitions.getInstance();
   }

   @Override
   public AudioManager getAudioManager() {
      return audioManager;
   }

   @Override
   public void dispose() {
      world.clear();
      stage.dispose();
      worldStage.dispose();
      input.clear();
      particleManager.dispose();
      renderPipeline.dispose();
      tweenManager.killAll();
      renderManager.dispose();
      eventManager.clear();
   }

   @Override
   public void updateAndRender(float delta) {
      tweenManager.update(delta);
      gameCamera.update(delta);
      stage.act(delta);
      worldStage.act(delta);
      batch.setProjectionMatrix(camera.combined);
      renderPipeline.render(batch, delta);
   }

   @Override
   public void resize(int width, int height) {
      stage.getViewport().update(width, height);
      worldStage.getViewport().update(width, height);
      renderPipeline.resize(width, height);
      gameCamera.resize(width, height);
   }

   private void wire() {
      world.addListener(new BehaviorManagerAdapter(behaviorManager));
      world.addListener(new GameObjectRenderManagerAdapter(renderManager));
      input.addProcessor(stage);
      input.addProcessor(worldStage);
      Gdx.input.setInputProcessor(input);
   }

   @Override
   public GameEventManager getEventManager() {
      return eventManager;
   }

   @Override
   public GameSettings getSettings() {
      return settings;
   }

   @Override
   public ShaderManager getShaderManager() {
      return shaderManager;
   }
}
