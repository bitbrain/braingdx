package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteBatchResolver implements BatchResolver<Batch> {

   private final Batch batch;
   private final Camera camera;

   public SpriteBatchResolver(Camera camera) {
      this.batch = new SpriteBatch();
      this.camera = camera;
   }

   @Override
   public void beforeRender() {
      batch.setProjectionMatrix(camera.combined);
   }

   @Override
   public Class<Batch> getBatchClass() {
      return Batch.class;
   }

   @Override
   public Batch getBatch() {
      return batch;
   }

   @Override
   public void begin() {
      batch.begin();
   }

   @Override
   public void end() {
      batch.end();
   }
}
