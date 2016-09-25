package de.bitbrain.braingdx.apps;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

import de.bitbrain.braingdx.assets.GameAssetLoader;

public class AppGameAssetLoader implements GameAssetLoader {

    @Override
    public void put(Map<String, Class<?>> assets) {
	assets.put(Assets.BUTTON_DEFAULT, Texture.class);
    }

}
