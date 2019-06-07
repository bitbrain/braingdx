package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.util.Resizeable;

public class ColoredRenderLayer extends RenderLayer2D implements Disposable, Resizeable {

   private Color color = Color.WHITE;

   private Texture texture;

   private SpriteBatch batch;

   private OrthographicCamera camera;

   public ColoredRenderLayer() {
      batch = new SpriteBatch();
      camera = new OrthographicCamera();
      camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      camera.update();
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
      batch.end();
      this.batch.setColor(color);
      this.batch.setProjectionMatrix(camera.combined);
      this.batch.begin();
      this.batch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      this.batch.end();
      batch.begin();
   }

   @Override
   public void dispose() {
      texture.dispose();
   }

   @Override
   public void resize(int width, int height) {
      camera.setToOrtho(false, width, height);
   }
}
