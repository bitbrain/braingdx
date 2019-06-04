package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.HashMap;
import java.util.Map;

public class AnimationConfig {

   private Map<Object, AnimationFrames> framesMap;

   private AnimationConfig(Map<Object, AnimationFrames> framesMap) {
      this.framesMap = framesMap;
   }

   public static AnimationConfigBuilder builder() {
      return new AnimationConfigBuilder();
   }

   AnimationFrames getFrames(Object type) {
      AnimationFrames frames = framesMap.get(type);
      if (frames == null) {
         throw new GdxRuntimeException("No animation frames configured for type=" + type);
      }
      return frames;
   }

   public static class AnimationConfigBuilder {

      private Map<Object, AnimationFrames> framesMap = new HashMap<Object, AnimationFrames>();

      private AnimationConfigBuilder() {}

      public AnimationConfigBuilder registerFrames(Object type, AnimationFrames frames) {
         if (framesMap.containsKey(type)) {
            throw new GdxRuntimeException("ERROR! Animation type=" + type + " already registered!");
         }
         framesMap.put(type, frames);
         return this;
      }

      public AnimationConfig build() {
         return new AnimationConfig(
               framesMap
         );
      }
   }
}
