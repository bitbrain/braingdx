package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.world.GameWorld;

public class WorldRenderLayer implements RenderLayer {

   private final GameWorld world;
   private final Batch batch;

   public WorldRenderLayer(GameWorld world, Batch batch) {
      this.world = world;
      this.batch = batch;
   }

   @Override
   public void render(float delta) {
      batch.begin();
      world.update(delta);
      batch.end();
   }

}
