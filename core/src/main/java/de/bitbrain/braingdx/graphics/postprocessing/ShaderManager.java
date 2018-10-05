package de.bitbrain.braingdx.graphics.postprocessing;

import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.graphics.event.GraphicsSettingsChangeEvent;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Fxaa;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.graphics.postprocessing.effects.Zoomer;

/**
 * Provides factory methods to create a variety of shaders.
 */
public class ShaderManager {

   private final GameEventManager gameEventManager;
   private final GraphicsSettings settings;

   public ShaderManager(GameEventManager gameEventManager, GraphicsSettings settings) {
      this.gameEventManager = gameEventManager;
      this.settings = settings;
   }


   /**
    * Creates an anti-aliasing effect to reduce aliasing.
    * It is recommended placing this effect always on top of other effects or layers.
    */
   public AutoReloadPostProcessorEffect<Fxaa> createFxaaEffect() {
      return createEffect(new EffectFactory<Fxaa>() {
         @Override
         public Fxaa create(Fxaa original, int newWidth, int newHeight, GraphicsSettings settings) {
            return new Fxaa(newWidth, newHeight);
         }
      });
   }

   /**
    * Creates a bloom effect which blurs light parts and darkens dark parts.
    */
   public AutoReloadPostProcessorEffect<Bloom> createBloomEffect() {
      return createEffect(new EffectFactory<Bloom>() {
         @Override
         public Bloom create(Bloom original, int newWidth, int newHeight, GraphicsSettings settings) {
            Bloom bloom = new Bloom(newWidth, newHeight);
            if (original != null) {
               bloom.setBaseIntesity(original.getBaseIntensity());
               bloom.setBaseSaturation(original.getBaseSaturation());
               bloom.setBloomIntesity(original.getBloomIntensity());
               bloom.setBloomSaturation(original.getBloomSaturation());
               bloom.setBlurAmount(original.getBlurAmount());
               bloom.setBlurPasses(original.getBlurPasses());
               bloom.setSettings(original.getSettings());
               bloom.setBlurType(original.getBlurType());
               bloom.setThreshold(original.getThreshold());
            }
            return bloom;
         }
      });
   }

   /**
    * Creates an effect which darkens the corners of the screen.
    */
   public AutoReloadPostProcessorEffect<Vignette> createVignetteEffect() {
      return createEffect(new EffectFactory<Vignette>() {
         @Override
         public Vignette create(Vignette original, int newWidth, int newHeight, GraphicsSettings settings) {
            Vignette vignette = new Vignette(newWidth, newHeight, false);
            if (original != null) {
               vignette.setCenter(original.getCenterX(), original.getCenterY());
               vignette.setCoords(original.getCoordsX(), original.getCoordsY());
               vignette.setIntensity(original.getIntensity());
               vignette.setLutTexture(original.getLut());
               vignette.setLutIntensity(original.getLutIntensity());
               vignette.setSaturation(original.getSaturation());
               vignette.setSaturationMul(original.getSaturationMul());
               vignette.setEnabled(original.isEnabled());
            }
            return vignette;
         }
      });
   }

   /**
    * Creates an effect which zooms in our out of the current scene with a blurred effect.
    */
   public AutoReloadPostProcessorEffect<Zoomer> createZoomerEffect() {
      return createEffect(new EffectFactory<Zoomer>() {
         @Override
         public Zoomer create(Zoomer original, int newWidth, int newHeight, GraphicsSettings settings) {
            Zoomer zoomer = new Zoomer(newWidth, newHeight);
            if (original != null) {
               zoomer.setBlurStrength(original.getBlurStrength());
               zoomer.setZoom(original.getZoom());
               zoomer.setOrigin(original.getOriginX(), original.getOriginY());
            }
            return zoomer;
         }
      });
   }

   private <T extends PostProcessorEffect> AutoReloadPostProcessorEffect<T> createEffect(EffectFactory<T> factory) {
      AutoReloadPostProcessorEffect<T> effect = new AutoReloadPostProcessorEffect<T>(factory, settings);
      gameEventManager.register(effect, GraphicsSettingsChangeEvent.class);
      return effect;
   }
}
