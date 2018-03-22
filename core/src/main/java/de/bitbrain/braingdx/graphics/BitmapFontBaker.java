package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import de.bitbrain.braingdx.assets.SharedAssetManager;

/**
 * Bakes {@link BitmapFont} out of ttf fonts
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 0.2.0
 */
public class BitmapFontBaker {

   public static BitmapFont bake(String fontPath, int size) {
       FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
       FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
       param.color = Color.WHITE;
       param.size = size;
       return generator.generateFont(param);
   }
}