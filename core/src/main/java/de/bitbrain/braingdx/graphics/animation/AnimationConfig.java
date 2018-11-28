package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.utils.GdxRuntimeException;
import de.bitbrain.braingdx.util.Enabler;
import de.bitbrain.braingdx.world.GameObject;

import java.util.HashMap;
import java.util.Map;

public class AnimationConfig {

   private AnimationTypeResolver<GameObject> animationTypeResolver;
   private Enabler<GameObject> animationEnabler;
   private Map<Object, AnimationFrames> framesMap;

   private AnimationConfig(
         AnimationTypeResolver<GameObject> animationTypeResolver,
         Enabler<GameObject> animationEnabler,
         Map<Object, AnimationFrames> framesMap
   ) {
      this.animationTypeResolver = animationTypeResolver;
      this.animationEnabler = animationEnabler;
      this.framesMap = framesMap;
   }

   public static AnimationConfigBuilder builder() {
      return new AnimationConfigBuilder();
   }

   public AnimationFrames getFrames(Object type) {
      AnimationFrames frames = framesMap.get(type);
      if (frames == null) {
         throw new GdxRuntimeException("No animation frames configured for type=" + type);
      }
      return frames;
   }

   public AnimationTypeResolver<GameObject> getAnimationTypeResolver() {
      return animationTypeResolver;
   }

   public void setAnimationTypeResolver(AnimationTypeResolver<GameObject> animationTypeResolver) {
      this.animationTypeResolver = animationTypeResolver;
   }

   public Enabler<GameObject> getAnimationEnabler() {
      return animationEnabler;
   }

   public void setAnimationEnabler(Enabler<GameObject> animationEnabler) {
      this.animationEnabler = animationEnabler;
   }

   public static class AnimationConfigBuilder {

      private AnimationTypeResolver<GameObject> animationTypeResolver = new
            AnimationTypeResolver<GameObject>() {
               @Override
               public Object getAnimationType(GameObject object) {
                  return object.getType();
               }
            };
      private Map<Object, AnimationFrames> framesMap = new HashMap<Object, AnimationFrames>();

      private Enabler<GameObject> animationEnabler = new Enabler<GameObject>() {
         @Override
         public boolean isEnabledFor(GameObject target) {
            return true;
         }
      };

      private AnimationConfigBuilder() {}

      public AnimationConfigBuilder animationTypeResolver(AnimationTypeResolver<GameObject> animationTypeResolver) {
         this.animationTypeResolver = animationTypeResolver;
         return this;
      }

      public AnimationConfigBuilder registerFrames(Object type, AnimationFrames frames) {
         if (framesMap.containsKey(type)) {
            throw new GdxRuntimeException("ERROR! Animation type=" + type + " already registered!");
         }
         framesMap.put(type, frames);
         return this;
      }

      public AnimationConfigBuilder enabler(Enabler<GameObject> enabler) {
         this.animationEnabler = enabler;
         return this;
      }

      public AnimationConfig build() {
         return new AnimationConfig(
               animationTypeResolver,
               animationEnabler,
               framesMap
         );
      }
   }
}
