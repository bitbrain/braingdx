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

package de.bitbrain.braingdx.behavior.movement;

import de.bitbrain.braingdx.world.GameObject;

/**
 * Resolves collisions in a Tiled basis.
 */
public interface TiledCollisionResolver {

   boolean isCollision(int tileX, int tileY, int layer);

   boolean isCollision(int tileX, int tileY, int layer, GameObject source);

   boolean isCollision(float x, float y, int layer);

   boolean isCollision(GameObject object, int tileOffsetX, int tileOffsetY);
}
