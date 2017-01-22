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

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Implementation of {@link TiledMapManager}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class TiledMapManagerImpl implements TiledMapManager {

    private final BehaviorManager behaviorManager;
    private final GameWorld gameWorld;
    private final List<TiledMapListener> listeners = new ArrayList<TiledMapListener>();
    private final GameObjectRenderManager renderManager;
    private final TiledMapAPIImpl api;
    private final ZIndexUpdater zIndexUpdater;
    private final State state;
    private final StatePopulator populator;

    public TiledMapManagerImpl(BehaviorManager behaviorManager, GameWorld gameWorld,
	    GameObjectRenderManager renderManager) {
	this.behaviorManager = behaviorManager;
	this.gameWorld = gameWorld;
	this.renderManager = renderManager;
	this.state = new State();
	this.populator = new StatePopulator(renderManager, gameWorld);
	this.api = new TiledMapAPIImpl(state);
	this.zIndexUpdater = new ZIndexUpdater(api);
    }

    @Override
    public void addListener(TiledMapListener listener) {
	listeners.add(listener);
    }

    @Override
    public void load(TiledMap tiledMap, Camera camera, TiledMapType type, TiledMapConfig config) {
	clear();
	behaviorManager.apply(zIndexUpdater);
	populator.populate(tiledMap, state, camera);
    }

    @Override
    public void load(TiledMap tiledMap, Camera camera, TiledMapType type) {
	this.load(tiledMap, camera, type, new TiledMapConfig());
    }

    @Override
    public void dispose() {
	clear();
    }

    private void clear() {
	state.clear();
	// Refresh zIndex calculation
	behaviorManager.remove(zIndexUpdater);
	gameWorld.clear();
	for (String id : state.getLayerIds()) {
	    renderManager.unregister(id);
	}
    }

}
