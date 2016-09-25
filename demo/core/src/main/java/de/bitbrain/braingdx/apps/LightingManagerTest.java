package de.bitbrain.braingdx.apps;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.AppGameAssetLoader;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.screens.LightingManagerScreen;

public class LightingManagerTest extends BrainGdxGame {

    @Override
    protected GameAssetLoader getAssetLoader() {
	return new AppGameAssetLoader();
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
	return new LightingManagerScreen(this);
    }

}
