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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Internal state for tiled map operations.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class State {

    private List<String> layerIds = Collections.emptyList();

    private Integer[][] heightMap;

    private Map<Integer, Boolean[][]> collisions = Collections.emptyMap();

    private int mapIndexHeight;

    private int mapIndexWidth;

    private int cellSize = 1;

    public List<String> getLayerIds() {
	return layerIds;
    }

    public Integer[][] getHeightMap() {
	return heightMap;
    }

    public Map<Integer, Boolean[][]> getCollisions() {
	return collisions;
    }

    public int getMapIndexWidth() {
	return mapIndexWidth;
    }

    public int getMapIndexHeight() {
	return mapIndexHeight;
    }

    public int getCellSize() {
	return cellSize;
    }

    public void setLayerIds(List<String> layerIds) {
	layerIds = Collections.unmodifiableList(layerIds);
    }

    public void setHeightMap(Integer[][] heightMap) {
	this.heightMap = heightMap;
    }

    public void setCollsions(Map<Integer, Boolean[][]> collisions) {
	this.collisions = collisions;
    }

    public void setIndexDimensions(int indexX, int indexY) {
	this.mapIndexWidth = indexX;
	this.mapIndexHeight = indexY;
    }

    public void setCellSize(int cellSize) {
	this.cellSize = cellSize;
    }

    public void clear() {
	heightMap = null;
	layerIds = Collections.emptyList();
	collisions = Collections.emptyMap();
    }
}
