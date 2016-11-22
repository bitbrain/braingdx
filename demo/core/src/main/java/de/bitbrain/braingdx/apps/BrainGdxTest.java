package de.bitbrain.braingdx.apps;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.apps.lighting.AppGameAssetLoader;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;

public class BrainGdxTest extends BrainGdxGame {

    @Override
    protected GameAssetLoader getAssetLoader() {
	return new AppGameAssetLoader();
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
	return new BrainGdxWelcomeScreen(this);
    }
}
