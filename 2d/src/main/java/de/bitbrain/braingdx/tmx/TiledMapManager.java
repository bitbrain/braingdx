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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * This manager gives extended support for {@link TiledMap} objects. It features an API to query the
 * map for details and provides an own rendering integration for braingdx.
 *
 * @author Miguel Gonzalez Sanchez
 */
public interface TiledMapManager {

   /**
    * Loads a tiledmap into the active {@link de.bitbrain.braingdx.world.GameWorld}
    *
    * @param tiledMap the tiledmap to loadTiledMapManager
    * @param camera   the camera to use
    * @param config   additional configuration
    * @return a new instance of {@link TiledMapContext}
    * @throws TiledMapException when there is an issue loading the tiledmap
    */
   TiledMapContext load(
         TiledMap tiledMap,
         Camera camera,
         TiledMapConfig config) throws TiledMapException;

   /**
    * Loads a tiledmap into the active {@link de.bitbrain.braingdx.world.GameWorld}
    *
    * @param tiledMap the tiledmap to load
    * @param camera   the camera to use
    * @return a new instance of {@link TiledMapContext}
    * @throws TiledMapException when there is an issue loading the tiledmap
    */
   TiledMapContext load(
         TiledMap tiledMap,
         Camera camera) throws TiledMapException;

   /**
    * Loads a tiledmap into the active {@link de.bitbrain.braingdx.world.GameWorld}
    *
    * @param path   the tiledmap path to load
    * @param camera the camera to use
    * @param config additional configuration
    * @return a new instance of {@link TiledMapContext}
    * @throws TiledMapException when there is an issue loading the tiledmap
    */
   TiledMapContext load(
         String path,
         Camera camera,
         TiledMapConfig config) throws TiledMapException;

   /**
    * Loads a tiledmap into the active {@link de.bitbrain.braingdx.world.GameWorld}
    *
    * @param path   the tiledmap path to load
    * @param camera the camera to use
    * @return a new instance of {@link TiledMapContext}
    * @throws TiledMapException when there is an issue loading the tiledmap
    */
   TiledMapContext load(
         String path,
         Camera camera) throws TiledMapException;

   /**
    * Unloads an existing tiledmap
    *
    * @param context an existing tiledmap context
    */
   void unload(TiledMapContext context);

   /**
    * Unloads an existing tiled map
    *
    * @param path unloads the tiled map at a given path
    */
   void unload(String path);
}
