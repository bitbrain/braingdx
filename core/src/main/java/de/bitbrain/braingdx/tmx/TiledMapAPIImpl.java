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

import com.badlogic.gdx.maps.MapProperties;

import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Implementation of {@link TiledMapAPI}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class TiledMapAPIImpl implements TiledMapAPI {

    private final State state;
    private final GameWorld gameWorld;

    public TiledMapAPIImpl(State state, GameWorld gameWorld) {
	this.state = state;
	this.gameWorld = gameWorld;
    }

    @Override
    public int highestZIndexAt(int tileX, int tileY) {
	Integer[][] heightMap = state.getHeightMap();
	if (verifyIndex(tileX, tileY) && heightMap != null) {
	    return heightMap[tileX][tileY];
	} else {
	    return -1;
	}
    }

    @Override
    public int highestZIndexAt(float x, float y) {
	int tileX = (int) Math.floor(x / (float) state.getCellWidth());
	int tileY = (int) Math.floor(y / (float) state.getCellWidth());
	return highestZIndexAt(tileX, tileY);
    }

    @Override
    public int layerIndexOf(GameObject object) {
	if (object.hasAttribute(Constants.LAYER_INDEX)) {
	    return (Integer) object.getAttribute(Constants.LAYER_INDEX);
	} else {
	    return -1;
	}
    }

    @Override
    public int lastLayerIndexOf(GameObject object) {
	if (object.hasAttribute(Constants.LAST_LAYER_INDEX)) {
	    return (Integer) object.getAttribute(Constants.LAST_LAYER_INDEX);
	} else {
	    return layerIndexOf(object);
	}
    }

    @Override
    public void setLayerIndex(GameObject object, int layerIndex) {
	if (layerIndex > state.getNumberOfLayers() - 1) {
	    throw new TiledMapException("Layer index is too high: " + layerIndex);
	}
	object.setAttribute(Constants.LAYER_INDEX, layerIndex);
    }

    @Override
    public int getNumberOfRows() {
	return state.getMapIndexHeight();
    }

    @Override
    public int getNumberOfColumns() {
	return state.getMapIndexWidth();
    }

    @Override
    public GameObject getGameObjectAt(int tileX, int tileY, int layer) {
	for (GameObject worldObject : gameWorld) {
	    int objectTileX = IndexCalculator.calculateXIndex(worldObject, state);
	    int objectTileY = IndexCalculator.calculateYIndex(worldObject, state);
	    int layerIndex = layerIndexOf(worldObject);
	    if (objectTileX == tileX && objectTileY == tileY && layer == layerIndex) {
		return worldObject;
	    }
	}
	return null;
    }

    @Override
    public float getCellWidth() {
	return state.getCellWidth();
    }

    @Override
    public float getCellHeight() {
	return state.getCellHeight();
    }

    @Override
    public boolean isCollision(int tileX, int tileY, int layer) {
	return state.getState(tileX, tileY, layer).isCollision();
    }

    @Override
    public MapProperties getPropertiesAt(int tileX, int tileY, int layer) {
	return state.getState(tileX, tileY, layer).getProperties();
    }

    private boolean verifyIndex(int indexX, int indexY) {
	return indexX >= 0 && indexY >= 0 && indexX < state.getMapIndexWidth() && indexY < state.getMapIndexHeight();
    }

}
