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

package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.utils.GdxRuntimeException;

enum TiledMapType {

   ORTHOGONAL("orthogonal"),
   ISOMETRIC("isometric");

   private final String orientation;

   TiledMapType(String orientation) {
      this.orientation = orientation;
   }

   public String getOrientation() {
      return orientation;
   }

   static TiledMapType fromOrientation(String orientation) {
      for (TiledMapType type : values()) {
         if (orientation.equals(type.getOrientation())) {
            return type;
         }
      }
      throw new GdxRuntimeException("TiledMap orientation '" + orientation + "' is currently unsupported.");
   }
}
