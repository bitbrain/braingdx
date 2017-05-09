package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;

public class ColorRenderLayer extends AbstractRenderLayer {

   private final ShapeRenderer renderer = new ShapeRenderer();

   private Color color;

   public ColorRenderLayer(Color color) {
      this.color = color;
   }

   @Override
   public void render(Batch batch, float delta) {
      renderer.begin(ShapeType.Filled);
      renderer.setColor(color);
      renderer.rect(0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      renderer.end();
   }
}
