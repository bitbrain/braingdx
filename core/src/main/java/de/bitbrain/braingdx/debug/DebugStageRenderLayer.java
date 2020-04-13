package de.bitbrain.braingdx.debug;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.StageRenderLayer;

public class DebugStageRenderLayer extends StageRenderLayer {

   private final GameContext context;

   public DebugStageRenderLayer(GameContext context) {
      super(context.getDebugStage());
      this.context = context;
   }

   @Override
   public void render(Batch batch, float delta) {
      if (context.isDebugEnabled()) {
         super.render(batch, delta);
      }
   }
}
