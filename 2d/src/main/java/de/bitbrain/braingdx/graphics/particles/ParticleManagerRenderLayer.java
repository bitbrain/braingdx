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
   private final Batch batch;

   public ParticleManagerRenderLayer(ParticleManager manager, Batch batch) {
      this.manager = manager;
      this.batch = batch;
   }

   @Override
   public void render(float delta) {
      batch.begin();
      manager.draw(batch, delta);
      batch.end();
   }

}
