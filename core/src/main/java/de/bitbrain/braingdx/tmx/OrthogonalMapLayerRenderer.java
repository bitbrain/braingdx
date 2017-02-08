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

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renders {@link TiledMapTileLayer} instances.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
class OrthogonalMapLayerRenderer implements GameObjectRenderer {

    private OrthogonalTiledMapRenderer renderer;
    private final TiledMapTileLayer layer;
    private final OrthographicCamera camera;
    private final TiledMap map;

    OrthogonalMapLayerRenderer(int layerIndex, TiledMap map, OrthographicCamera camera) {
	layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
	this.camera = camera;
	this.map = map;
    }

    @Override
    public void init() {
	// noOp
    }

    @Override
    public void render(GameObject object, Batch batch, float delta) {
	if (renderer == null) {
	    renderer = new OrthogonalTiledMapRenderer(map, batch);
	}
	renderer.setView(camera);
	renderer.renderTileLayer(layer);
    }

}
