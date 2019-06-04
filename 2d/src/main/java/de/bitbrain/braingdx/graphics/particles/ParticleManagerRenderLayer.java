package de.bitbrain.braingdx.graphics.particles;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;

/**
 * Renders particles on a render layer.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.2.6
 */
public class ParticleManagerRenderLayer implements RenderLayer {

   private final ParticleManager manager;

   public ParticleManagerRenderLayer(ParticleManager manager) {
      this.manager = manager;
   }

   @Override
   public void beforeRender() {
      // noOp
   }

   @Override
   public void render(Batch batch, float delta) {
      batch.begin();
      manager.draw(batch, delta);
      batch.end();
   }

}
