package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import de.bitbrain.braingdx.graphics.GameCamera;

public class GameCameraTween implements TweenAccessor<GameCamera> {

   public static final int DEFAULT_ZOOM_FACTOR = 1;
   public static final int TARGET_TRACKING_SPEED = 2;

   @Override
   public int getValues(GameCamera target, int tweenType, float[] returnValues) {
      switch (tweenType) {
         case DEFAULT_ZOOM_FACTOR:
            returnValues[0] = target.getDefaultZoomFactor();
            return 1;
         case TARGET_TRACKING_SPEED:
            returnValues[0] = target.getTargetTrackingSpeed();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(GameCamera target, int tweenType, float[] newValues) {
      switch (tweenType) {
         case DEFAULT_ZOOM_FACTOR:
            target.setDefaultZoomFactor(newValues[0]);
            break;
         case TARGET_TRACKING_SPEED:
            target.setTargetTrackingSpeed(newValues[0]);
            break;
      }
   }
}
