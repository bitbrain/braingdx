/* Copyright 2016 Miguel Gonzalez Sanchez
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

package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.GameObject;

/**
 * Handles rendering of game objects
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class RenderManager {
    private static Map<Integer, Renderer> rendererMap = new HashMap<Integer, Renderer>();

    public void render(GameObject object, Batch batch, float delta) {
	Renderer renderer = rendererMap.get(object.getType());
	if (renderer != null) {
	    renderer.render(object, batch, delta);
	}
    }

    public void register(Integer gameObjectType, Renderer renderer) {
	if (!rendererMap.containsKey(gameObjectType)) {
	    renderer.init();
	    rendererMap.put(gameObjectType, renderer);
	}
    }

    public static interface Renderer {

	void init();

	void render(GameObject object, Batch batch, float delta);
    }
}
