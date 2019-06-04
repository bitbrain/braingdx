package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;
import static java.lang.String.format;

/**
 * Provides cached {@code TextureRegion} objects from a {@code Texture} provided to this sprite sheet.
 * <p></p>
 * Automatically calculates the regions required for a given range on the sprite sheet and returns them.
 * Additionally, this class caches regions so they can be re-used again.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.4
 */
public class AnimationSpriteSheet {

   private final Map<String, TextureRegion[]> textureRegionCache = new HashMap<String, TextureRegion[]>();
   private final TextureRegion[][] textureRegions;

   public AnimationSpriteSheet(Texture texture, int tileSize) {
      textureRegions = TextureRegion.split(
            texture,
            tileSize,
            tileSize
      );
   }

   public AnimationSpriteSheet(Texture texture, int tileWidth, int tileHeight) {
      textureRegions = TextureRegion.split(
            texture,
            tileWidth,
            tileHeight
      );
   }

   public TextureRegion[] getFrames(int startX, int startY, int endX, int endY) {
      String cacheKey = getCacheKey(startX, startY, endX, endY);
      TextureRegion[] cachedData = textureRegionCache.get(cacheKey);
      if (cachedData == null) {
         cachedData = computeCacheData(startX, startY, endX, endY);
         textureRegionCache.put(cacheKey, cachedData);
      }
      return cachedData;
   }

   private String getCacheKey(int startX, int startY, int endX, int endY) {
      return format("%s_%s_%s_%s", startX, startY, endX, endY);
   }

   private TextureRegion[] computeCacheData(int startX, int startY, int endX, int endY) {
      int columns = abs((endX + 1) - startX);
      int rows = abs((endY + 1) - startY);
      TextureRegion[] regions = new TextureRegion[columns * rows];
      int index = 0;
      for (int y = startY; y <= endY; y++) {
         for (int x = startX; x <= endX; x++) {
            regions[index++] = textureRegions[y][x];
         }
      }
      return regions;
   }
}
