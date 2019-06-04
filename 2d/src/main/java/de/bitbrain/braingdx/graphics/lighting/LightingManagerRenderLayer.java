package de.bitbrain.braingdx.graphics.lighting;

import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.util.Resizeable;

public class LightingManagerRenderLayer implements RenderLayer, Resizeable {

   private final LightingManager manager;

   public LightingManagerRenderLayer(LightingManager manager) {
      this.manager = manager;
   }

   @Override
   public void render(float delta) {
      manager.render();
   }

   @Override
   public void resize(int width, int height) {
      manager.resize(width, height);
   }

}
