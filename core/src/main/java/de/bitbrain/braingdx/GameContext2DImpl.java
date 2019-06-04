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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
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
import de.bitbrain.braingdx.graphics.event.GraphicsSettingsChangeEvent;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.pipeline.CombinedRenderPipelineFactory;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.postprocessing.ShaderManager;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.input.InputManager;
import de.bitbrain.braingdx.input.InputManagerImpl;
import de.bitbrain.braingdx.physics.PhysicsManager;
import de.bitbrain.braingdx.physics.PhysicsManagerImpl;
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
   private final OrthographicCamera uiCamera;
   private final Batch batch;
   private final Stage stage, worldStage;
   private final RenderPipeline renderPipeline;
   private final LightingManager lightingManager;
   private final ParticleManager particleManager;
   private final World boxWorld;
   private final TiledMapManager tiledMapManager;
   private final TweenManager tweenManager = SharedTweenManager.getInstance();
   private final InputManagerImpl inputManager;
   private final GameEventManager eventManager;
   private final AudioManager audioManager;
   private final GameSettings settings;
   private final ShaderManager shaderManager;
   private final PhysicsManagerImpl physicsManager;

   public GameContext2DImpl(ViewportFactory viewportFactory, ShaderConfig shaderConfig) {
      eventManager = new GameEventManagerImpl();
      settings = new GameSettings(eventManager);
      shaderManager = new ShaderManager(eventManager, settings.getGraphics());
      camera = new OrthographicCamera();
      uiCamera = new OrthographicCamera();
      world = new GameWorld();
      behaviorManager = new BehaviorManager(world);
      batch = new SpriteBatch();
      inputManager = new InputManagerImpl();
      renderManager = new GameObjectRenderManager(batch);
      gameCamera = new VectorGameCamera(camera, world);
      particleManager = new ParticleManager(behaviorManager, settings.getGraphics());
      stage = new Stage(viewportFactory.create(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), uiCamera));
      worldStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera));
      boxWorld = new World(Vector2.Zero, true);
      physicsManager = new PhysicsManagerImpl(boxWorld, world, behaviorManager);
      lightingManager = new LightingManager(new RayHandler(boxWorld), camera);
      renderPipeline = new CombinedRenderPipelineFactory(//
            shaderConfig,//
            world,//
            lightingManager,//
            particleManager,//
            stage,//
            worldStage
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
   public InputManager getInputManager() {
      return inputManager;
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
      inputManager.dispose();
      particleManager.dispose();
      renderPipeline.dispose();
      tweenManager.killAll();
      renderManager.dispose();
      eventManager.clear();
      physicsManager.dispose();
      lightingManager.dispose();
   }

   @Override
   public void updateAndRender(float delta) {
      physicsManager.update(delta);
      inputManager.update(delta);
      behaviorManager.update(delta);
      tweenManager.update(delta);
      gameCamera.update(delta);
      uiCamera.update();
      stage.act(delta);
      worldStage.act(delta);
      batch.setProjectionMatrix(camera.combined);
      renderPipeline.render(batch, delta);
   }

   @Override
   public void resize(int width, int height) {
      gameCamera.resize(width, height);
      renderPipeline.resize(width, height);
      uiCamera.setToOrtho(false, width, height);
      stage.getViewport().update(width, height, true);
      worldStage.getViewport().update(width, height, true);
      eventManager.publish(new GraphicsSettingsChangeEvent());
   }

   private void wire() {
      world.addListener(new BehaviorManagerAdapter(behaviorManager));
      world.addListener(new GameObjectRenderManagerAdapter(renderManager));
      inputManager.register(stage);
      inputManager.register(worldStage);
      Gdx.input.setInputProcessor(inputManager.getMultiplexer());
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

   @Override
   public PhysicsManager getPhysicsManager() {
      return physicsManager;
   }
}
