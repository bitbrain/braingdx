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

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.util.ZIndexComparator;

import java.util.*;

/**
 * Game world which contains all game objects and managed them.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameWorld implements Iterable<GameObject> {

   /**
    * the default cache size this world uses
    */
   public static final int DEFAULT_CACHE_SIZE = 512;
   private final List<GameObject> objects = new ArrayList<GameObject>();
   private final Map<String, GameObject> identityMap = new HashMap<String, GameObject>();
   private final List<GameObject> unmodifiableObjects;
   private final Pool<GameObject> pool;
   private final Comparator<GameObject> comparator = new ZIndexComparator();
   private final List<GameWorldListener> listeners = new ArrayList<GameWorldListener>();
   private WorldBounds bounds = new WorldBounds() {

      @Override
      public boolean isInBounds(GameObject object) {
         return true;
      }

      @Override
      public float getWorldWidth() {
         return 0f;
      }

      @Override
      public float getWorldHeight() {
         return 0f;
      }

      @Override
      public float getWorldOffsetX() {
         return 0;
      }

      @Override
      public float getWorldOffsetY() {
         return 0;
      }
   };

   private OrthographicCamera camera;

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
    * Provides the world bounds.
    *
    * @return bounds the currently active world bounds
    */
   public WorldBounds getBounds() {
      return bounds;
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
      if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
         Gdx.app.debug("DEBUG", "GameWorld - obtaining new object...");
      }
      final GameObject object = pool.obtain();
      if (identityMap.containsKey(object.getId())) {
         Gdx.app.error("FATAL", String.format(
               "GameWorld - game object %s already exists. Unable to add new object %s",
               object,
               identityMap.get(object.getId())
               )
         );
         return object;
      }
      if (lazy) {
         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("DEBUG", String.format("GameWorld - requested addition for new game object %s", object));
         }
         Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
               objects.add(object);
               for (int i = 0; i < listeners.size(); ++i) {
                  listeners.get(i).onAdd(object);
               }
            }
         });
      } else {
         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("DEBUG", String.format("GameWorld - added new game object %s", object));
         }
         objects.add(object);
         for (int i = 0; i < listeners.size(); ++i) {
            listeners.get(i).onAdd(object);
         }
      }
      if (mutator != null) {
         mutator.mutate(object);
      }
      identityMap.put(object.getId(), object);
      return object;
   }

   /**
    * Updates and renders this world
    *
    * @param delta frame delta
    */
   public void update(float delta) {
      Collections.sort(objects, comparator);
      for (int i = 0; i < objects.size(); ++i) {
         GameObject object = objects.get(i);
         if (!bounds.isInBounds(object) && !object.isPersistent()) {
            Gdx.app.debug("DEBUG", String.format("GameWorld - object %s is out of bounds! Remove...", object));
            remove(object);
            continue;
         }
         for (int listenerIndex = 0; listenerIndex < listeners.size(); ++listenerIndex) {
            listeners.get(listenerIndex).onUpdate(object, delta);
         }
         if (object.isActive()) {
            for (int otherObjIndex = 0; otherObjIndex < objects.size(); ++otherObjIndex) {
               GameObject other = objects.get(otherObjIndex);
               if (other.isActive() && !object.getId().equals(other.getId())) {
                  for (int listenerIndex = 0; listenerIndex < listeners.size(); ++listenerIndex) {
                     listeners.get(listenerIndex).onUpdate(object, other, delta);
                  }
               }
            }
         }
      }
   }

   /**
    * Gets an object by id
    */
   public GameObject getObjectById(String id) {
      return identityMap.get(id);
   }

   /**
    * Returns a list of all objects within this world.
    */
   public List<GameObject> getObjects() {
      return getObjects(null);
   }


   /**
    * Returns a list of all objects within this world, ordered by the comparator provided. The comparator
    * can be null.
    */
   public List<GameObject> getObjects(Comparator<GameObject> comparator) {
      List<GameObject> result = new ArrayList<GameObject>(objects);
      if (comparator != null) {
         Collections.sort(result, comparator);
      }
      return result;
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
      identityMap.clear();
      for (GameWorldListener l : listeners) {
         l.onClear();
      }
      Gdx.app.debug("DEBUG", "GameWorld - Cleared all game objects!");
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
   public void remove(final GameObject... objects) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            for (final GameObject object : objects) {
               if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
                  Gdx.app.debug("DEBUG", String.format("GameWorld - requested removal of game object %s", object));
               }
               removeInternally(object);
            }
         }
      });

   }

   private void removeInternally(GameObject object) {
      if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
         Gdx.app.debug("DEBUG", String.format("%s - GameWorld - removing game object %s", System.nanoTime(), object));
      }
      if (identityMap.remove(object.getId()) == null) {
         Gdx.app.error("FATAL", String.format("%s - GameWorld - game object %s does not exist.", System.nanoTime(), object));
      }
      for (int i = 0; i < listeners.size(); ++i) {
         listeners.get(i).onRemove(object);
      }
      pool.free(object);
      objects.remove(object);
   }

   /**
    * Describes when a game object is in bounds.
    */
   public interface WorldBounds {
      boolean isInBounds(GameObject object);

      float getWorldWidth();

      float getWorldHeight();

      float getWorldOffsetX();

      float getWorldOffsetY();
   }

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
}
