package de.bitbrain.braingdx.apps;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.graphics.LightingManager;

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

	    private LightingManager lightingManager;

	    @Override
	    protected void onCreateStage(Stage stage, int width, int height) {
		super.onCreateStage(stage, width, height);
		setBackgroundColor(Color.WHITE);
		World world = new World(Vector2.Zero, false);
		lightingManager = new LightingManager(world);
		lightingManager.setAmbientLight(new Color(0f, 0f, 0f, 0.1f));
		lightingManager.addPointLight("p1", 0, 0, 200, Color.RED);
	    }

	    @Override
	    protected void afterWorldRender(Batch batch, float delta) {
		super.afterWorldRender(batch, delta);
		batch.end();
		lightingManager.render(camera);
		batch.begin();
	    }
	};
    }

}
