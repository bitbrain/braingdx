package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerRenderLayer;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.StageRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.WorldRenderLayer;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.util.ViewportFactory;
import de.bitbrain.braingdx.world.GameWorld;

public class CombinedRenderPipelineFactory implements RenderPipelineFactory {

   private final ShaderConfig config;

   private final GameWorld world;

   private final LightingManager lightingManager;

   private final ParticleManager particleManager;

   private final Stage stage;

   private final Stage worldStage;

   public CombinedRenderPipelineFactory(ShaderConfig config, GameWorld world, LightingManager lightingManager, ParticleManager particleManager,
                                        Stage stage, Stage worldStage) {
      this.config = config;
      this.world = world;
      this.lightingManager = lightingManager;
      this.particleManager = particleManager;
      this.stage = stage;
      this.worldStage = worldStage;
   }

   @Override
   public RenderPipeline create() {
      RenderPipeline pipeline = new CombinedRenderPipeline(config);
      pipeline.put(RenderPipeIds.BACKGROUND, new AbstractRenderLayer() {
         @Override
         public void render(Batch batch, float delta) {
         }
      });
      pipeline.put(RenderPipeIds.FOREGROUND, new AbstractRenderLayer() {
         @Override
         public void render(Batch batch, float delta) {
            // noOp
         }
      });
      pipeline.put(RenderPipeIds.WORLD, new WorldRenderLayer(world));
      pipeline.put(RenderPipeIds.LIGHTING, new LightingManagerRenderLayer(lightingManager));
      pipeline.put(RenderPipeIds.PARTICLES, new ParticleManagerRenderLayer(particleManager));
      pipeline.put(RenderPipeIds.WORLD_UI, new StageRenderLayer(worldStage));
      pipeline.put(RenderPipeIds.UI, new StageRenderLayer(stage));
      return pipeline;
   }

}
