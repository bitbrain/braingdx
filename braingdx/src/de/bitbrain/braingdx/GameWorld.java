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

    public void setCameraTracking(GameObject object) {
        tracker.setTarget(object);
    }

    public void focusCamera() {
        tracker.focus();
    }

    public void setTrackingSpeed(float speed) {
        tracker.setSpeed(speed);
    }

    public void setTrackingZoomScale(float scale) {
        tracker.setZoomScale(scale);
    }

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
