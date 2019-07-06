package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import box2dLight.PointLight;

public class PointLight2DTween implements TweenAccessor<PointLight> {

   public static final int DISTANCE = 1;
   public static final int COLOR_R = 2;
   public static final int COLOR_G = 3;
   public static final int COLOR_B = 4;
   public static final int COLOR_A = 5;

   @Override
   public int getValues(PointLight target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case DISTANCE:
            returnValues[0] = target.getDistance();
            return 1;
         case COLOR_R:
            returnValues[0] = target.getColor().r;
            return 1;
         case COLOR_G:
            returnValues[0] = target.getColor().g;
            return 1;
         case COLOR_B:
            returnValues[0] = target.getColor().b;
            return 1;
         case COLOR_A:
            returnValues[0] = target.getColor().a;
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(PointLight target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case DISTANCE:
            target.setDistance(newValues[0]);
            break;
         case COLOR_R:
            target.getColor().r = newValues[0];
            break;
         case COLOR_G:
            target.getColor().g = newValues[0];
            break;
         case COLOR_B:
            target.getColor().b = newValues[0];
            break;
         case COLOR_A:
            target.getColor().a = newValues[0];
            break;
      }
   }
}
