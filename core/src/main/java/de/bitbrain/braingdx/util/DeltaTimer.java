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

package de.bitbrain.braingdx.util;

/**
 * Utility timer to count delta time
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class DeltaTimer {

   private float time;

   public DeltaTimer() {
      // noOp
   }

   public DeltaTimer(float time) {
      this.time = time;
   }

   public float getTicks() {
      return time;
   }

   public void update(float delta) {
      time += delta;
   }

   public void reset() {
      time = 0;
   }

   public boolean reached(float millis) {
      return time >= millis;
   }
}
