package de.bitbrain.braingdx.graphics.pipeline.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;

public class TextureRenderLayer extends AbstractRenderLayer {
    
    private Texture texture;
    
    private SpriteBatch internalBatch = new SpriteBatch();

    public TextureRenderLayer(Texture texture) {
	this.texture = texture;
    }

    @Override
    public void render(Batch batch, float delta) {
	internalBatch.begin();
	internalBatch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	internalBatch.end();
    }

}
