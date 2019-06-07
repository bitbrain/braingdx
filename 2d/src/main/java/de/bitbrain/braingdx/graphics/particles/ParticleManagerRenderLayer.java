package de.bitbrain.braingdx.graphics.particles;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;

/**
 * Renders particles on a render layer.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.2.6
 */
public class ParticleManagerRenderLayer extends RenderLayer2D {

   private final ParticleManager manager;

   public ParticleManagerRenderLayer(ParticleManager manager) {
      this.manager = manager;
   }

   @Override
   public void render(Batch batch, float delta) {
      manager.draw(batch, delta);
   }

}