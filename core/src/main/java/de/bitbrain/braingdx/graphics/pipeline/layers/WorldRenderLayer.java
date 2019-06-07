package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.world.GameWorld;

public class WorldRenderLayer extends RenderLayer2D {

   private final GameWorld world;

   public WorldRenderLayer(GameWorld world) {
      this.world = world;
   }

   @Override
   public void render(Batch batch, float delta) {
      batch.begin();
      world.update(delta);
      batch.end();
   }

}
