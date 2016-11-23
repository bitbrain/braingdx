package de.bitbrain.braingdx.apps.rpg;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class RPGAssetLoader implements GameAssetLoader {

    @Override
    public void put(Map<String, Class<?>> assets) {
	assets.put(Assets.RPG.CHARACTER_TILESET, Texture.class);
    }

}
