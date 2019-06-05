package de.bitbrain.braingdx.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;

public abstract class GameObject2DRenderer implements GameObjectRenderManager.GameObjectRenderer<Batch> {

   @Override
   public Class<Batch> getBatchClass() {
      return Batch.class;
   }
}
