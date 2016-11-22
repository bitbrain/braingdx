package de.bitbrain.braingdx.apps.lighting;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.GameAssetLoader;

public class AppGameAssetLoader implements GameAssetLoader {

    @Override
    public void put(Map<String, Class<?>> assets) {
	assets.put(Assets.BUTTON_DEFAULT, Texture.class);
	assets.put(Assets.FONT_BYOM_32, BitmapFont.class);
	assets.put(Assets.TEX_BACKGROUND, Texture.class);
	assets.put(Assets.SOLDIER, Texture.class);
	assets.put(Assets.WALL, Texture.class);
    }

}
