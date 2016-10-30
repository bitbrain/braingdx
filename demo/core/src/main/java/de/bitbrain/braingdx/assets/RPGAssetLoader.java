package de.bitbrain.braingdx.assets;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

public class RPGAssetLoader implements GameAssetLoader {

    @Override
    public void put(Map<String, Class<?>> assets) {
	assets.put(Assets.SOLDIER, Texture.class);
	assets.put(Assets.WALL, Texture.class);
    }

}
