package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import de.bitbrain.braingdx.assets.Asset;

import java.util.ArrayList;
import java.util.List;

/**
 * Bakes {@link BitmapFont} out of ttf fonts
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.2.0
 */
public class BitmapFontBaker {

   private static List<BitmapFont> bakedFonts = new ArrayList<BitmapFont>();

   public static BitmapFont bake(String fontPath, FreeTypeFontGenerator.FreeTypeFontParameter params) {
      FreeTypeFontGenerator generator = Asset.get(fontPath, FreeTypeFontGenerator.class);
      BitmapFont font = generator.generateFont(params);
      bakedFonts.add(font);
      return font;
   }

   public static BitmapFont bake(String fontPath, int size, boolean mono) {
      FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
      param.color = Color.WHITE;
      param.size = size;
      param.mono = mono;
      return bake(fontPath, param);
   }

   public static BitmapFont bake(String fontPath, int size) {
      return bake(fontPath, size, false);
   }

   public static void dispose() {
      if (!bakedFonts.isEmpty()) {
         Gdx.app.log("INFO", "Disposing " + bakedFonts.size() + " fonts...");
         for (BitmapFont font : bakedFonts) {
            font.dispose();
         }
         Gdx.app.log("INFO", "Done.");
      }
   }
}