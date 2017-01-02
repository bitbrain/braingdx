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

package de.bitbrain.braingdx.graphics.animation.types;

import de.bitbrain.braingdx.graphics.animation.AnimationType;

/**
 * Moves the frames from start to end and vise versa.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
class ForwardYoyoAnimationType implements AnimationType {

    @Override
    public int updateCurrentFrame(int lastFrame, int currentFrame, int totalFrames, int origin) {
	if (isBackwards(lastFrame, currentFrame)) {
	    currentFrame--;
	    if (currentFrame < 0) {
		// Call recursively
		return updateCurrentFrame(-1, 0, totalFrames, origin);
	    }
	} else {
	    currentFrame++;
	    if (currentFrame > totalFrames - 1) {
		// Call recursively
		return updateCurrentFrame(totalFrames, totalFrames - 1, totalFrames, origin);
	    }
	}
	return currentFrame;
    }

    private boolean isBackwards(int lastFrame, int currentFrame) {
	return currentFrame < lastFrame;
    }
}
