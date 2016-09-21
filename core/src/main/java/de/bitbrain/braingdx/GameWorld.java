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

package de.bitbrain.braingdx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;

import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.CameraTracker;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.VectorCameraTracker;
import de.bitbrain.braingdx.util.ZIndexComparator;

/**
 * Game world which contains all game objects and managed them.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class GameWorld {

    /** the default cache size this world uses */
    public static final int DEFAULT_CACHE_SIZE = 100;

    public static interface WorldBounds {
	boolean isInBounds(GameObject object, OrthographicCamera camera);
    }

    private final List<GameObject> removals = new ArrayList<GameObject>();

    private final List<GameObject> objects = new ArrayList<GameObject>();

    private final Pool<GameObject> pool;

    private WorldBounds bounds = new WorldBounds() {

	@Override
	public boolean isInBounds(GameObject object, OrthographicCamera camera) {
	    return true;
	}
    };

    private OrthographicCamera camera;

    private GameObjectRenderManager renderManager;

    private CameraTracker tracker;

    private final Comparator<GameObject> comparator = new ZIndexComparator();

    private final BehaviorManager behaviorManager = new BehaviorManager();

    public GameWorld(OrthographicCamera camera) {
	this(camera, DEFAULT_CACHE_SIZE);
    }

    public GameWorld(OrthographicCamera camera, GameObjectRenderManager renderManager, CameraTracker tracker, int cacheSize) {
	this.camera = camera;
	this.renderManager = renderManager;
	this.tracker = tracker;
	this.pool = new Pool<GameObject>(cacheSize) {
	    @Override
	    protected GameObject newObject() {
		return new GameObject();
	    }
	};
    }

    public GameWorld(OrthographicCamera camera, int cacheSize) {
	this(camera, new GameObjectRenderManager(), new VectorCameraTracker(camera), cacheSize);
    }

    public void applyBehavior(String id, Behavior behavior) {
	behaviorManager.apply(behavior, id);
    }

    public void removeBehavior(String id) {
	behaviorManager.remove(id);
    }

    public void removeBehavior(GameObject source) {
	behaviorManager.remove(source);
    }

    public void applyBehavior(Behavior behavior, GameObject source) {
	behaviorManager.apply(behavior, source);
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
	final GameObject object = pool.obtain();
	objects.add(object);
	return object;
    }

    /**
     * Registers a renderer for an existing game object of the given type (id)
     *
     * @param gameObjectId type/id of the game object
     * @param renderer instance of the renderer
     */
    public void registerRenderer(Integer gameObjectId, GameObjectRenderManager.GameObjectRenderer renderer) {
	renderManager.register(gameObjectId, renderer);
    }

    /**
     * Enables camera tracking for the given object. Tracking can be disabled by providing null.
     *
     * @param object game object which should be tracked.
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
	Collections.sort(objects, comparator);
	for (final GameObject object : objects) {
	    if (!bounds.isInBounds(object, camera)) {
		removals.add(object);
		continue;
	    }
	    behaviorManager.updateGlobally(object, delta);
	    behaviorManager.updateLocally(object, delta);
	    for (final GameObject other : objects) {
		if (!object.getId().equals(other.getId())) {
		    behaviorManager.updateLocallyCompared(object, other, delta);
		}
	    }
	    renderManager.render(object, batch, delta);
	}
	tracker.update(delta);
	for (final GameObject removal : removals)
	    remove(removal);
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
	for (final GameObject object : objects)
	    removals.add(object);
    }

    private void remove(GameObject object) {
	pool.free(object);
	objects.remove(object);
    }
}
