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

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
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
public class SpriteSheetAnimationSupplier extends BehaviorAdapter implements AnimationSupplier<GameObject> {

    private final Map<GameObject, SpriteSheetAnimation> animations = new HashMap<GameObject, SpriteSheetAnimation>();

    private final SpriteSheetAnimation template;
    private final Map<Orientation, Integer> orientations;
    private final AnimationType movingType;
    private final AnimationType stillType;

    public SpriteSheetAnimationSupplier(Map<Orientation, Integer> orientations,
	    SpriteSheetAnimation template,
	    AnimationType movingType) {
	this(orientations, template, movingType, AnimationTypes.RESET);
    }

    public SpriteSheetAnimationSupplier(Map<Orientation, Integer> orientations,
	    SpriteSheetAnimation template,
	    AnimationType movingType, AnimationType stillType) {
	this.orientations = orientations;
	this.template = template;
	this.movingType = movingType;
	this.stillType = stillType;
    }

    @Override
    public void onDetach(GameObject source) {
	animations.remove(source);
    }

    @Override
    public SpriteSheetAnimation supplyFor(GameObject object) {
	SpriteSheetAnimation animation = animations.get(object);
	if (animation == null) {
	    animation = template.clone();
	    animations.put(object, animation);
	}
	orientate(animation, object);
	return animation;
    }

    private void orientate(SpriteSheetAnimation animation, GameObject object) {
	// TODO Find smarter way to specify movement
	// if (!movement.isMoving()) {
	// animation.type(stillType);
	// } else
	if (object.hasAttribute(Orientation.class)) {
	    Orientation orientation = (Orientation) object.getAttribute(Orientation.class);
	    switch (orientation) {
		case DOWN:  case LEFT:
		case RIGHT: case UP:
		    animation.type(movingType);
	    }
	}
    }


}
