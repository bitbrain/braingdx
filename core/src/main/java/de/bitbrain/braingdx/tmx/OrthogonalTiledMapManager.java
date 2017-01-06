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

import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Manages {@link TiledMap} rendering within braingdx. Make sure to call {@link #init()} on this
 * manager.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class OrthogonalTiledMapManager extends BehaviorAdapter {

    private final TiledMap map;
    private final GameCamera camera;
    private final GameObjectRenderManager renderManager;

    public OrthogonalTiledMapManager(TiledMap map, GameObjectRenderManager renderManager, GameCamera gameCamera) {
	this.map = map;
	this.camera = gameCamera;
	this.renderManager = renderManager;
    }

    @Override
    public void onAttach(GameObject source) {
	// TODO
    }

    @Override
    public void onDetach(GameObject source) {
	// TODO
    }

    @Override
    public void update(GameObject source, float delta) {
	// TODO
    }


}
