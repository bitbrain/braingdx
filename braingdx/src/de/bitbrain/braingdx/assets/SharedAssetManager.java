package de.bitbrain.braingdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import de.bitbrain.braingdx.assets.loader.ParticleLoader;

/**
 *Implementation of a shared asset manager
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class SharedAssetManager {

    private static AssetManager instance = null;

    private SharedAssetManager() {
    }

    public static AssetManager getInstance() {

        if (instance == null) {
            loadInternal();
        }

        return instance;
    }

    public static <T> T get(String s, Class<T> clss) {
        return getInstance().get(s, clss);
    }

    public static void dispose() {
        if (instance != null) {
            getInstance().dispose();
            instance = null;
        }
    }

    private static void loadInternal() {

        if (Gdx.files == null) {
            throw new RuntimeException("LibGDX is not initialized yet!");
        }

        if (Gdx.files.isLocalStorageAvailable()) {
            instance = new AssetManager();
            instance.setLoader(ParticleEffect.class, new ParticleLoader(new InternalFileHandleResolver()));
        }
    }

    public static void reload() {
        instance.dispose();
        loadInternal();
    }
}
