package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;

public class TextureRenderLayer extends RenderLayer2D {

   private final Batch batch;
   private final Texture texture;

   public TextureRenderLayer(Texture texture) {
      this.batch = new SpriteBatch();
      this.texture = texture;
   }

   @Override
   public void render(Batch batch, float delta) {
      this.batch.begin();
      this.batch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      this.batch.end();
   }

}
