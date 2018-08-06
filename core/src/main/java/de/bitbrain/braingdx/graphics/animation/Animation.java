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

package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * A generic animation which can be rendered onto a {@link Screen} or applied {@link FrameBuffer}.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public interface Animation {

   /**
    * Renders this animation at the given position with specified dimensions.
    *
    * @param batch  the batch to use in order to render this animation
    * @param x      the horizontal position of this animation on the screen
    * @param y      the vertical position of this animation on the screen
    * @param width  the width in pixel of this animation
    * @param height the height in pixel of this animation
    * @param delta  the current frame delta
    */
   void render(Batch batch, float x, float y, float width, float height, float delta, Color color);

   /**
    * Clones this animation.
    */
   Animation clone();
}
