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
 * This component calculates the index for instances of type {@link GameObject}.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class IndexCalculator {

    public static int calculateXIndex(GameObject object, State state) {
	return calculateXIndex(object.getLeft(), state.getMapIndexWidth());
    }

    public static int calculateYIndex(GameObject object, State state) {
	return calculateYIndex(object.getTop(), state.getMapIndexHeight());
    }

    public static int calculateXIndex(float x, int numberOfColumns) {
	return (int) Math.round(Math.floor(x / (float) numberOfColumns));
    }

    public static int calculateYIndex(float y, int numberOfRows) {
	return (int) Math.round(Math.floor(y / (float) numberOfRows));
    }

    public static int calculateZIndex(GameObject object, TiledMapAPI api, int currentLayerIndex) {
	int rows = api.getNumberOfRows();
	int yIndex = calculateYIndex(object.getTop(), rows);
	return calculateZIndex(rows, yIndex, currentLayerIndex);
    }

    public static int calculateZIndex(GameObject object, State state, int currentLayerIndex) {
	int rows = state.getMapIndexHeight();
	int yIndex = calculateYIndex(object, state);
	return calculateZIndex(rows, yIndex, currentLayerIndex);
    }

    public static int calculateZIndex(int rows, int yIndex, int currentLayerIndex) {
	return (currentLayerIndex + 1) * rows + yIndex;
    }
}
