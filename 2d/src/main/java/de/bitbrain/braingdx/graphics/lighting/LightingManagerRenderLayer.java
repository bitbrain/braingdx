package de.bitbrain.braingdx.graphics.lighting;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.util.Resizeable;

public class LightingManagerRenderLayer extends RenderLayer2D implements Resizeable {

   private final LightingManagerImpl manager;

   public LightingManagerRenderLayer(LightingManagerImpl manager) {
      this.manager = manager;
   }

   @Override
   public void render(Batch batch, float delta) {
      //batch.end();
      manager.render();
     // batch.begin();
   }

   @Override
   public void resize(int width, int height) {
      manager.resize(width, height);
   }

}
