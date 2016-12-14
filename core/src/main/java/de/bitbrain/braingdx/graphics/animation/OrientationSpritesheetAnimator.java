/* Copyright 2016 Miguel Gonzalez Sanchez
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

package de.bitbrain.braingdx.graphics.animation;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Orientation based implementation of {@link Animator}
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 1.0.0
 * @version 1.0.0
 */
public class OrientationSpritesheetAnimator extends BehaviorAdapter {

    private final SpriteSheetAnimation animation;
    private final Movement<Orientation> movement;
    private final AnimationType movingType;
    private final AnimationType stillType;

    public OrientationSpritesheetAnimator(Movement<Orientation> movement, SpriteSheetAnimation animation,
	    AnimationType movingType) {
	this(movement, animation, movingType, AnimationTypes.RESET);
    }

    public OrientationSpritesheetAnimator(Movement<Orientation> movement, SpriteSheetAnimation animation,
	    AnimationType movingType,
	    AnimationType stillType) {
	this.movement = movement;
	this.animation = animation;
	this.movingType = movingType;
	this.stillType = stillType;
    }

    @Override
    public void update(GameObject source, float delta) {
	if (!movement.isMoving()) {
	    animation.type(stillType);
	} else if (source.hasAttribute(Orientation.class)) {
	    Orientation orientation = (Orientation) source.getAttribute(Orientation.class);
	    switch (orientation) {
		case DOWN:  case LEFT:
		case RIGHT: case UP:
		    animation.type(movingType);
	    }
	}
    }

}
