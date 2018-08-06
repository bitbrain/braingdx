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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Simple spritesheet which can be
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class SpriteSheet {

   private final TextureRegion[][] sprites;

   private float flip = 1;

   public SpriteSheet(Texture texture, int spritesHorizontal, int spritesVertical) {
      sprites = TextureRegion.split(texture, texture.getWidth() / spritesHorizontal,
            texture.getHeight() / spritesVertical);
   }

   public SpriteSheet flipped(boolean flipped) {
      flip = flipped ? -1 : 1;
      return this;
   }

   public void draw(Batch batch, int indexX, int indexY, float x, float y, float width, float height) {
      batch.draw(sprites[indexY][indexX], x, flip == -1 ? y + height : y, width, flip * height);
   }
}
