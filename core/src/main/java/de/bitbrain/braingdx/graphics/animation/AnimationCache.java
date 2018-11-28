package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

/**
 * Internal animation cache to avoid recreation of similar {@code Animation} objects.
 * <p></p>
 * This cache is intelligent and automatically detects if directions have changed and
 * it applies updates to animation attributes dynamically.
 */
class AnimationCache {

   private final Map<Object, Animation<TextureRegion>> animations = new HashMap<Object, Animation<TextureRegion>>();
   private final AnimationSpriteSheet spriteSheet;
   private final AnimationConfig config;
   private final Map<Object, AnimationFrames.Direction> directionMap = new HashMap<Object, AnimationFrames.Direction>();

   AnimationCache(AnimationSpriteSheet spriteSheet, AnimationConfig config) {
      this.spriteSheet = spriteSheet;
      this.config = config;
   }

   Animation<TextureRegion> getAnimation(Object animationType) {
      Animation<TextureRegion> animation = animations.get(animationType);
      if (animation == null || hasDirectionChanged(animationType)) {
         TextureRegion[] regions = getRegionsForType(animationType);
         AnimationFrames frames = config.getFrames(animationType);
         animation = new Animation<TextureRegion>(frames.getDuration(), regions);
         animations.put(animationType, animation);
         directionMap.put(animationType, frames.getDirection());
      }
      updateAnimation(animationType, animation);
      return animation;
   }

   private boolean hasDirectionChanged(Object animationType) {
      return !directionMap.get(animationType).equals(config.getFrames(animationType).getDirection());
   }

   private TextureRegion[] getRegionsForType(Object type) {
      AnimationFrames frames = config.getFrames(type);
      int startX = frames.getOriginX();
      int startY = frames.getOriginY();
      int endX = frames.getDirection() == AnimationFrames.Direction.HORIZONTAL
            ? frames.getOriginX() + frames.getFrames() - 1
            : frames.getOriginX();
      int endY = frames.getDirection() == AnimationFrames.Direction.VERTICAL
            ? frames.getOriginY() + frames.getFrames() - 1
            : frames.getOriginY();
      return spriteSheet.getFrames(startX, startY, endX, endY);
   }

   private void updateAnimation(Object animationType, Animation<TextureRegion> animation) {
      AnimationFrames frames = config.getFrames(animationType);
      animation.setFrameDuration(frames.getDuration());
      animation.setPlayMode(frames.getPlayMode());
   }
}
