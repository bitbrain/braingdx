package de.bitbrain.braingdx.graphics.lighting;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.util.Resizeable;

public class LightingManagerRenderLayer implements RenderLayer, Resizeable {

    private final LightingManager manager;

    public LightingManagerRenderLayer(LightingManager manager) {
	this.manager = manager;
    }

    @Override
    public void beforeRender() {
	manager.beforeRender();
    }

    @Override
    public void render(Batch batch, float delta) {
	manager.render(batch, delta);
    }

    @Override
    public void resize(int width, int height) {
	manager.resize(width, height);
    }

}
