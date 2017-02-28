/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.graphics.particles;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Disposable;

import de.bitbrain.braingdx.assets.SharedAssetManager;

/**
 * Manages particle effects.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class ParticleManager implements Disposable {

   public static final int DEFAULT_INITIAL_CAPACITY = 25;
   public static final int DEFAULT_MAXIMUM_EFFECTS = 500;

   private final Map<String, ParticleEffectPool> pools = new HashMap<String, ParticleEffectPool>();

   private final int initialCapacity;

   private final int maximalEffects;

   public ParticleManager() {
      this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAXIMUM_EFFECTS);
   }

   public ParticleManager(int initialCapacity, int maximalEffects) {
      this.initialCapacity = initialCapacity;
      this.maximalEffects = maximalEffects;
   }

   public ManagedParticleEffect create(String particleFile) {
      ParticleEffectPool pool = pools.get(particleFile);
      if (!pools.containsKey(particleFile)) {
         ParticleEffect effect = SharedAssetManager.getInstance().get(particleFile, ParticleEffect.class);
         pool = new ParticleEffectPool(effect, initialCapacity, maximalEffects);
         pools.put(particleFile, pool);
      }
      PooledEffect effect = pool.obtain();
      return new ManagedParticleEffect(effect, particleFile);
   }

   public boolean free(ManagedParticleEffect effect, boolean force) {
      if (force || effect.getEffect().isComplete()) {
         effect.getEffect().free();
         ParticleEffectPool pool = pools.get(effect.getPath());
         pool.free(effect.getEffect());
         return true;
      }
      return false;
   }

   public boolean free(ManagedParticleEffect effect) {
      return free(effect, true);
   }

   @Override
   public void dispose() {
      for (ParticleEffectPool pool : pools.values()) {
         pool.clear();
      }
      pools.clear();
   }

}
