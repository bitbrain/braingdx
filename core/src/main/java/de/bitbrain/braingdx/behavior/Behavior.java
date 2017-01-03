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

import de.bitbrain.braingdx.world.GameObject;

/**
 * Behavior which is executed every single frame
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public interface Behavior {

    /**
     * Callback which is called whenever this behavior gets attached to the source provided.
     * 
     * @param source the source where this behavior gets attached to
     */
    void onAttach(GameObject source);

    /**
     * Callback which is called whenever this behavior gets detached from the source provided.
     * 
     * @param source the source where this behavior gets detached from
     */
    void onDetach(GameObject source);

    /**
     * Updates the behavior for a given source object.
     * 
     * @param source the game object to apply the behavior to
     * @param delta current frame delta
     */
    void update(GameObject source, float delta);

    /**
     * Updates the behavior for a given source and a related target object.
     * 
     * @param source the game object to apply the behavior to
     * @param target the game object to get information from
     * @param delta current frame delta
     */
    void update(GameObject source, GameObject target, float delta);
}
