package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.util.ArgumentFactory;

public class CombinedRenderPipeline2DFactory implements ArgumentFactory<GameContext, RenderPipeline> {

   private final Batch batch;

   public CombinedRenderPipeline2DFactory(Batch batch) {
      this.batch = batch;
   }
   @Override
   public RenderPipeline create(GameContext context) {
      return new CombinedRenderPipeline(context.getShaderManager().getConfig(), batch);
   }

}
