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

package de.bitbrain.braingdx.graphics.renderer;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renderer implementation for animations
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class SpriteSheetAnimationRenderer extends AnimationRenderer {

    private final SpriteSheetAnimation animation;

    private final Map<Orientation, Integer> orientations = new HashMap<Orientation, Integer>();

    public SpriteSheetAnimationRenderer(SpriteSheetAnimation animation) {
	super(animation);
	this.animation = animation;
    }

    public SpriteSheetAnimationRenderer map(Orientation direction, int offset) {
	this.orientations.put(direction, offset);
	return this;
    }

    @Override
    public void render(GameObject object, Batch batch, float delta) {
	if (object.hasAttribute(Orientation.class)) {
	    Orientation direction = (Orientation) object.getAttribute(Orientation.class);
	    if (orientations.containsKey(direction)) {
		int offset = orientations.get(direction);
		animation.offset(offset, offset);
	    }
	}
	super.render(object, batch, delta);
    }
}
