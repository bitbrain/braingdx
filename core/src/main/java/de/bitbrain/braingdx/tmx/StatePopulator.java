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
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.util.IDGenerator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Extracts {@link GameObject} instances from a {@link TiledMap} provided.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class StatePopulator {

    private static final boolean DEFAULT_COLLISION = false;

    private final GameObjectRenderManager renderManager;
    private final GameWorld gameWorld;
    private final TiledMapAPI api;
    private final List<TiledMapListener> listeners;

    public StatePopulator(GameObjectRenderManager renderManager, GameWorld gameWorld, TiledMapAPI api,
	    List<TiledMapListener> listeners) {
	this.renderManager = renderManager;
	this.gameWorld = gameWorld;
	this.api = api;
	this.listeners = listeners;
    }

    public void populate(TiledMap tiledMap, State state, Camera camera, MapLayerRendererFactory rendererFactory) {
	MapLayers mapLayers = tiledMap.getLayers();
	handleMapProperties(tiledMap.getProperties(), state);
	List<String> layerIds = new ArrayList<String>();
	for (int i = 0; i < mapLayers.getCount(); ++i) {
	    MapLayer mapLayer = mapLayers.get(i);
	    if (mapLayer instanceof TiledMapTileLayer) {
		String layerId = handleTiledMapTileLayer((TiledMapTileLayer)mapLayer, i, tiledMap, camera, rendererFactory);
		layerIds.add(layerId);
		populateStaticMapData(i, (TiledMapTileLayer) mapLayer, state);
	    } else {
		// Not a tiledlayer so consider it as an object layer
		handleObjectLayer(i, mapLayer, state);
	    }
	}
	state.setLayerIds(layerIds);
    }

    private void handleMapProperties(MapProperties properties, State state) {
	state.setIndexDimensions(properties.get(Constants.WIDTH, Integer.class), 
		                 properties.get(Constants.HEIGHT, Integer.class));
    }

    private void handleObjectLayer(int layerIndex, MapLayer layer, State state) {
	MapObjects mapObjects = layer.getObjects();
	for (int objectIndex = 0; objectIndex < mapObjects.getCount(); ++objectIndex) {
	    MapObject mapObject = mapObjects.get(objectIndex);
	    MapProperties objectProperties = mapObject.getProperties();
	    GameObject gameObject = gameWorld.addObject();
	    float x = objectProperties.get(Constants.X, Float.class);
	    float y = objectProperties.get(Constants.Y, Float.class);
	    Object objectType = objectProperties.get(Constants.TYPE);
	    gameObject.setPosition(x, y);
	    gameObject.setColor(mapObject.getColor());
	    gameObject.setType(objectType);
	    gameObject.setAttribute(Constants.LAYER_INDEX, layerIndex);
	    CollisionCalculator.updateCollision(true, gameObject, layerIndex, state);
	    for (TiledMapListener listener : listeners) {
		listener.onLoad(gameObject, api);
	    }
	}
    }

    private String handleTiledMapTileLayer(TiledMapTileLayer layer, int index, TiledMap tiledMap, Camera camera,
	    MapLayerRendererFactory rendererFactory) {
	final int numberOfRows = tiledMap.getProperties().get(Constants.HEIGHT, Integer.class);
	GameObjectRenderer renderer = rendererFactory.create(index, tiledMap, camera);
	String id = IDGenerator.generateNext(OrthogonalMapLayerRenderer.class);
	renderManager.register(id, renderer);
	GameObject layerObject = gameWorld.addObject();
	layerObject.setActive(false);
	layerObject.setType(id);
	int layerZIndex = (index + 1) * numberOfRows;
	layerObject.setZIndex(layerZIndex);
	return id;
    }

    private void populateStaticMapData(int layerIndex, TiledMapTileLayer layer, State state) {
	Integer[][] heightMap = state.getHeightMap();
	if (heightMap == null) {
	    heightMap = new Integer[state.getMapIndexWidth()][state.getMapIndexHeight()];
	    state.setHeightMap(heightMap);
	}
	for (int x = 0; x < heightMap.length; ++x) {
	    for (int y = 0; y < heightMap[x].length; ++y) {
		populateHeightMap(x, y, state, layerIndex, layer);
		populateCollisions(x, y, state, layerIndex, layer);
	    }
	}
    }

    private void populateHeightMap(int x, int y, State state, int layerIndex, TiledMapTileLayer layer) {
	Cell cell = layer.getCell(x, y);
	if (cell != null) {
	    Integer[][] heightMap = state.getHeightMap();
	    heightMap[x][y] = IndexCalculator.calculateZIndex(state.getMapIndexHeight(), y, layerIndex);
	}
    }

    private void populateCollisions(int x, int y, State state, int layerIndex, TiledMapTileLayer layer) {
	Cell cell = layer.getCell(x, y);
	Boolean[][] collisions = CollisionCalculator.getLayerCollisions(layerIndex, state);
	if (cell != null) {
	    TiledMapTile tile = cell.getTile();
	    if (tile != null) {
		MapProperties properties = tile.getProperties();
		if (properties.containsKey(Constants.COLLISION)) {
		    collisions[x][y] = properties.get(Constants.COLLISION, Boolean.class);
		} else {
		    collisions[x][y] = DEFAULT_COLLISION;
		}
	    } else {
		collisions[x][y] = true;
	    }
	} else {
	    collisions[x][y] = true;
	}
    }
}
