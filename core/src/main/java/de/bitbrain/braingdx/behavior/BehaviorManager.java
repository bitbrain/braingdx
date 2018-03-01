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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;

import java.util.Set;

import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Manages behavior of game objects.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class BehaviorManager {

   private final Set<Behavior> globalBehaviors;
   private final Set<Behavior> globalBehaviorsAdditions;
   private final Set<Behavior> globalBehaviorsRemovals;
   
   private final Map<String, List<Behavior>> localBehaviors;   
   private final Map<String, List<Behavior>> localBehaviorAdditions;
   private final Set<String> localBehaviorRemovals;
   
   private final GameWorld world;

   public BehaviorManager(GameWorld world) {
      this.world = world;
      globalBehaviors = new HashSet<Behavior>();
      globalBehaviorsAdditions = new HashSet<Behavior>();
      globalBehaviorsRemovals = new HashSet<Behavior>();
      localBehaviors = new HashMap<String, List<Behavior>>();
      localBehaviorRemovals = new HashSet<String>();
      localBehaviorAdditions = new HashMap<String, List<Behavior>>();
   }

   public void apply(Behavior behavior, GameObject source) {
	   List<Behavior> additions = localBehaviorAdditions.get(source.getId());
	   if (additions == null) {
		   additions = new ArrayList<Behavior>();
		   localBehaviorAdditions.put(source.getId(), additions);
	   }
	   additions.add(behavior);
   }

   public void apply(Behavior behavior) {
	   globalBehaviorsAdditions.add(behavior);
   }

   public void remove(Behavior behavior) {
	   globalBehaviorsRemovals.add(behavior);
   }

   public void remove(GameObject source) {
	   localBehaviorRemovals.add(source.getId());
   }

	public void updateGlobally(GameObject source, float delta) {
		invalidateGlobally();
		for (Behavior behavior : globalBehaviors) {
			behavior.update(source, delta);
		}
		invalidateGlobally();
	}

   public void updateLocally(GameObject source, float delta) {
	   invalidateLocally();
      List<Behavior> behaviors = localBehaviors.get(source.getId());
      if (behaviors != null) {
         for (Behavior behavior : behaviors) {
            behavior.update(source, delta);
         }         
      }
      invalidateLocally();
      
   }

   public void updateLocallyCompared(GameObject source, GameObject target, float delta) {
      List<Behavior> behaviors = localBehaviors.get(source.getId());
      if (behaviors != null) {
         for (Behavior behavior : behaviors) {
            behavior.update(source, target, delta);
         }
      }
   }

   public void clear() {
      for (Entry<String, List<Behavior>> behaviors : localBehaviors.entrySet()) {
         for (Behavior behavior : behaviors.getValue()) {
            behavior.onDetach(world.getObjectById(behaviors.getKey()));
         }
      }
      localBehaviors.clear();
      localBehaviorAdditions.clear();
      localBehaviorRemovals.clear();
      globalBehaviors.clear();
      globalBehaviorsAdditions.clear();
      globalBehaviorsRemovals.clear();
   }

	private void invalidateGlobally() {
		for (Behavior addition : globalBehaviorsAdditions) {
			globalBehaviors.add(addition);
		}
		globalBehaviorsAdditions.clear();

		for (Behavior removal : globalBehaviorsRemovals) {
			globalBehaviors.remove(removal);
		}

		globalBehaviorsRemovals.clear();
	}
   
	private void invalidateLocally() {
		for (Entry<String, List<Behavior>> addition : localBehaviorAdditions.entrySet()) {
			List<Behavior> behaviors = localBehaviors.get(addition.getKey());
			if (behaviors == null) {
				behaviors = new ArrayList<Behavior>();
				localBehaviors.put(addition.getKey(), behaviors);
			}
			for (Behavior additionBehavior : addition.getValue()) {
				behaviors.add(additionBehavior);
				additionBehavior.onAttach(world.getObjectById(addition.getKey()));
			}
         for (Behavior globalBehavior : globalBehaviors) {
            globalBehavior.onAttach(world.getObjectById(addition.getKey()));
         }
		}
		localBehaviorAdditions.clear();
		for (String removal : localBehaviorRemovals) {
			List<Behavior> behaviors = localBehaviors.remove(removal);
			if (behaviors != null) {
				for (Behavior behavior : behaviors) {
					behavior.onDetach(world.getObjectById(removal));
				}
			}
			for (Behavior globalBehavior : globalBehaviors) {
				globalBehavior.onDetach(world.getObjectById(removal));
			}
		}
		localBehaviorRemovals.clear();
	}
}
