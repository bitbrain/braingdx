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

import de.bitbrain.braingdx.util.DeltaTimer;

public class RandomOrientationMovementController implements MovementController<Orientation> {

    private float interval;
    private Orientation orientation;
    private Random random = new Random();
    private DeltaTimer timer = new DeltaTimer();

    @Override
    public void update(Movement<Orientation> movement, float delta) {
	timer.update(delta);
	if (timer.reached(interval)) {
	    timer.reset();
	    changeDirection();
	    changeInterval();
	    movement.move(orientation);
	}
    }

    private void changeDirection() {
	int size = Orientation.values().length;
	orientation = Orientation.values()[(int) Math.floor(random.nextFloat() * (size - 1))];
    }

    private void changeInterval() {
	interval = 1.2f + random.nextFloat() * 1.5f;
    }

}
