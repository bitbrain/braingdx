package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;

public class GameObjectRenderLayer extends RenderLayer2D {

   private final GameObjectRenderManager renderManager;

   public GameObjectRenderLayer(GameObjectRenderManager renderManager) {
      this.renderManager = renderManager;
   }

   @Override
   public void render(Batch batch, float delta) {
      renderManager.beforeRender();
      renderManager.render(delta);
   }

}
