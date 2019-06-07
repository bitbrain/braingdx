package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g2d.Batch;

public abstract class RenderLayer2D implements RenderLayer<Batch> {

   @Override
   public Class<Batch> getBatchCass() {
      return Batch.class;
   }
}
