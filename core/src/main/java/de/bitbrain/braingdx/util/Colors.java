package de.bitbrain.braingdx.util;

import com.badlogic.gdx.graphics.Color;

/**
 * Color utilities
 */
public final class Colors {

   public static Color lighten(Color color, float factor) {
      Color result = color.cpy();
      result.r *= factor;
      result.g *= factor;
      result.b *= factor;
      return result;
   }

   public static Color darken(Color color, float factor) {
      Color result = color.cpy();
      result.r /= factor;
      result.g /= factor;
      result.b /= factor;
      return result;
   }
}
