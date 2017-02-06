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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapProperties;
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
    private final GameObjectUpdater gameObjectUpdater;
    private final State state;
    private final StatePopulator populator;
    private final Map<TiledMapType, MapLayerRendererFactory> factories;

    public TiledMapManagerImpl(BehaviorManager behaviorManager, 
	                       GameWorld gameWorld,
	                       GameObjectRenderManager renderManager) {
	this.behaviorManager = behaviorManager;
	this.gameWorld = gameWorld;
	this.renderManager = renderManager;
	this.state = new State();
	this.api = new TiledMapAPIImpl(state, gameWorld);
	this.populator = new StatePopulator(renderManager, gameWorld, api, listeners);
	this.gameObjectUpdater = new GameObjectUpdater(api, state);
	this.factories = createFactories();
    }

    @Override
    public void addListener(TiledMapListener listener) {
	listeners.add(listener);
    }

    @Override
    public void load(TiledMap tiledMap, Camera camera, TiledMapType type, TiledMapConfig config)  throws TiledMapException {
	validate(tiledMap);
	clear();
	behaviorManager.apply(gameObjectUpdater);
	populator.populate(tiledMap, state, camera, factories.get(type));
    }

    @Override
    public void load(TiledMap tiledMap, Camera camera, TiledMapType type)  throws TiledMapException {
	this.load(tiledMap, camera, type, new TiledMapConfig());
    }

    @Override
    public void dispose() {
	clear();
    }

    @Override
    public TiledMapAPI getAPI() {
	return api;
    }

    private void clear() {
	behaviorManager.remove(gameObjectUpdater);
	gameWorld.clear();
	for (String id : state.getLayerIds()) {
	    renderManager.unregister(id);
	}
	state.clear();
    }

    protected Map<TiledMapType, MapLayerRendererFactory> createFactories() {
	Map<TiledMapType, MapLayerRendererFactory> factories = new HashMap<TiledMapType, MapLayerRendererFactory>();
	factories.put(TiledMapType.ORTHOGONAL, new OrthogonalMapLayerRendererFactory());
	return factories;
    }

    private void validate(TiledMap map) throws TiledMapException {
	MapProperties properties = map.getProperties();
	if (properties.get(Constants.WIDTH) == null) {
	    throw new TiledMapException("Map has no width specified");
	}
	if (properties.get(Constants.HEIGHT) == null) {
	    throw new TiledMapException("Map has no width specified");
	}
	if (properties.get(Constants.WIDTH, int.class) <= 0f) {
	    throw new TiledMapException("Map width must be larger than 0");
	}
	if (properties.get(Constants.HEIGHT, int.class) <= 0f) {
	    throw new TiledMapException("Map height must be larger than 0");
	}
    }

}
