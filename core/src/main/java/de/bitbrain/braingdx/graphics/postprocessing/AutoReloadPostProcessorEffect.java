package de.bitbrain.braingdx.graphics.postprocessing;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.graphics.event.GraphicsSettingsChangeEvent;
import de.bitbrain.braingdx.util.Mutator;

/**
 * Implementation of a {@link PostProcessorEffect} which automatically re-initialises itself after
 * graphics settings change.
 */
public class AutoReloadPostProcessorEffect<T extends PostProcessorEffect> extends PostProcessorEffect implements GameEventListener<GraphicsSettingsChangeEvent> {

   private final EffectFactory<T> factory;
   private final GraphicsSettings settings;
   private T effect;
   private boolean disposed;

   public AutoReloadPostProcessorEffect(EffectFactory<T> factory,  GraphicsSettings settings) {
      this.settings = settings;
      this.factory = factory;
      effect = factory.create(null, settings.getScaledRenderWidth(), settings.getScaledRenderHeight(), settings);
   }

   public void mutate(Mutator<T> mutator) {
      if (mutator != null) {
         mutator.mutate(effect);
      }
   }

   @Override
   public void onEvent(GraphicsSettingsChangeEvent event) {
      effect.dispose();
      effect = factory.create(effect, settings.getScaledRenderWidth(), settings.getScaledRenderHeight(), settings);
   }

   @Override
   public void rebind() {
      if (disposed) {
         return;
      }
      effect.rebind();
   }

   @Override
   public void render(FrameBuffer src, FrameBuffer dest) {
      if (disposed) {
         return;
      }
      effect.render(src, dest);
   }

   @Override
   public void dispose() {
      if (disposed) {
         return;
      }
      effect.dispose();
      disposed = true;
   }
}
