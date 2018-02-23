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

package de.bitbrain.braingdx.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;

import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.util.ZIndexComparator;

/**
 * Game world which contains all game objects and managed them.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class GameWorld implements Iterable<GameObject> {

   /** the default cache size this world uses */
   public static final int DEFAULT_CACHE_SIZE = 100;

   /**
    * Listens to GameWorld events.
    */
   public static class GameWorldListener {
      public void onAdd(GameObject object) {
      }

      public void onRemove(GameObject object) {
      }

      public void onUpdate(GameObject object, float delta) {
      }

      public void onUpdate(GameObject object, GameObject other, float delta) {
      }

      public void onClear() {
      }
   }

   /**
    * Describes when a game object is in bounds.
    */
   public static interface WorldBounds {
      boolean isInBounds(GameObject object, OrthographicCamera camera);
   }

   private final List<GameObject> removals = new ArrayList<GameObject>();
   
   private final List<GameObject> additions = new ArrayList<GameObject>();

   private final List<GameObject> objects = new ArrayList<GameObject>();
   
   private final Map<String, GameObject> identityMap = new HashMap<String, GameObject>();

   private final List<GameObject> unmodifiableObjects;

   private final Pool<GameObject> pool;

   private WorldBounds bounds = new WorldBounds() {

      @Override
      public boolean isInBounds(GameObject object, OrthographicCamera camera) {
         return true;
      }
   };

   private OrthographicCamera camera;

   private final Comparator<GameObject> comparator = new ZIndexComparator();

   private final Set<GameWorldListener> listeners = new HashSet<GameWorldListener>();

   public GameWorld(OrthographicCamera camera) {
      this(camera, DEFAULT_CACHE_SIZE);
   }

   public GameWorld(OrthographicCamera camera, int cacheSize) {
      unmodifiableObjects = Collections.unmodifiableList(objects);
      this.camera = camera;
      this.pool = new Pool<GameObject>(cacheSize) {
         @Override
         protected GameObject newObject() {
            return new GameObject();
         }
      };
   }

   public void addListener(GameWorldListener listener) {
      listeners.add(listener);
   }

   public void removeListener(GameWorldListener listener) {
      listeners.remove(listener);
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
      return addObject(null, false);
   }

   /**
    * Adds a new game object to the game world and provides it.
    *
    * @return newly created game object
    */
   public GameObject addObject(boolean lazy) {
      return addObject(null, lazy);
   }
   
   /**
    * Adds a new game object to the game world with a custom ID
    * 
    * @param mutator the mutator which might change the GameObject
    * @return newly created game object
    */
   public GameObject addObject(Mutator<GameObject> mutator) {
	   return addObject(mutator, false);
   }
   
   /**
    * Adds a new game object to the game world with a custom ID
    * 
    * @param mutator the mutator which might change the GameObject
    * @return newly created game object
    */
   public GameObject addObject(Mutator<GameObject> mutator, boolean lazy) {
	   final GameObject object = pool.obtain();
	   if (mutator != null) {
		   mutator.mutate(object);
	   }
	   if (identityMap.containsKey(object.getId())) {
	       Gdx.app.log("ERROR", "Unable to add game object with ID '" + object.getId() + "' to game world. Already exists!");
	       return object;
	   }
	   if (lazy) {
		   additions.add(object);
	   } else {
		   objects.add(object);
		   for (GameWorldListener l : listeners) {
		       l.onAdd(object);
		   }
	   }
	   identityMap.put(object.getId(), object);
	   return object;
   }

   /**
    * Updates and renders this world
    *
    * @param batch the batch
    * @param delta frame delta
    */
   public void update(float delta) {
	  for (GameObject addition : additions) {
		  objects.add(addition);
		   for (GameWorldListener l : listeners) {
		       l.onAdd(addition);
		   }
	  }
	  additions.clear();
      Collections.sort(objects, comparator);
      for (GameObject object : objects) {
         if (!bounds.isInBounds(object, camera)) {
            removals.add(object);
            continue;
         }
         for (GameWorldListener l : listeners) {
            l.onUpdate(object, delta);
         }
         if (object.isActive()) {
            for (GameObject other : objects) {
               if (other.isActive() && !object.getId().equals(other.getId())) {
                  for (GameWorldListener l : listeners) {
                     l.onUpdate(object, other, delta);
                  }
               }
            }
         }
      }

      for (final GameObject removal : removals) {
         removeInternally(removal);
      }
      removals.clear();
   }
   
   public GameObject getObjectById(String id) {
	   return identityMap.get(id);
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
   public void clear() {
      pool.clear();
      objects.clear();
      removals.clear();
      identityMap.clear();
      for (GameWorldListener l : listeners) {
         l.onClear();
      }
   }

   @Override
   public Iterator<GameObject> iterator() {
      return unmodifiableObjects.iterator();
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

   private void removeInternally(GameObject object) {
      pool.free(object);
      objects.remove(object);
      identityMap.remove(object.getId());
      for (GameWorldListener l : listeners) {
         l.onRemove(object);
      }
   }
}
