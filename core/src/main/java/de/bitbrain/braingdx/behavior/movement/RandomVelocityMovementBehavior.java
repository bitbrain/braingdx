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

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Behavior which moves the object in random directions
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class RandomVelocityMovementBehavior extends BehaviorAdapter {

   private float interval = 1f;

   private DeltaTimer timer = new DeltaTimer();

   private Vector2 direction = new Vector2(1f, 0f);

   private Vector2 velocity = new Vector2(100f, 100f);

   private Random random = new Random();

   public RandomVelocityMovementBehavior() {
      changeInterval();
      timer = new DeltaTimer(interval);
   }

   @Override
   public void update(GameObject source, float delta) {
      timer.update(delta);
      if (timer.reached(interval)) {
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
