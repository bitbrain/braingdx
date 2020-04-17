package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.graphics.renderer.GameObject2DRenderer;
import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.util.Factory;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renders an animation for a {@link GameObject} onto the screen for the position where
 * the game object is currently positioned in world coordinates.
 * <p></p>
 * This implementation does not directly require an {@link Animation} object. Instead,
 * on initialisation it expects a {@link AnimationConfig} object which can be constructed via
 * the {@link AnimationConfig.AnimationConfigBuilder} implementation.
 * Animations are dynamically created based on the information provided by the configuration.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.4.0
 */
public class AnimationRenderer extends GameObject2DRenderer {

   private static final Enabler<GameObject> DEFAULT_ENABLER = new Enabler<GameObject>() {
      @Override
      public boolean isEnabledFor(GameObject target) {
         return true;
      }
   };

   private static final AnimationTypeResolver<GameObject> DEFAULT_ANIMATION_TYPE_RESOLVER = new AnimationTypeResolver<GameObject>() {
      @Override
      public Object getAnimationType(GameObject object) {
         return object.getType();
      }
   };

   private class AnimationState {
      public float stateTime;
   }

   private final Factory<AnimationState> animationStateFactory = new Factory<AnimationState>() {

      @Override
      public AnimationState create() {
         return new AnimationState();
      }
   };

   private final AnimationConfig config;
   private final AnimationCache animationCache;
   private final Sprite sprite;
   private final AnimationTypeResolver<GameObject> animationTypeResolver;
   private final Enabler<GameObject> animationEnabler;
   private final Vector2 offset = new Vector2();
   private final Vector2 customSize = new Vector2();
   private boolean enabled = true;

   public AnimationRenderer(AnimationSpriteSheet spriteSheet, AnimationConfig config, Enabler<GameObject> animationEnabler) {
      this(spriteSheet, config, DEFAULT_ANIMATION_TYPE_RESOLVER, animationEnabler);
   }

   public AnimationRenderer(AnimationSpriteSheet spriteSheet, AnimationConfig config) {
      this(spriteSheet, config, DEFAULT_ANIMATION_TYPE_RESOLVER, DEFAULT_ENABLER);
   }

   public AnimationRenderer(AnimationSpriteSheet spriteSheet, AnimationConfig config, AnimationTypeResolver<GameObject> animationTypeResolver) {
      this(spriteSheet, config, animationTypeResolver, DEFAULT_ENABLER);
   }

   public AnimationRenderer(AnimationSpriteSheet spriteSheet, AnimationConfig config, AnimationTypeResolver<GameObject> animationTypeResolver, Enabler<GameObject> animationEnabler) {
      this.config = config;
      this.animationCache = new AnimationCache(spriteSheet, config);
      this.sprite = new Sprite();
      this.animationTypeResolver = animationTypeResolver;
      this.animationEnabler = animationEnabler;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      TextureRegion region = retrieveRegionFor(object, delta);
      if (enabled) {
         batch.setColor(object.getColor());
         drawRegion(
               batch,
               region,
               object
         );
         batch.setColor(Color.WHITE);
      }
   }

   public AnimationRenderer offset(float x, float y) {
      this.offset.set(x, y);
      return this;
   }

   public AnimationRenderer size(float width, float height) {
      this.customSize.set(width, height);
      return this;
   }

   public AnimationRenderer enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   private TextureRegion retrieveRegionFor(GameObject object, float delta) {
      Object currentAnimationType = animationTypeResolver.getAnimationType(object);
      AnimationState state = object.getOrSetAttribute(AnimationState.class, animationStateFactory);
      Animation<TextureRegion> animation = animationCache.getAnimation(currentAnimationType);
      AnimationFrames frames = config.getFrames(currentAnimationType);
      if (state.stateTime == 0f) {
         if (frames.isRandomOffset()) {
            state.stateTime = (float) (Math.random() * (frames.getDuration() * frames.getFrames()));
         } else {
            state.stateTime = frames.getOffset();
         }
      }
      state.stateTime += delta;
      boolean animationEnabled = animationEnabler.isEnabledFor(object);
      if (!animationEnabled) {
         if (frames.isRandomOffset()) {
            state.stateTime = (float) (Math.random() * (frames.getDuration() * frames.getFrames()));
         } else {
            state.stateTime = frames.getOffset();
         }
      }
      return animation.getKeyFrame(animationEnabled ? state.stateTime : frames.getDuration() * frames.getResetIndex());
   }

   private void drawRegion(Batch batch, TextureRegion region, GameObject object) {
      sprite.setRegion(region);
      sprite.setOrigin(object.getOriginX(), object.getOriginY());
      sprite.setColor(object.getColor());
      sprite.setBounds(
            object.getLeft() + object.getOffsetX() + offset.x,
            object.getTop() + object.getOffsetY() + offset.y,
            customSize.x != 0 ? customSize.x : object.getWidth(),
            customSize.y != 0 ? customSize.y : object.getHeight()
      );
      sprite.setScale(object.getScaleX(), object.getScaleY());
      sprite.draw(batch);
   }
}
