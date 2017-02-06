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
import com.badlogic.gdx.utils.Disposable;

/**
 * This manager gives extended support for {@link TiledMap} objects. It features an API to query the
 * map for details and provides an own rendering integration for braingdx.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public interface TiledMapManager extends Disposable {

    void addListener(TiledMapListener listener);

    void load(TiledMap tiledMap, 
	      Camera camera, 
	      TiledMapType type, 
	      TiledMapConfig config) throws TiledMapException;

    void load(TiledMap tiledMap, 
	      Camera camera, 
	      TiledMapType type) throws TiledMapException;

    TiledMapAPI getAPI();
}
