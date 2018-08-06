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

import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation.Direction;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;

import java.util.Map;

/**
 * Creates {@link SpriteSheetAnimation} objects.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class SpriteSheetAnimationFactory {

   private static final float DEFAULT_INTERVAL = 0.15f;
   private static final int DEFAULT_BASE = 1;
   private static final int DEFAULT_FRAMES = 3;
   private static final Direction DEFAULT_DIRECTION = Direction.HORIZONTAL;
   private static final AnimationType DEFAULT_TYPE = AnimationTypes.RESET;
   private static final Index DEFAULT_INDEX = new Index(0, 0);
   private final Map<Integer, Index> indices;
   private final SpriteSheet sheet;

   public SpriteSheetAnimationFactory(SpriteSheet sheet, Map<Integer, Index> indices) {
      this.sheet = sheet;
      this.indices = indices;
   }

   public SpriteSheetAnimation create(int type) {
      Index index = indices.get(type);
      if (sheet == null) {
         index = DEFAULT_INDEX;
      }
      return new SpriteSheetAnimation(sheet).origin(index.x, index.y).interval(DEFAULT_INTERVAL)
            .direction(DEFAULT_DIRECTION).type(DEFAULT_TYPE).base(DEFAULT_BASE).frames(DEFAULT_FRAMES);
   }

   public static class Index {
      public final int x;
      public final int y;

      public Index(int x, int y) {
         this.x = x;
         this.y = y;
      }
   }
}
