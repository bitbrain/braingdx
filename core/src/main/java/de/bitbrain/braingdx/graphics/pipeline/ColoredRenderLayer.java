package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

public class ColoredRenderLayer extends AbstractRenderLayer implements Disposable {

   private Color color = Color.WHITE;

   private Texture texture;

   public ColoredRenderLayer() {
      Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Format.RGB888);
      pixmap.setColor(Color.WHITE);
      pixmap.fill();
      texture = new Texture(pixmap);
      pixmap.dispose();
   }

   public void setColor(Color color) {
      this.color = color;
   }

   @Override
   public void render(Batch batch, float delta) {
      batch.setColor(color);
      batch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
   }

   @Override
   public void dispose() {
      texture.dispose();
   }

}
