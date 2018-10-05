package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import de.bitbrain.braingdx.graphics.postprocessing.effects.MotionBlur;

public class MotionBlurShaderTween implements TweenAccessor<MotionBlur> {

   public static final int BLUR_OPACITY = 1;

   @Override
   public int getValues(MotionBlur target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case BLUR_OPACITY:
            returnValues[0] = target.getBlurOpacity();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(MotionBlur target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case BLUR_OPACITY:
            target.setBlurOpacity(newValues[0]);
            break;
      }
   }

}
