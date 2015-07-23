/* brainGDX project provides utilities for libGDX
 * Copyright (C) 2015 Miguel Gonzalez
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bitbrain.braingdx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.List;

import de.bitbrain.braingdx.graphics.CameraTracker;
import de.bitbrain.braingdx.graphics.RenderManager;

/**
 * Game world which contains all game objects and managed them.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class GameWorld {

    public static interface WorldBounds {
        boolean isInBounds(GameObject object, OrthographicCamera camera);
    }

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

    private CameraTracker tracker;

    public GameWorld(OrthographicCamera camera) {
        this.camera = camera;
        renderManager = new RenderManager();
        tracker = new CameraTracker(camera);
    }

    /**
     * Sets the bounds of the world. By default, everything is in bounds.
     *
     * @param bounds the new bounds implementation
     */
    public void setBounds(WorldBounds bounds) {
        this.bounds = bounds;
    }

    /**
     * Adds a new game object to the game world and provides it.
     *
     * @return newly created game object
     */
    public GameObject addObject() {
        GameObject object = pool.obtain();
        objects.add(object);
        return object;
    }

    /**
     * Registers a renderer for an existing game object of the given type (id)
     *
     * @param gameObjectId type/id of the game object
     * @param renderer instance of the renderer
     */
    public void registerRenderer(Integer gameObjectId, RenderManager.Renderer renderer) {
        renderManager.register(gameObjectId, renderer);
    }

    /**
     * Enables camera tracking for the given object. Tracking can be disabled by providing null.
     *
     * @param object game object shich should be tracked.
     */
    public void setCameraTracking(GameObject object) {
        tracker.setTarget(object);
    }

    /**
     * Focuses the camera on its target (if available)
     */
    public void focusCamera() {
        tracker.focus();
    }

    /**
     * Sets the speed the camera should follow its target
     *
     * @param speed camera tracking speed
     */
    public void setTrackingSpeed(float speed) {
        tracker.setSpeed(speed);
    }

    /**
     * Sets the zoom scale the camera should have while following its target
     *
     * @param scale scale factor of the camera while tracking a target
     */
    public void setTrackingZoomScale(float scale) {
        tracker.setZoomScale(scale);
    }

    /**
     * Updates and renders this world
     *
     * @param batch the batch
     * @param delta frame delta
     */
    public void updateAndRender(Batch batch, float delta) {
        for (GameObject object : objects) {
            if (!bounds.isInBounds(object, camera)) {
                removals.add(object);
                continue;
            }
            renderManager.render(object, batch, delta);
        }
        tracker.update(delta);
        for (GameObject removal : removals) {
            remove(removal);
        }
        removals.clear();
    }

    /**
     * Number of active objects in the world
     *
     * @return
     */
    public int size() {
        return objects.size();
    }

    /**
     * Resets this world object
     */
    public void reset() {
        pool.clear();
        objects.clear();
        removals.clear();
    }

    /**
     * Removes the given game objects from this world
     *
     * @param objects
     */
    public void remove(GameObject... objects) {
        for (GameObject object : objects) {
            removals.add(object);
        }
    }

    private void remove(GameObject object) {
        pool.free(object);
        objects.remove(object);
    }
}