package de.bitbrain.braingdx.graphics.particles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.GraphicsSettings;
import de.bitbrain.braingdx.world.GameObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.ceil;

public class ParticleManagerImpl implements ParticleManager, Disposable {

   private final Set<InternalPooledEffect> effects = new HashSet<InternalPooledEffect>();
   private final Map<String, ParticleEffectPool> pools = new HashMap<String, ParticleEffectPool>();
   private final BehaviorManager behaviorManager;
   private final GraphicsSettings settings;

   public ParticleManagerImpl(BehaviorManager behaviorManager, GraphicsSettings settings) {
      this.behaviorManager = behaviorManager;
      this.settings = settings;
   }

   public void draw(Batch batch, float delta) {
      for (final InternalPooledEffect internal : effects) {
         if (internal.effect.isComplete()) {
            Gdx.app.postRunnable(new Runnable() {
               @Override
               public void run() {
                  freeEffect(internal);
               }
            });
         }
         internal.effect.draw(batch, delta);
      }
   }

   public ParticleEffect attachEffect(final String assetEffectId, GameObject object, final float offsetX, final float offsetY) {
      final InternalPooledEffect resultEffect = ensureEffect(assetEffectId);
      behaviorManager.apply(new BehaviorAdapter() {

         private InternalPooledEffect effect = resultEffect;

         @Override
         public void onDetach(GameObject source) {
            freeEffect(effect);
         }

         @Override
         public void update(GameObject source, float delta) {
            if (effect.effect.isComplete() && effects.contains(effect)) {
               behaviorManager.remove(source, this);
            } else {
               effect.effect.setPosition(source.getLeft() + offsetX, source.getTop() + offsetY);
            }
         }

         @Override
         public void onStatusChange(GameObject source, boolean updateable) {
            if (updateable && !effects.contains(effect)) {
               effect = ensureEffect(assetEffectId);
            } else if (effects.contains(effect)) {
               freeEffect(effect);
            }
         }
      }, object);
      return resultEffect.effect;
   }

   public ParticleEffect spawnEffect(String assetEffectId, float worldX, float worldY) {
      InternalPooledEffect internal = ensureEffect(assetEffectId);
      internal.effect.setPosition(worldX, worldY);
      return internal.effect;
   }

   @Override
   public int getTotalEffectCount() {
      return effects.size();
   }

   @Override
   public void dispose() {
      for (InternalPooledEffect effect : effects) {
         ParticleEffectPool pool = pools.get(effect.assetId);
         effect.effect.free();
         pool.free(effect.effect);
      }
      effects.clear();
      pools.clear();
   }

   private InternalPooledEffect ensureEffect(String particleId) {
      ParticleEffectPool pool = pools.get(particleId);
      if (pool == null) {
         ParticleEffect effect = SharedAssetManager.getInstance().get(particleId, ParticleEffect.class);
         pool = new ParticleEffectPool(effect, 100, 500);
         pools.put(particleId, pool);
      }
      InternalPooledEffect effect = new InternalPooledEffect(particleId, pool.obtain());
      applySettingsToEffect(effect.effect);
      effects.add(effect);
      effect.effect.start();
      return effect;
   }

   private void freeEffect(InternalPooledEffect effect) {
      ParticleEffectPool pool = pools.get(effect.assetId);
      if (pool != null) {
         effect.effect.reset();
         effects.remove(effect);
         pool.free(effect.effect);
      } else {
         Gdx.app.error("Particles", "Unable to release effect " + effect.assetId + ". No pool available!");
      }
   }

   private void applySettingsToEffect(ParticleEffectPool.PooledEffect effect) {
      for (ParticleEmitter emitter : effect.getEmitters()) {
         emitter.setMinParticleCount((int) ceil(emitter.getMinParticleCount() * settings.getParticleMultiplier()));
         emitter.setMaxParticleCount((int) ceil(emitter.getMaxParticleCount() * settings.getParticleMultiplier()));
      }
   }

   private static class InternalPooledEffect {
      public final ParticleEffectPool.PooledEffect effect;
      public final String assetId;

      public InternalPooledEffect(String assetId, ParticleEffectPool.PooledEffect effect) {
         this.effect = effect;
         this.assetId = assetId;
      }
   }
}
