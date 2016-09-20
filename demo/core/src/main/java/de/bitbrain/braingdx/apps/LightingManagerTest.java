package de.bitbrain.braingdx.apps;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class LightingManagerTest extends BrainGdxGame {

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
		setBackgroundColor(Color.WHITE);
		lightingManager.setAmbientLight(new Color(0f, 0f, 0f, 0.1f));
	    }
	};
    }

}
