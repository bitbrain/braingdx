package de.bitbrain.braingdx.util;

public final class BitUtils {

   public static boolean haveSameSign(float a, float b) {
      return a >= 0 && b >= 0 || a < 0 && b < 0;
   }

   public static boolean haveSameSign(double a, double b) {
      return a >= 0 && b >= 0 || a < 0 && b < 0;
   }
}
