package de.bitbrain.braingdx.apps;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;

public class LightingManagerTest extends BrainGdxGame {

    private static final int OBJECTS = 5;

    private static final int TYPE = 1;

    @Override
    protected GameAssetLoader getAssetLoader() {
	return new GameAssetLoader() {

	    @Override
	    public void put(Map<String, Class<?>> assets) {
		// noOp
	    }

	};
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
	return new AbstractScreen<LightingManagerTest>(this) {

	    @Override
	    protected void onCreateStage(Stage stage, int width, int height) {
		super.onCreateStage(stage, width, height);
		setBackgroundColor(Color.GRAY);
		lightingManager.setAmbientLight(new Color(0.1f, 0f, 0.2f, 0.1f));
		Texture texture = GraphicsFactory.createTexture(100, 100, Color.DARK_GRAY);
		world.registerRenderer(TYPE, new SpriteRenderer(texture));
		for (int i = 0; i < OBJECTS; ++i) {
		    GameObject object = world.addObject();
		    object.setDimensions(100, 100);
		    object.setPosition((int) (Gdx.graphics.getWidth() * Math.random()),
			    (int) (Gdx.graphics.getHeight() * Math.random()));
		    object.setType(TYPE);
		    Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(),
			    1f);
		    world.applyBehavior(new PointLightBehavior(randomColor, 300f, lightingManager), object);
		}
	    }
	};
    }

}
