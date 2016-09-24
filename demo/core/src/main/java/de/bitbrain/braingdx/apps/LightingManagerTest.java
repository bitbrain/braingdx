package de.bitbrain.braingdx.apps;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.bitfire.postprocessing.effects.Bloom;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.behavior.RandomMovementBehavior;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;

public class LightingManagerTest extends BrainGdxGame {

    private static final int OBJECTS = 205;

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
		Texture texture = GraphicsFactory.createTexture(30, 30, Color.DARK_GRAY);
		world.registerRenderer(TYPE, new SpriteRenderer(texture));

		// Shading
		RenderPipe pipe = renderPipeline.getPipe(PIPE_WORLD);
		Bloom bloom = new Bloom(Math.round(Gdx.graphics.getWidth()), Math.round(Gdx.graphics.getHeight()));
		bloom.setBlurAmount(20f);
		bloom.setBloomIntesity(3.1f);
		bloom.setBlurPasses(8);
		pipe.addEffects(bloom);
		pipe = renderPipeline.getPipe(PIPE_WORLD);

		for (int i = 0; i < OBJECTS; ++i) {
		    GameObject object = world.addObject();
		    object.setDimensions(30, 30);
		    object.setPosition((int) (Gdx.graphics.getWidth() * Math.random()),
			    (int) (Gdx.graphics.getHeight() * Math.random()));
		    object.setType(TYPE);
		    Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(),
			    0.27f);
		    world.applyBehavior(new PointLightBehavior(randomColor, 300f, lightingManager), object);
		    world.applyBehavior(new RandomMovementBehavior(), object);
		}
	    }
	};
    }

}
