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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.math.QuadTree;
import de.bitbrain.braingdx.util.Group;
import de.bitbrain.braingdx.util.Mutator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Game world which contains all game objects and managed them.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameWorld {

   public static final String DEFAULT_GROUP_ID = "GlobalGameWorldGroup";

   /**
    * the default cache size this world uses
    */
   public static final int DEFAULT_CACHE_SIZE = 512;
   private final Group<Object, GameObject> objects = new Group<Object, GameObject>();
   private final Map<String, GameObject> identityMap = new HashMap<String, GameObject>();
   private final Pool<GameObject> pool;
   private final Array<GameWorldListener> listeners = new Array<GameWorldListener>();
   final QuadTree quadTree;
   private final Rectangle boundsRectangle, tmp;
   private GameCamera gameCamera;
   private final Array<GameObject> updateableObjects = new Array<GameObject>(200);
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

   public GameWorld() {
      this(DEFAULT_CACHE_SIZE);
   }

   public GameWorld(int cacheSize) {
      this.pool = new Pool<GameObject>(cacheSize) {
         @Override
         protected GameObject newObject() {
            return new GameObject();
         }
      };
      this.boundsRectangle = new Rectangle();
      this.quadTree = new QuadTree(5, 6, 0, boundsRectangle);
      this.tmp = new Rectangle();
   }

   public GameWorld(int cacheSize, QuadTree quadTree) {
      this.pool = new Pool<GameObject>(cacheSize) {
         @Override
         protected GameObject newObject() {
            return new GameObject();
         }
      };
      this.boundsRectangle = new Rectangle();
      this.quadTree = quadTree;
      this.tmp = new Rectangle();
   }

   public void setCamera(GameCamera gameCamera) {
      this.gameCamera = gameCamera;
   }

   public void addListener(GameWorldListener listener) {
      listeners.add(listener);
   }

   public void removeListener(GameWorldListener listener) {
      listeners.removeValue(listener, false);
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
   public GameObject addObject(Object group) {
      return addObject(group, false);
   }

   /**
    * Adds a new game object to the game world and provides it.
    *
    * @return newly created game object
    */
   public GameObject addObject() {
      return addObject(DEFAULT_GROUP_ID, false);
   }

   /**
    * Adds a new game object to the game world and provides it.
    *
    * @return newly created game object
    */
   public GameObject addObject(Object group, boolean lazy) {
      return addObject(group, null, lazy);
   }

   /**
    * Adds a new game object to the game world and provides it.
    *
    * @return newly created game object
    */
   public GameObject addObject(Mutator<GameObject> mutator, boolean lazy) {
      return addObject(DEFAULT_GROUP_ID, mutator, lazy);
   }

   /**
    * Adds a new game object to the game world and provides it.
    *
    * @return newly created game object
    */
   public GameObject addObject(boolean lazy) {
      return addObject(DEFAULT_GROUP_ID, lazy);
   }

   /**
    * Adds a new game object to the game world with a custom ID
    *
    * @param mutator the mutator which might change the GameObject
    * @return newly created game object
    */
   public GameObject addObject(Mutator<GameObject> mutator) {
      return addObject(DEFAULT_GROUP_ID, mutator, false);
   }

   /**
    * Adds a new game object to the game world with a custom ID
    *
    * @param mutator the mutator which might change the GameObject
    * @return newly created game object
    */
   public GameObject addObject(Object group, Mutator<GameObject> mutator) {
      return addObject(group, mutator, false);
   }

   /**
    * Adds a new game object to the game world with a custom ID
    *
    * @param mutator the mutator which might change the GameObject
    * @return newly created game object
    */
   public GameObject addObject(final Object group, Mutator<GameObject> mutator, boolean lazy) {
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
      if (mutator != null) {
         mutator.mutate(object);
      }
      identityMap.put(object.getId(), object);
      if (lazy) {
         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("DEBUG", String.format("GameWorld - requested addition for new game object %s", object));
         }
         Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
               objects.addToGroup(group, object);
               for (int i = 0; i < listeners.size; ++i) {
                  listeners.get(i).onAdd(object);
               }
            }
         });
      } else {
         if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
            Gdx.app.debug("DEBUG", String.format("GameWorld - added new game object %s", object));
         }
         objects.addToGroup(group, object);
         for (int i = 0; i < listeners.size; ++i) {
            listeners.get(i).onAdd(object);
         }
      }
      return object;
   }

   /**
    * Updates and renders this world
    *
    * @param delta frame delta
    */
   public void update(float delta) {
      updateUpdatableObjects();
      for (int i = 0; i < updateableObjects.size; ++i) {
         GameObject object = updateableObjects.get(i);
         if (!bounds.isInBounds(object) && !object.isPersistent()) {
            Gdx.app.debug("DEBUG", String.format("GameWorld - object %s is out of bounds! Remove...", object));
            remove(object);
            continue;
         }
         for (int listenerIndex = 0; listenerIndex < listeners.size; ++listenerIndex) {
            listeners.get(listenerIndex).onUpdate(object, delta);
         }
         if (object.isActive()) {
            for (int otherObjIndex = 0; otherObjIndex < updateableObjects.size; ++otherObjIndex) {
               GameObject other = updateableObjects.get(otherObjIndex);
               if (other.isActive() && !object.getId().equals(other.getId())) {
                  for (int listenerIndex = 0; listenerIndex < listeners.size; ++listenerIndex) {
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
   public Array<GameObject> getObjects() {
      return getObjects(null, false);
   }

   public Array<GameObject> getObjects(Comparator<GameObject> comparator) {
      return getObjects(comparator, false);
   }

   /**
    * Returns a list of all objects within this world, ordered by the comparator provided. The comparator
    * can be null.
    */
   public Array<GameObject> getObjects(Comparator<GameObject> comparator, boolean updatableOnly) {
      Array<GameObject> result;
      if (updatableOnly) {
         result = updateableObjects;
      } else {
         result = objects.getAll();
      }
      if (comparator != null) {
         result.sort(comparator);
      }
      return result;
   }

   /**
    * Number of active objects in the world
    *
    * @return
    */
   public int size() {
      return identityMap.size();
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

   public void clearGroup(Object groupKey) {
      Array<GameObject> group = objects.getGroup(groupKey);
      if (group != null) {
         group = new Array<GameObject>(group);
         for (int i = 0; i < group.size; i++) {
            removeInternally(group.get(i).getId());
         }
         objects.clearGroup(groupKey);
      }
   }

   public Array<GameObject> getGroup(Object groupKey) {
      return objects.getGroup(groupKey);
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
               removeInternally(object.getId());
            }
         }
      });
   }

   public void remove(final String... ids) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            for (final String id : ids) {
               if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
                  Gdx.app.debug("DEBUG", String.format("GameWorld - requested removal of game object with id %s", id));
               }
               removeInternally(id);
            }
         }
      });
   }

   private void removeInternally(String id) {
      if (Gdx.app.getLogLevel() == Application.LOG_DEBUG) {
         Gdx.app.debug("DEBUG", String.format("%s - GameWorld - removing game object with id %s", System.nanoTime(), id));
      }
      GameObject object = identityMap.remove(id);
      if (object == null) {
         Gdx.app.debug("DEBUG", String.format("%s - GameWorld - game object with id %s does not exist any longer.", System.nanoTime(), id));
         return;
      }
      for (int i = 0; i < listeners.size; ++i) {
         listeners.get(i).onRemove(object);
      }
      objects.remove(object);
      pool.free(object);
   }

   private void updateUpdatableObjects() {
      boundsRectangle.set(bounds.getWorldOffsetX(), bounds.getWorldOffsetY(), bounds.getWorldWidth(), bounds.getWorldHeight());
      Array<GameObject> allObjects = objects.getAll();
      quadTree.clear();
      for (GameObject o : allObjects) {
         quadTree.insert(o);
      }
      updateableObjects.clear();
      tmp.set(gameCamera.getLeft(), gameCamera.getTop(), gameCamera.getScaledCameraWidth(), gameCamera.getScaledCameraHeight());
      quadTree.retrieve(updateableObjects, tmp);
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
