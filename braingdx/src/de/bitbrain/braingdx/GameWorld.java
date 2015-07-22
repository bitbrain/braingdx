package de.bitbrain.braingdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

import de.bitbrain.braingdx.graphics.RenderManager;

/**
 * Game world which handles game objects internally
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class GameWorld {

    private final List<GameObject> removals = new ArrayList<GameObject>();

    private final List<GameObject> objects = new ArrayList<GameObject>();

    private final Pool<GameObject> pool = new Pool<GameObject>(160) {
        @Override
        protected GameObject newObject() {
            return new GameObject();
        }
    };

    private WorldBounds bounds = new WorldBounds() {

        @Override
        public boolean isInBounds(GameObject object, OrthographicCamera camera) {
            return true;
        }
    };

    private OrthographicCamera camera;

    private RenderManager renderManager;

    public GameWorld(OrthographicCamera camera) {
        this.camera = camera;
        renderManager = new RenderManager();
    }

    public void setBounds(WorldBounds bounds) {
        this.bounds = bounds;
    }

    public GameObject addObject() {
        GameObject object = pool.obtain();
        objects.add(object);
        return object;
    }

    public void registerRenderer(Integer gameObjectId, RenderManager.Renderer renderer) {
        renderManager.register(gameObjectId, renderer);
    }

    public void updateAndRender(Batch batch, float delta) {
        for (GameObject object : objects) {
            if (!bounds.isInBounds(object, camera)) {
                removals.add(object);
                continue;
            }
            renderManager.render(object, batch, delta);
        }
        for (GameObject removal : removals) {
            remove(removal);
        }
        removals.clear();
    }

    public int size() {
        return objects.size();
    }

    public void remove(GameObject... objects) {
        for (GameObject object : objects) {
            removals.add(object);
        }
    }

    private void remove(GameObject object) {
        pool.free(object);
        objects.remove(object);
    }

    public void reset() {
        pool.clear();
        objects.clear();
        removals.clear();
    }

    public static interface WorldBounds {

        boolean isInBounds(GameObject object, OrthographicCamera camera);
    }
}
