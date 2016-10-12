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

package de.bitbrain.braingdx.behavior;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.bitbrain.braingdx.GameObject;

/**
 * Manages behavior of game objects.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class BehaviorManager {

    private Map<String, Behavior> globalBehaviors;

    private Map<String, List<Behavior>> localBehaviors;

    public BehaviorManager() {
	globalBehaviors = new HashMap<String, Behavior>();
	localBehaviors = new HashMap<String, List<Behavior>>();
    }

    public void apply(Behavior behavior, GameObject source) {
	if (!localBehaviors.containsKey(source.getId())) {
	    localBehaviors.put(source.getId(), new ArrayList<Behavior>());
	}
	localBehaviors.get(source.getId()).add(behavior);
    }

    public void apply(Behavior behavior, String identifier) {
	globalBehaviors.put(identifier, behavior);
    }

    public void remove(String identifier) {
	globalBehaviors.remove(identifier);
    }

    public void remove(GameObject source) {
	List<Behavior> behaviors = localBehaviors.remove(source.getId());
	if (behaviors != null) {
	    for (Behavior behavior : behaviors) {
		behavior.onRemove(source);
	    }
	}
    }

    public void updateGlobally(GameObject source, float delta) {
	for (Behavior behavior : globalBehaviors.values()) {
	    behavior.update(source, delta);
	}
    }

    public void updateLocally(GameObject source, float delta) {
	List<Behavior> behaviors = localBehaviors.get(source.getId());
	if (behaviors != null) {
	    for (Behavior behavior : behaviors) {
		behavior.update(source, delta);
	    }
	}
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
	localBehaviors.clear();
	globalBehaviors.clear();
    }
}
