package de.bitbrain.braingdx.apps;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.RPGAssetLoader;
import de.bitbrain.braingdx.screens.RPGScreen;

public class RPGTest extends BrainGdxGame {

    @Override
    protected GameAssetLoader getAssetLoader() {
	return new RPGAssetLoader();
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
	return new RPGScreen(this);
    }

}
