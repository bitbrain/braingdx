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

package de.bitbrain.braingdx.behavior.movement;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

import java.util.Random;

/**
 * Behavior which moves the object in random directions
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class RandomVelocityMovementBehavior extends BehaviorAdapter {

   private float interval = 1f;

   private final DeltaTimer timer;

   private Vector2 direction = new Vector2(1f, 0f);

   private Vector2 velocity = new Vector2(100f, 100f);

   private final Random random;

   public RandomVelocityMovementBehavior() {
      this(new Random());
      changeInterval();
   }

   public RandomVelocityMovementBehavior(Random random) {
      this.random = random;
      timer = new DeltaTimer(interval);
      changeInterval();
   }

   @Override
   public void update(GameObject source, float delta) {
      timer.update(delta);
      if (timer.reached(interval) && source.isActive()) {
         timer.reset();
         changeDirection();
         changeInterval();
      }
      source.move(direction.x * velocity.x * delta, direction.y * velocity.y * delta);
   }

   private void changeDirection() {
      direction.setAngle(random.nextFloat() * 360f);
   }

   private void changeInterval() {
      interval = 0.2f + random.nextFloat() * 0.5f;
   }

}
