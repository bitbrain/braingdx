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

package de.bitbrain.braingdx.assets;

public interface SampleValidAssets {

   interface Textures {
      String field1 = "texture-field1";
      String field2 = "texture-field2";
   }

   interface Musics {
      String field1 = "musics-field1";
      String field2 = "musics-field2";
   }

   interface Sounds {
      String field1 = "sounds-field1";
      String field2 = "sounds-field2";
      int invalid = 39;
   }

   interface TiledMaps {
      String field1 = "tmx-field1";
      String field2 = "tmx-field2";
   }

   interface Particles {
      String field1 = "particles-field1";
      String field2 = "particles-field2";
   }

   interface Fonts {
      String field1 = "fonts-field1";
      String field2 = "fonts-field2";
   }
}
