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

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

/**
 * This component calculates the zIndex for active instances of type {@link GameObject}.
 * 
 * The zIndex is calculated depending on the given layer the {@link GameObject} is currently on.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class ZIndexUpdater extends BehaviorAdapter {

    private final TiledMapAPI api;

    public ZIndexUpdater(TiledMapAPI api) {
	this.api = api;
    }

    @Override
    public void update(GameObject object, float delta) {
	if (object.isActive()) {
	    object.setZIndex(calculateZIndex(object));
	}
    }

    private int calculateZIndex(GameObject object) {
	int currentLayerIndex = api.layerIndexOf(object);
	int rows = api.getNumberOfRows();
	int yIndex = (int) Math.round(Math.floor(object.getTop() / (float) rows));
	return (currentLayerIndex + 1) * rows + yIndex;
    }
}
