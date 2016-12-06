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

import java.math.BigDecimal;
import java.math.MathContext;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.world.GameObject;

/**
 * Using underlying vectors to calculate the camera tracking.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class VectorGameCamera implements GameCamera {

    private static final MathContext PRECISION = MathContext.DECIMAL128;

    private final OrthographicCamera camera;
    private final Vector2 velocity;
    private GameObject target;
    private BigDecimal speed = new BigDecimal(6.2f, PRECISION);
    private BigDecimal zoomScale = new BigDecimal(0.0025f, PRECISION);
    private boolean focusRequested = false;

    public VectorGameCamera(OrthographicCamera camera) {
	this.camera = camera;
	velocity = new Vector2();
    }

    @Override
    public void setTarget(GameObject target) {
	this.target = target;
	focus();
    }

    @Override
    public void update(float delta) {
	if (target == null)
	    return;
	if (speed.round(PRECISION).longValue() == 0L) {
	    focus();
	}
	if (focusRequested) {
	    camera.position.x = target.getLeft() + target.getOffset().x + target.getWidth() / 2f;
	    camera.position.y = target.getTop() + target.getOffset().y + target.getHeight() / 2f;
	    focusRequested = false;
	} else {
	    BigDecimal preciseDelta = BigDecimal.valueOf(delta);
	    BigDecimal left = new BigDecimal(target.getLeft() + target.getOffset().x, PRECISION);
	    BigDecimal width = new BigDecimal(target.getWidth(), PRECISION);
	    BigDecimal camLeft = new BigDecimal(camera.position.x, PRECISION);
	    BigDecimal top = new BigDecimal(target.getTop() + target.getOffset().y, PRECISION);
	    BigDecimal height = new BigDecimal(target.getHeight(), PRECISION);
	    BigDecimal camTop = new BigDecimal(camera.position.y, PRECISION);

	    velocity.x = left.add(width.divide(BigDecimal.valueOf(2.0))).subtract(camLeft).floatValue();
	    velocity.y = top.add(height.divide(BigDecimal.valueOf(2.0))).subtract(camTop).floatValue();

	    BigDecimal distance = BigDecimal.valueOf(velocity.len());
	    velocity.nor();
	    BigDecimal overAllSpeed = distance.multiply(speed);

	    BigDecimal deltaX = BigDecimal.valueOf(velocity.x).multiply(overAllSpeed).multiply(preciseDelta);
	    BigDecimal deltaY = BigDecimal.valueOf(velocity.y).multiply(overAllSpeed).multiply(preciseDelta);
	    camera.position.x = camLeft.add(deltaX).floatValue();
	    camera.position.y = camTop.add(deltaY).floatValue();
	    camera.zoom = zoomScale.multiply(distance).add(BigDecimal.ONE).floatValue();
	}
	camera.update();
    }

    @Override
    public void setSpeed(float speed) {
	this.speed = new BigDecimal(Math.abs(speed), PRECISION);
    }

    @Override
    public void setZoomScale(float zoomScale) {
	this.zoomScale = new BigDecimal(zoomScale, PRECISION);
    }

    @Override
    public void focus() {
	focusRequested = true;
    }

    @Override
    public Camera getInternal() {
	return camera;
    }
}
