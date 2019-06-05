package de.bitbrain.braingdx.graphics.pipeline;

import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.util.ArgumentFactory;

public class CombinedRenderPipeline2DFactory implements ArgumentFactory<GameContext, RenderPipeline> {
   @Override
   public RenderPipeline create(GameContext context) {
      return new CombinedRenderPipeline(context.getShaderManager().getConfig());
   }

}
