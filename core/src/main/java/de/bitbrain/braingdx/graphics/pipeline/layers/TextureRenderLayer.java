package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;

public class TextureRenderLayer implements RenderLayer {

   private final Batch batch;
   private final Texture texture;

   public TextureRenderLayer(Texture texture, Batch batch) {
      this.batch = batch;
      this.texture = texture;
   }

   @Override
   public void render(float delta) {
      batch.begin();
      batch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      batch.end();
   }

}
