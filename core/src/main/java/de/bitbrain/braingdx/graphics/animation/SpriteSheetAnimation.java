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
import de.bitbrain.braingdx.util.DeltaTimer;

/**
 * Animates {@link SpriteSheet} objects.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
class SpriteSheetAnimation implements Animation {

    public static final float DEFAULT_INTERVAL = 0.2f;

    private final SpriteSheet sheet;

    private final int offsetX;
    private final int offsetY;
    private final int totalFrames;

    private int currentFrame;
    private float interval;

    private AnimationType strategy;

    private final DeltaTimer timer = new DeltaTimer();

    SpriteSheetAnimation(SpriteSheet sheet, int offsetX, int offsetY, int frames, float interval,
	    AnimationType strategy) {
	this.sheet = sheet;
	this.offsetX = offsetX;
	this.offsetY = offsetY;
	this.totalFrames = frames;
	this.interval = interval;
	this.strategy = strategy;
    }

    @Override
    public void render(Batch batch, float x, float y, float width, float height, float delta) {
	timer.update(delta);
	if (timer.reached(interval)) {
	    currentFrame = strategy.updateCurrentFrame(currentFrame, totalFrames);
	    timer.reset();
	}
	sheet.draw(batch, offsetX + currentFrame, offsetY, x, y, width, height);
    }
}
