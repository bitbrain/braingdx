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

package de.bitbrain.braingdx.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.world.GameObject;

/**
 * Handles rendering of game objects
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class GameObjectRenderManager {

    private static Map<Integer, GameObjectRenderer> rendererMap = new HashMap<Integer, GameObjectRenderer>();

    private final Batch batch;

    public GameObjectRenderManager(Batch batch) {
	this.batch = batch;
    }

    public void render(GameObject object, float delta) {
	final GameObjectRenderer renderer = rendererMap.get(object.getType());
	if (renderer != null) {
	    renderer.render(object, batch, delta);
	}
    }

    public void register(Integer gameObjectType, GameObjectRenderer renderer) {
	if (!rendererMap.containsKey(gameObjectType)) {
	    renderer.init();
	    rendererMap.put(gameObjectType, renderer);
	}
    }

    /**
     * Combines multiple renderers for a particular game object
     * 
     * @param gameObjectRenderers renderers
     * @return a new combined {@link GameObjectRenderer}
     */
    public static GameObjectRenderer combine(GameObjectRenderer... gameObjectRenderers) {
	return new CombinedGameObjectRenderer(gameObjectRenderers);
    }

    public static interface GameObjectRenderer {

	void init();

	void render(GameObject object, Batch batch, float delta);
    }

    static class CombinedGameObjectRenderer implements GameObjectRenderer {

	private final GameObjectRenderer[] renderers;

	public CombinedGameObjectRenderer(GameObjectRenderer... gameObjectRenderers) {
	    this.renderers = gameObjectRenderers;
	}

	@Override
	public void init() {
	    for (GameObjectRenderer renderer : renderers) {
		renderer.init();
	    }
	}

	@Override
	public void render(GameObject object, Batch batch, float delta) {
	    for (GameObjectRenderer renderer : renderers) {
		renderer.render(object, batch, delta);
	    }
	}

    }
}
