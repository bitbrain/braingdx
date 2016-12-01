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

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.graphics.animation.types.AnimationType;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;

/**
 * Builds {@link Animation} objects.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class AnimationBuilder {

    private SpriteSheet spritesheet;

    private Vector2 offset = new Vector2();

    private AnimationType type = AnimationTypes.FORWARD;

    private float interval = 1f;

    private int frames = 1;

    /**
     * Specifies the {@link SpriteSheet} object to use for this animation.
     * 
     * @param spritesheet the spritesheet object
     * @return this
     */
    public AnimationBuilder with(SpriteSheet spritesheet) {
	this.spritesheet = spritesheet;
	return this;
    }

    /**
     * Specifies the offset on the {@link SpriteSheet} where to start the animation.
     * 
     * @param x the horizontal index, starting from 0
     * @param y the vertical index, starting from 0
     * @return this
     */
    public AnimationBuilder offset(int x, int y) {
	offset.set(x, y);
	return this;
    }

    /**
     * The number of maximum frames of the animation.
     * 
     * @param frames number of frames
     * @return this
     */
    public AnimationBuilder frames(int frames) {
	this.frames = Math.abs(frames);
	return this;
    }

    /**
     * The interval in which to iterate over single frames.
     * 
     * @param interval interval in seconds
     * @return this
     */
    public AnimationBuilder interval(float interval) {
	this.interval = Math.abs(interval);
	return this;
    }

    /**
     * The {link AnimationType} of this animation.
     * 
     * @param type animation type object
     * @return this
     */
    public AnimationBuilder type(AnimationType type) {
	this.type = type;
	return this;
    }

    /**
     * Builds an animation.
     * 
     * @return a newly created {@link Animation} object.
     * @throws RuntimeException when no spritesheet is defined.
     */
    public Animation build() {
	if (spritesheet == null) {
	    throw new RuntimeException("No spritesheet defined when building an animation!");
	}
	return new SpriteSheetAnimation(spritesheet, (int) offset.x, (int) offset.y, frames, interval, type);
    }
}
