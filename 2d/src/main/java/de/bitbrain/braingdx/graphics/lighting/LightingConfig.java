package de.bitbrain.braingdx.graphics.lighting;

public class LightingConfig {

   private static final int DEFAULT_RAYS = 30;

   boolean shadows = true;
   boolean diffuseLighting = true;
   boolean blur = true;
   boolean culling = true;
   boolean gammaCorrection = true;
   int rays = DEFAULT_RAYS;

   public LightingConfig shadows(boolean enabled) {
      this.shadows = enabled;
      return this;
   }

   public LightingConfig diffuseLighting(boolean enabled) {
      this.diffuseLighting = enabled;
      return this;
   }

   public LightingConfig blur(boolean enabled) {
      this.blur = enabled;
      return this;
   }

   public LightingConfig culling(boolean enabled) {
      this.culling = enabled;
      return this;
   }

   public LightingConfig gammaCorrection(boolean enabled) {
      this.gammaCorrection = enabled;
      return this;
   }

   public LightingConfig rays(int rays) {
      this.rays = rays;
      return this;
   }

}