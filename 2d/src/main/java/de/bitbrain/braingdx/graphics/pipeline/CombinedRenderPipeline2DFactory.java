package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.StageRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.WorldRenderLayer;
import de.bitbrain.braingdx.graphics.particles.ParticleManagerRenderLayer;
import de.bitbrain.braingdx.util.ArgumentFactory;

public class CombinedRenderPipeline2DFactory implements ArgumentFactory<GameContext2D, RenderPipeline> {

   private final Batch batch;

   public CombinedRenderPipeline2DFactory(Batch batch) {
      this.batch = batch;
   }
   @Override
   public RenderPipeline create(GameContext2D context) {
      RenderPipeline pipeline = new CombinedRenderPipeline(context.getShaderManager().getConfig(), batch);
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
      pipeline.put(RenderPipeIds.WORLD, new WorldRenderLayer(context.getGameWorld()));
      pipeline.put(RenderPipeIds.LIGHTING, new LightingManagerRenderLayer(context.getLightingManager()));
      pipeline.put(RenderPipeIds.PARTICLES, new ParticleManagerRenderLayer(context.getParticleManager()));
      pipeline.put(RenderPipeIds.WORLD_UI, new StageRenderLayer(context.getWorldStage()));
      pipeline.put(RenderPipeIds.UI, new StageRenderLayer(context.getStage()));
      return pipeline;
   }

}
