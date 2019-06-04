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

package de.bitbrain.braingdx.movement;

import de.bitbrain.braingdx.world.GameObject;

/**
 * Resolves collisions on tiled maps.
 */
public interface TiledCollisionResolver {

   /**
    * Determines the collision on tiled index and layer.
    *
    * @param tileX the horizontal tile index
    * @param tileY the vertical tile index
    * @param layer the tile layer
    */
   boolean isCollision(int tileX, int tileY, int layer);

   /**
    * Determines the collision on tiled index and layer, excluding the source object.
    *
    * @param tileX  the horizontal tile index
    * @param tileY  the vertical tile index
    * @param layer  the tile layer
    * @param source the source to ignore the collision on
    */
   boolean isExclusiveCollision(int tileX, int tileY, int layer, GameObject source);

   /**
    * Determines the collision on tiled index and layer, only for the source object.
    *
    * @param tileX  the horizontal tile index
    * @param tileY  the vertical tile index
    * @param layer  the tile layer
    * @param source the source to check collision for
    */
   boolean isInclusiveCollision(int tileX, int tileY, int layer, GameObject source);

   /**
    * Determines the collision on absolute x and y coordinate
    *
    * @param x     the world x position
    * @param y     the world y position
    * @param layer the tiled layer
    */
   boolean isCollision(float x, float y, int layer);

   /**
    * Determines if there is an existing collision on the field where the object is,
    * ignoring the self-collision caused by the object given.
    *
    * @param object the object to check
    */
   boolean isExclusiveCollision(GameObject object);

   /**
    * Determines if there is an existing collision on the field where the object is,
    * only for the object provided.
    *
    * @param object the object to check
    */
   boolean isInclusiveCollision(GameObject object);

   /**
    * Determines if there is an existing collision on the world position,
    * ignoring the collision caused by an object given.
    *
    * @param x      the world x position
    * @param y      the world y position
    * @param layer  the tiled layer
    * @param object the object to ignore the collision on
    */
   boolean isExclusiveCollision(float x, float y, int layer, GameObject object);

   /**
    * Determines if there is an existing collision on the world position,
    * only for the object provided
    *
    * @param x      the world x position
    * @param y      the world y position
    * @param layer  the tiled layer
    * @param object the object to check collision for
    */
   boolean isInclusiveCollision(float x, float y, int layer, GameObject object);

   /**
    * Determines the collision of the object with additional horizontal and vertical offset.
    *
    * @param object      the object to check for
    * @param tileOffsetX the horizontal offset
    * @param tileOffsetY the vertical offset
    */
   boolean isCollision(GameObject object, int tileOffsetX, int tileOffsetY);
}
