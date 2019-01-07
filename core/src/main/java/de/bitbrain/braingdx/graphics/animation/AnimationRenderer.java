package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.util.Enabler;
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

   private final AnimationConfig config;
   private final AnimationCache animationCache;
   private final Sprite sprite;
   private final AnimationTypeResolver<GameObject> animationTypeResolver;
   private final Enabler<GameObject> animationEnabler;

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
      Object currentAnimationType = animationTypeResolver.getAnimationType(object);
      AnimationState state = (AnimationState) object.getOrSetAttribute(AnimationState.class, new AnimationState());
      state.stateTime += delta;
      Animation<TextureRegion> animation = animationCache.getAnimation(currentAnimationType);
      boolean animationEnabled = animationEnabler.isEnabledFor(object);
      AnimationFrames frames = config.getFrames(currentAnimationType);
      return animation.getKeyFrame(animationEnabled ? state.stateTime : frames.getDuration() * frames.getResetIndex());
   }

   private void drawRegion(Batch batch, TextureRegion region, GameObject object) {
      sprite.setRegion(region);
      sprite.setOrigin(object.getOriginX(), object.getOriginY());
      sprite.setColor(object.getColor());
      sprite.setBounds(
            object.getLeft() + object.getOffsetX(),
            object.getTop() + object.getOffsetY(),
            object.getWidth(),
            object.getHeight()
      );
      sprite.setScale(object.getScaleX(), object.getScaleY());
      sprite.draw(batch);
   }
}
