package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g3d.ModelBatch;

public abstract class RenderLayer3D implements RenderLayer<ModelBatch> {

   @Override
   public Class<ModelBatch> getBatchCass() {
      return ModelBatch.class;
   }
}
