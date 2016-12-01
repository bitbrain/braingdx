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

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.graphics.animation.types.AnimationType;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.util.DeltaTimer;

/**
 * Animates {@link SpriteSheet} objects.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class SpriteSheetAnimation implements Animation {

    public enum Direction {
	HORIZONTAL, VERTICAL
    }

    public static final float DEFAULT_INTERVAL = 0.2f;

    private SpriteSheet sheet;

    private int offsetX;
    private int offsetY;
    private int totalFrames = 1;
    private int currentFrame;
    private float interval;
    private Direction direction = Direction.HORIZONTAL;

    private AnimationType type = AnimationTypes.FORWARD;

    private DeltaTimer timer = new DeltaTimer();

    public SpriteSheetAnimation() {
	this(null);
    }

    public SpriteSheetAnimation(SpriteSheet sheet) {
	this.sheet = sheet;
    }

    public SpriteSheetAnimation offset(int offsetX, int offsetY) {
	this.offsetX = Math.abs(offsetX);
	this.offsetY = Math.abs(offsetY);
	return this;
    }

    public SpriteSheetAnimation interval(float interval) {
	this.interval = Math.abs(interval);
	return this;
    }

    public SpriteSheetAnimation frames(int frames) {
	this.totalFrames = Math.max(frames, 1);
	return this;
    }

    public SpriteSheetAnimation source(SpriteSheet source) {
	if (source != null) {
	    this.sheet = source;
	}
	return this;
    }

    public SpriteSheetAnimation type(AnimationType type) {
	if (type != null) {
	    this.type = type;
	}
	return this;
    }

    public SpriteSheetAnimation direction(Direction direction) {
	if (direction != null) {
	    this.direction = direction;
	}
	return this;
    }

    @Override
    public void render(Batch batch, float x, float y, float width, float height, float delta) {
	timer.update(delta);
	if (timer.reached(interval)) {
	    currentFrame = type.updateCurrentFrame(currentFrame, totalFrames);
	    timer.reset();
	}
	if (sheet != null) {
	    sheet.draw(batch, getDirectionalOffsetX(), getDirectionalOffsetY(), x, y, width, height);
	}
    }

    private int getDirectionalOffsetX() {
	if (Direction.HORIZONTAL.equals(direction)) {
	    return offsetX + currentFrame;
	}
	return offsetX;
    }

    private int getDirectionalOffsetY() {
	if (Direction.VERTICAL.equals(direction)) {
	    return offsetY + currentFrame;
	}
	return offsetY;
    }
}
