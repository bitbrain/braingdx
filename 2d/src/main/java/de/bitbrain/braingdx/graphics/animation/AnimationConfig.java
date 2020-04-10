package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class AnimationConfig {

   private final Map<Object, AnimationFrames> framesMap;
   private final AnimationFrames standaloneFrames;

   private AnimationConfig(Map<Object, AnimationFrames> framesMap, AnimationFrames standaloneFrames) {
      this.framesMap = framesMap;
      this.standaloneFrames = standaloneFrames;
   }

   public static AnimationConfigBuilder builder() {
      return new AnimationConfigBuilder();
   }

   AnimationFrames getFrames(Object type) {
      if (!framesMap.containsKey(type)) {
         if (standaloneFrames == null) {
            throw new GdxRuntimeException("No animation frames configured for type=" + type);
         }
         return standaloneFrames;
      }
      AnimationFrames frames = framesMap.get(type);
      if (frames == null) {
         throw new GdxRuntimeException("No animation frames configured for type=" + type);
      }
      return frames;
   }

   public static class AnimationConfigBuilder {

      private Map<Object, AnimationFrames> framesMap = new HashMap<Object, AnimationFrames>();
      private AnimationFrames standaloneFrames;

      private AnimationConfigBuilder() {}

      public AnimationConfigBuilder registerFrames(AnimationFrames frames) {
         return registerFrames(null, frames);
      }

      public AnimationConfigBuilder registerFrames(Object type, AnimationFrames frames) {
         if (type == null) {
            this.standaloneFrames = frames;
         } else {
            if (framesMap.containsKey(type)) {
               throw new GdxRuntimeException("ERROR! Animation type=" + type + " already registered!");
            }
            framesMap.put(type, frames);
         }
         return this;
      }

      public AnimationConfig build() {
         return new AnimationConfig(
               framesMap,
               standaloneFrames);
      }
   }
}
