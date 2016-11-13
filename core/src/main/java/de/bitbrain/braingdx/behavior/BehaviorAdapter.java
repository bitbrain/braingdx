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

import de.bitbrain.braingdx.world.GameObject;

/**
 * Adapter for behaviors
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public abstract class BehaviorAdapter implements Behavior {

    @Override
    public void onAttach(GameObject source) {
	// noOp
    }

    @Override
    public void onDetach(GameObject source) {
	// noOp
    }

    @Override
    public void update(GameObject source, float delta) {
	// noOp
    }

    @Override
    public void update(GameObject source, GameObject target, float delta) {
	// noOp
    }

}
