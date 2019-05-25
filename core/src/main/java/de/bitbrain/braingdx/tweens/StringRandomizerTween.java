package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import de.bitbrain.braingdx.util.StringRandomizer;

/**
 * Tween implementation for {@link StringRandomizer}
 */
public class StringRandomizerTween implements TweenAccessor<StringRandomizer> {

   public static final int FACTOR = 0;

   @Override
   public int getValues(StringRandomizer target, int tweenType, float[] returnValues) {
      if (tweenType == FACTOR) {
         returnValues[0] = target.getFactor();
         return 1;
      }
      return 0;
   }

   @Override
   public void setValues(StringRandomizer target, int tweenType, float[] newValues) {
      if (tweenType == FACTOR) {
         target.setFactor(newValues[0]);
      }
   }
}
