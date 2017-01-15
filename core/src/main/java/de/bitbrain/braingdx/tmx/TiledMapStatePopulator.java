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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
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
class TiledMapStatePopulator {

    private final GameObjectRenderManager renderManager;

    private final GameWorld gameWorld;

    public TiledMapStatePopulator(GameObjectRenderManager renderManager, GameWorld gameWorld) {
	this.renderManager = renderManager;
	this.gameWorld = gameWorld;
    }

    public void populate(TiledMap tiledMap, TiledMapState state, Camera camera) {
	MapLayers mapLayers = tiledMap.getLayers();
	List<String> layerIds = new ArrayList<String>();
	for (int i = 0; i < mapLayers.getCount(); ++i) {
	    MapLayer mapLayer = mapLayers.get(i);
	    if (mapLayer instanceof TiledMapTileLayer) {
		String layerId = handleTiledMapTileLayer(mapLayer, i, tiledMap, camera);
		layerIds.add(layerId);
	    } else {
		// Not a tiledlayer so consider it as an object layer
		handleObjectLayer(mapLayer);
	    }
	}
	state.setLayerIds(layerIds);
    }

    private void handleObjectLayer(MapLayer layer) {
	MapObjects mapObjects = layer.getObjects();
	for (int objectIndex = 0; objectIndex < mapObjects.getCount(); ++objectIndex) {
	    MapObject object = mapObjects.get(objectIndex);
	    MapProperties objectProperties = object.getProperties();
	    GameObject mapObject = gameWorld.addObject();
	    float x = objectProperties.get(TmxConstants.X, Float.class);
	    float y = objectProperties.get(TmxConstants.Y, Float.class);
	    Object objectType = objectProperties.get(TmxConstants.TYPE);
	    mapObject.setPosition(x, y);
	    mapObject.setColor(object.getColor());
	    mapObject.setType(objectType);
	}
    }

    private String handleTiledMapTileLayer(MapLayer layer, int index, TiledMap tiledMap, Camera camera) {
	final int numberOfRows = tiledMap.getProperties().get(TmxConstants.HEIGHT, Integer.class);
	OrthogonalMapLayerRenderer renderer = new OrthogonalMapLayerRenderer(index, tiledMap,
		(OrthographicCamera) camera);
	String id = IDGenerator.generateNext(OrthogonalMapLayerRenderer.class);
	renderManager.register(id, renderer);
	GameObject layerObject = gameWorld.addObject();
	layerObject.setActive(false);
	layerObject.setType(id);
	int layerZIndex = (index + 1) * numberOfRows;
	layerObject.setZIndex(layerZIndex);
	return id;
    }
}
