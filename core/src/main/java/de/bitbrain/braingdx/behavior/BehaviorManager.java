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

package de.bitbrain.braingdx.behavior;

import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.*;
import java.util.Map.Entry;

/**
 * Manages behavior of game objects.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class BehaviorManager {

   private final List<Behavior> globalBehaviors;

   private final List<Updateable> updateables;

   private final Map<String, List<Behavior>> localBehaviors;

   private final GameWorld world;

   public BehaviorManager(GameWorld world) {
      this.world = world;
      globalBehaviors = new ArrayList<Behavior>();
      updateables = new ArrayList<Updateable>();
      localBehaviors = new HashMap<String, List<Behavior>>();
   }

   public void apply(final Behavior behavior, final GameObject source) {
      final String id = source.getId();
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            List<Behavior> behaviors = localBehaviors.get(id);
            if (behaviors == null) {
               behaviors = new ArrayList<Behavior>();
               localBehaviors.put(id, behaviors);
            }
            behaviors.add(behavior);
            if (behavior instanceof Updateable) {
               updateables.add((Updateable) behavior);
            }
            behavior.onAttach(source);
            for (Behavior behavior : globalBehaviors) {
               behavior.onAttach(source);
            }
         }
      });
   }

   public void apply(final Behavior behavior) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            if (behavior instanceof Updateable) {
               updateables.add((Updateable) behavior);
            }
            globalBehaviors.add(behavior);
         }
      });
   }

   public void remove(final Behavior behavior) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            if (behavior instanceof Updateable) {
               updateables.remove(behavior);
            }
            globalBehaviors.remove(behavior);
         }
      });
   }

   public void remove(final GameObject source, final Behavior behavior) {
      final String id = source.getId();
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            List<Behavior> behaviors = localBehaviors.get(id);
            if (behaviors != null && behaviors.contains(behavior)) {
               behaviors.remove(behavior);
               behavior.onDetach(source);
            }
         }
      });
   }

   public void remove(final GameObject source) {
      final String id = source.getId();
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            List<Behavior> behaviors = localBehaviors.remove(id);
            if (behaviors != null) {
               for (Behavior behavior : behaviors) {
                  if (behavior instanceof Updateable) {
                     updateables.remove(behavior);
                  }
                  behavior.onDetach(source);
               }
            }
            for (Behavior behavior : globalBehaviors) {
               behavior.onDetach(source);
            }
         }
      });
   }

   public void update(float delta) {
      for (int i = 0; i < updateables.size(); ++i) {
         updateables.get(i).update(delta);
      }
   }

   public void updateGlobally(GameObject source, float delta) {
      for (int i = 0; i < globalBehaviors.size(); ++i) {
         globalBehaviors.get(i).update(source, delta);
      }
   }

   public void updateLocally(GameObject source, float delta) {
      List<Behavior> behaviors = localBehaviors.get(source.getId());
      if (behaviors != null) {
         for (int i = 0; i < behaviors.size(); ++i) {
            behaviors.get(i).update(source, delta);
         }
      }

   }

   public void updateLocallyCompared(GameObject source, GameObject target, float delta) {
      List<Behavior> behaviors = localBehaviors.get(source.getId());
      if (behaviors != null) {
         for (int i = 0; i < behaviors.size(); ++i) {
            behaviors.get(i).update(source, target, delta);
         }
      }
   }

   public void updateGloballyCompared(GameObject source, GameObject target, float delta) {
      for (int i = 0; i < globalBehaviors.size(); ++i) {
         globalBehaviors.get(i).update(source, target, delta);
      }
   }

   public void clear() {
      for (Entry<String, List<Behavior>> behaviors : localBehaviors.entrySet()) {
         for (Behavior behavior : behaviors.getValue()) {
            behavior.onDetach(world.getObjectById(behaviors.getKey()));
         }
      }
      localBehaviors.clear();
      globalBehaviors.clear();
   }
}
