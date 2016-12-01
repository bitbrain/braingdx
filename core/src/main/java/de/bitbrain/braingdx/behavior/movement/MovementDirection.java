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

package de.bitbrain.braingdx.behavior.movement;

import com.badlogic.gdx.math.Vector2;

/**
 * Describes movement directions.
 * 
 * @author Miguel Gonzalez Sanchez
 */
public enum MovementDirection {
    
    LEFT(-1, 0), 
    RIGHT(1, 0), 
    DOWN(0, 1), 
    UP(0, -1);

    private Vector2 direction;

    MovementDirection(float x, float y) {
	direction = new Vector2(x, y);
    }

    public float getXFactor() {
	return direction.x;
    }

    public float getYFactor() {
	return direction.y;
    }
}
