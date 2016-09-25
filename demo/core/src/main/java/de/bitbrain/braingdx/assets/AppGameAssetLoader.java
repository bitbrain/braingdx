package de.bitbrain.braingdx.assets;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class AppGameAssetLoader implements GameAssetLoader {

    @Override
    public void put(Map<String, Class<?>> assets) {
	assets.put(Assets.BUTTON_DEFAULT, Texture.class);
	assets.put(Assets.FONT_BYOM_32, BitmapFont.class);
    }

}
