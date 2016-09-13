package de.bitbrain.braingdx.core;

import java.util.Map;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class BrainGdxApp extends BrainGdxGame {

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
