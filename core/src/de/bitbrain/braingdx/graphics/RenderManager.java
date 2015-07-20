package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.GameObject;

/**
 * Manages rendering of game objects
 *
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
