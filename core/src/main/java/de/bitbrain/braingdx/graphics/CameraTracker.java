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

package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.GameObject;

/**
 * Tracks a camera and make it following a target
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class CameraTracker {

    private OrthographicCamera camera;

    private Vector2 velocity;

    private GameObject target;

    private float speed = 5.2f;

    private float zoomScale = 0.0025f;

    public CameraTracker(OrthographicCamera camera) {
	this.camera = camera;
	velocity = new Vector2();
    }

    public void setTarget(GameObject target) {
	this.target = target;
    }

    public void update(float delta) {
	if (target == null) {
	    return;
	}
	velocity.x = (float) (target.getLeft() + Math.floor(target.getWidth() / 2.0f) - (camera.position.x));
	velocity.y = (float) (target.getTop() + Math.floor(target.getHeight() / 2.0f) - (camera.position.y + 100f));

	float distance = velocity.len();
	velocity.nor();
	double overAllSpeed = distance * speed;

	// Round it up to prevent camera shaking
	camera.position.x = (float) (camera.position.x + (velocity.x * overAllSpeed * delta));
	camera.position.y = (float) (camera.position.y + (velocity.y * overAllSpeed * delta));
	camera.zoom = 1.0f + zoomScale * distance;
    }

    public void setSpeed(float speed) {
	this.speed = speed;
    }

    public void setZoomScale(float zoomScale) {
	this.zoomScale = zoomScale;
    }

    public void focus() {
	if (target == null) {
	    return;
	}
	camera.position.x = target.getLeft();
	camera.position.y = target.getTop();
	camera.zoom = 1;
    }
}
