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

package de.bitbrain.braingdx.tweens;

import com.badlogic.gdx.audio.Music;

import aurelienribon.tweenengine.TweenAccessor;

public class MusicTween implements TweenAccessor<Music> {

   public static final int VOLUME = 1;

   @Override
   public int getValues(Music object, int type, float[] values) {
      switch (type) {
         case VOLUME:
            values[0] = object.getVolume();
            return 1;
      }
      return 0;
   }

   @Override
   public void setValues(Music object, int type, float[] values) {
      switch (type) {
         case VOLUME:
            object.setVolume(values[0]);
            break;
      }
   }

}
