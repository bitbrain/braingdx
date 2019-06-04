package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.scenes.scene2d.Stage;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;

public class StageRenderLayer implements RenderLayer {

   private final Stage stage;

   public StageRenderLayer(Stage stage) {
      this.stage = stage;
   }

   @Override
   public void render(float delta) {
      stage.draw();
   }

}
