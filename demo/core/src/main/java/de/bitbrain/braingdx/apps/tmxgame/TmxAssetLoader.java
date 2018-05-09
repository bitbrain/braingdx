package de.bitbrain.braingdx.apps.tmxgame;

import java.util.Map;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class TmxAssetLoader implements GameAssetLoader {

   @Override
   public void put(Map<String, Class<?>> assets) {
      assets.put(Assets.RPG.CHARACTER_TILESET, Texture.class);
      assets.put(Assets.RPG.MAP_2, TiledMap.class);
      assets.put(Assets.Sounds.SOUND_TEST, Sound.class);
   }

}
