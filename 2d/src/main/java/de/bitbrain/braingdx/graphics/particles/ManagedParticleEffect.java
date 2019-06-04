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

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;

/**
 * Is managed by {@link ParticleManager}
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class ManagedParticleEffect {

   private final PooledEffect effect;

   private final String path;

   private Vector2 offset = new Vector2();

   private boolean started = false;

   ManagedParticleEffect(PooledEffect effect, String path) {
      this.effect = effect;
      this.path = path;
   }

   public String getPath() {
      return path;
   }

   PooledEffect getEffect() {
      return effect;
   }

   public ManagedParticleEffect offset(float x, float y) {
      this.offset.x = x;
      this.offset.y = y;
      return this;
   }

   public ManagedParticleEffect scale(float scale) {
      effect.scaleEffect(scale);
      return this;
   }

   public ManagedParticleEffect attached(boolean attached) {
      for (ParticleEmitter emitter : effect.getEmitters()) {
         emitter.setAttached(attached);
      }
      return this;
   }

   public void render(float x, float y, Batch batch, float delta) {
      if (!started) {
         effect.start();
         started = true;
      }
      effect.setPosition(x + offset.x, y + offset.y);
      effect.draw(batch, delta);
   }

}
