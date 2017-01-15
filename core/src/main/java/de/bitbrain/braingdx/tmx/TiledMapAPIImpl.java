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

import de.bitbrain.braingdx.world.GameObject;

/**
 * Implementation of {@link TiledMapAPI}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class TiledMapAPIImpl implements TiledMapAPI {

    private final TiledMapState state;

    public TiledMapAPIImpl(TiledMapState state) {
	this.state = state;
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
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int layerIndexOf(GameObject object) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int getNumberOfRows() {
	return state.getMapIndexHeight();
    }

    @Override
    public int getNumberOfColumns() {
	return state.getMapIndexWidth();
    }

    private boolean verifyIndex(int indexX, int indexY) {
	return indexX < 0 || indexY < 0 || indexX >= state.getMapIndexWidth() || indexY >= state.getMapIndexHeight();
    }

}
