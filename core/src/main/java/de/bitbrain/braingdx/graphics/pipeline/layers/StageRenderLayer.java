package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;

public class StageRenderLayer extends AbstractRenderLayer {

    private final Stage stage;

    public StageRenderLayer(Stage stage) {
	this.stage = stage;
    }

    @Override
    public void render(Batch batch, float delta) {
	stage.draw();
    }

}
