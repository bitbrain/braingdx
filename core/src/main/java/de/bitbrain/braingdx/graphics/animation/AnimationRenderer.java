package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renders an animation for a {@link GameObject} onto the screen for the position where
 * the game object is currently positioned in world coordinates.
 * <p></p>
 * This implementation does not directly require an {@link Animation} object. Instead,
 * on initialisation it expects a {@link AnimationConfig} object which can be constructed via
 * the {@link de.bitbrain.braingdx.graphics.animation.AnimationConfig.AnimationConfigBuilder} implementation.
 * Animations are dynamically created based on the information provided by the configuration.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.4.0
 */
public class AnimationRenderer implements GameObjectRenderManager.GameObjectRenderer {

   private class AnimationState {
      public float stateTime;
   }

   private final AnimationConfig config;
   private final AnimationCache animationCache;

   public AnimationRenderer(AnimationSpriteSheet spriteSheet, AnimationConfig config) {
      this.config = config;
      this.animationCache = new AnimationCache(spriteSheet, config);
   }

   @Override
   public void init() {

   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      batch.setColor(object.getColor());
      drawRegion(
            batch,
            retrieveRegionFor(object, delta),
            object
      );
      batch.setColor(Color.WHITE);
   }

   private TextureRegion retrieveRegionFor(GameObject object, float delta) {
      Object currentAnimationType = config
            .getAnimationTypeResolver()
            .getAnimationType(object);
      AnimationState state = (AnimationState) object.getOrSetAttribute(AnimationState.class, new AnimationState());
      state.stateTime += delta;
      Animation<TextureRegion> animation = animationCache.getAnimation(currentAnimationType);
      boolean animationEnabled = config.getAnimationEnabler().isEnabledFor(object);
      AnimationFrames frames = config.getFrames(currentAnimationType);
      return animation.getKeyFrame(animationEnabled ? state.stateTime : frames.getDuration() * frames.getResetIndex());
   }

   private void drawRegion(Batch batch, TextureRegion region, GameObject object) {
      if (object.getWidth() != 0 && object.getHeight() != 0) {
         float scalingOffsetX = object.getScaleX() < 0 ? object.getWidth() * object.getScaleX() : 0;
         float scalingOffsetY = object.getScaleY() < 0 ? object.getHeight() * object.getScaleY() : 0;
         batch.draw(region,
               object.getLeft() + object.getOffsetX() - scalingOffsetX,
               object.getTop() + object.getOffsetY() - scalingOffsetY,
               object.getWidth() * object.getScaleX(),
               object.getHeight() * object.getScaleY()
         );
      } else {
         batch.draw(region, object.getLeft(), object.getTop());
      }
   }
}
