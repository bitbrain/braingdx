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

package de.bitbrain.braingdx.context;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.VectorGameCamera;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerRenderLayer;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.CombinedRenderPipeline2DFactory;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.pipeline.layers.ColoredRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.StageRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.WorldRenderLayer;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.physics.PhysicsManager;
import de.bitbrain.braingdx.physics.PhysicsManagerImpl;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.tmx.TiledMapManagerImpl;
import de.bitbrain.braingdx.util.ArgumentFactory;
import de.bitbrain.braingdx.util.Resizeable;
import de.bitbrain.braingdx.util.ViewportFactory;

/**
 * 2D Implementation of {@link GameContext}.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.1.0
 */
public class GameContext2DImpl extends GameContextImpl implements GameContext2D, Disposable, Resizeable {

   private final Batch batch;
   private final GameObjectRenderManager renderManager;
   private final Stage worldStage;
   private final LightingManager lightingManager;
   private final ParticleManager particleManager;
   private final World boxWorld;
   private final TiledMapManager tiledMapManager;
   private final PhysicsManagerImpl physicsManager;
   private final ColoredRenderLayer coloredRenderLayer;
   private final RenderPipeline renderPipeline;

   public GameContext2DImpl(ViewportFactory viewportFactory, ShaderConfig shaderConfig) {
      super(shaderConfig, viewportFactory, new ArgumentFactory<GameContext, GameCamera>() {
         @Override
         public GameCamera create(GameContext context) {
            return new VectorGameCamera(new OrthographicCamera(), context.getGameWorld());
         }
      });
      coloredRenderLayer = new ColoredRenderLayer();
      batch = new SpriteBatch();
      renderManager = new GameObjectRenderManager(batch);
      particleManager = new ParticleManager(getBehaviorManager(), getSettings().getGraphics());
      worldStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
      boxWorld = new World(Vector2.Zero, true);
      physicsManager = new PhysicsManagerImpl(
            boxWorld,
            getGameWorld(),
            getBehaviorManager()
      );
      lightingManager = new LightingManager(
            new RayHandler(boxWorld),
            (OrthographicCamera) getGameCamera().getInternalCamera()
      );
      renderPipeline = new CombinedRenderPipeline2DFactory(batch).create(this);
      configurePipeline(renderPipeline, this);
      tiledMapManager = new TiledMapManagerImpl(
            getBehaviorManager(),
            getGameWorld(),
            renderManager,
            getEventManager()
      );
   }

   @Override
   public Stage getWorldStage() {
      return worldStage;
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
   public GameObjectRenderManager getRenderManager() {
      return renderManager;
   }

   @Override
   public RenderPipeline getRenderPipeline() {
      return renderPipeline;
   }

   @Override
   public LightingManager getLightingManager() {
      return lightingManager;
   }

   @Override
   public TiledMapManager getTiledMapManager() {
      return tiledMapManager;
   }

   @Override
   public void dispose() {
      super.dispose();
      worldStage.dispose();
      particleManager.dispose();
      renderManager.dispose();
      physicsManager.dispose();
      lightingManager.dispose();
      renderPipeline.dispose();
   }

   public void updateAndRender(float delta) {
      physicsManager.update(delta);
      worldStage.act(delta);
      renderPipeline.render(delta);
   }

   @Override
   public void setBackgroundColor(Color color) {
      super.setBackgroundColor(color);
      coloredRenderLayer.setColor(color);
      getRenderPipeline().put(RenderPipeIds.BACKGROUND, coloredRenderLayer);
   }

   @Override
   public void resize(int width, int height) {
      super.resize(width, height);
      renderPipeline.resize(width, height);
      worldStage.getViewport().update(width, height, true);
   }

   @Override
   public PhysicsManager getPhysicsManager() {
      return physicsManager;
   }

   private void configurePipeline(RenderPipeline pipeline, GameContext2D context) {
      pipeline.put(RenderPipeIds.BACKGROUND, new RenderLayer() {
         @Override
         public void render(float delta) {
         }
      });
      pipeline.put(RenderPipeIds.FOREGROUND, new RenderLayer() {
         @Override
         public void render(float delta) {
            // noOp
         }
      });
      pipeline.put(RenderPipeIds.WORLD, new WorldRenderLayer(context.getGameWorld(), batch));
      pipeline.put(RenderPipeIds.LIGHTING, new LightingManagerRenderLayer(context.getLightingManager()));
      pipeline.put(RenderPipeIds.PARTICLES, new ParticleManagerRenderLayer(context.getParticleManager(), batch));
      pipeline.put(RenderPipeIds.WORLD_UI, new StageRenderLayer(context.getWorldStage()));
      pipeline.put(RenderPipeIds.UI, new StageRenderLayer(context.getStage()));
   }
}
