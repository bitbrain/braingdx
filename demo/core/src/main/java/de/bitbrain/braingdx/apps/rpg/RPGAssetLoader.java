package de.bitbrain.braingdx.apps.rpg;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class RPGAssetLoader implements GameAssetLoader {

    @Override
    public void put(Map<String, Class<?>> assets) {
	assets.put(Assets.RPG.CHARACTER_TILESET, Texture.class);
	assets.put(Assets.RPG.TORCH, Texture.class);
	assets.put(Assets.RPG.MAP_1, TiledMap.class);
	assets.put(Assets.RPG.FLAME, ParticleEffect.class);
    }

}
