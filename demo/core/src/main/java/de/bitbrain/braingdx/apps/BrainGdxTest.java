package de.bitbrain.braingdx.apps;

import java.util.Map;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.screens.BrainGdxWelcomeScreen;

public class BrainGdxTest extends BrainGdxGame {

    @Override
    protected GameAssetLoader getAssetLoader() {
	return new GameAssetLoader() {

	    @Override
	    public void put(Map<String, Class<?>> assets) {

	    }

	};
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
	return new BrainGdxWelcomeScreen(this);
    }
}
