package de.bitbrain.braingdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SharedAssetManager;

/**
 * Base implementation for BrainGDX games
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public abstract class BrainGdxGame extends Game {

    @Override
    public final void create() {
        loadAssets();
        setScreen(getInitialScreen());
    }

    protected abstract GameAssetLoader getAssetLoader();
    protected abstract AbstractScreen getInitialScreen();

    private void loadAssets() {
        AssetManager assetManager = SharedAssetManager.getInstance();
        GameAssetLoader loader = getAssetLoader();
        if (loader == null) {
            throw new RuntimeException("No asset loader has been specified.");
        }
        HashMap<String, Class<?> > mapping = new HashMap<String, Class<?> >();
        loader.put(mapping);
        for (Map.Entry<String, Class<?> > entry : mapping.entrySet()) {
            assetManager.load(entry.getKey(), entry.getValue());
        }
        assetManager.finishLoading();
    }
}
