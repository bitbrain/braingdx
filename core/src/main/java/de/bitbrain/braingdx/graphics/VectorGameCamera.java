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

package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.braingdx.world.GameWorld.WorldBounds;

import java.math.BigDecimal;
import java.math.MathContext;

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
	private final GameWorld world;
	
	private GameObject target;
	private BigDecimal speed = new BigDecimal(6.2f, PRECISION);
	private BigDecimal zoomScale = new BigDecimal(0.0025f, PRECISION);
	private boolean focusRequested = false;
	private BigDecimal baseZoom = new BigDecimal(1, PRECISION);
	private boolean worldBoundsStickiness = true;
	private boolean adjustedX = false;
	private boolean adjustedY = false;

	public VectorGameCamera(OrthographicCamera camera, GameWorld world) {
		this.camera = camera;
		velocity = new Vector2();
		this.world = world;
	}

	@Override
	public void setTarget(GameObject target) {
		setTarget(target, true);
	}

	@Override
	public void setTarget(GameObject target, boolean focus) {
		this.target = target;
		if (focus) {
			focus();
		}
	}

	@Override
	public void update(float delta) {
		if (target == null)
			return;
		if (focusRequested) {
		   focus(target);
			focusRequested = false;
		} else {
			BigDecimal preciseDelta = BigDecimal.valueOf(delta);
			BigDecimal left = new BigDecimal(target.getLeft() + target.getOffset().x, PRECISION);
			BigDecimal width = new BigDecimal(target.getWidth(), PRECISION);
			BigDecimal camLeft = new BigDecimal(camera.position.x, PRECISION);
			BigDecimal top = new BigDecimal(target.getTop() + target.getOffset().y, PRECISION);
			BigDecimal height = new BigDecimal(target.getHeight(), PRECISION);
			BigDecimal camTop = new BigDecimal(camera.position.y, PRECISION);

			if (!adjustedX) {
			   velocity.x = left.add(width.divide(BigDecimal.valueOf(2.0))).subtract(camLeft).floatValue();
			}
			if (!adjustedY) {
			   velocity.y = top.add(height.divide(BigDecimal.valueOf(2.0))).subtract(camTop).floatValue();
			}

			BigDecimal distance = BigDecimal.valueOf(velocity.len());
			BigDecimal overAllSpeed = distance.multiply(speed);

			BigDecimal deltaX = BigDecimal.valueOf(velocity.x).multiply(overAllSpeed).multiply(preciseDelta);
			BigDecimal deltaY = BigDecimal.valueOf(velocity.y).multiply(overAllSpeed).multiply(preciseDelta);
			if (!adjustedX) {
			   camera.position.x = camLeft.add(deltaX).floatValue();
			}
			if (!adjustedY) {
			   camera.position.y = camTop.add(deltaY).floatValue();
			}
			if (!adjustedX && !adjustedY) {
			   camera.zoom = zoomScale.multiply(distance).add(baseZoom).floatValue();
			}
		}
      adjustedX = false;
      adjustedY = false;
      if (worldBoundsStickiness) {
         applyWorldBounds();
      }

      camera.update();
	}

   @Override
   public void resize(int width, int height) {
      adjustedX = false;
      adjustedY = false;
      focusRequested = true;
      camera.setToOrtho(false, width, height);
   }

	@Override
	public float getBaseZoom() {
		return baseZoom.floatValue();
	}

	@Override
	public void zoom(float amount) {
		baseZoom = baseZoom.multiply(new BigDecimal(amount, PRECISION));
	}

	@Override
	public void setBaseZoom(float zoom) {
		this.baseZoom = new BigDecimal(zoom, PRECISION);
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
   public void focus(GameObject object) {
	   camera.position.x = object.getLeft() + object.getOffset().x + object.getWidth() / 2f;
      camera.position.y = object.getTop() + object.getOffset().y + object.getHeight() / 2f;
   }

	@Override
	public void focus() {
		focusRequested = true;
	}

	@Override
	public Camera getInternal() {
		return camera;
	}

   @Override
   public void setStickToWorldBounds(boolean enabled) {
      this.worldBoundsStickiness = enabled;
   }
   
   private void applyWorldBounds() {
      
      final WorldBounds bounds = world.getBounds();
      
      if (bounds.getWorldWidth() == 0 || bounds.getWorldHeight() == 0) {
         return;
      }
      final float camWidthScaled = camera.viewportWidth * camera.zoom;
      final float camHeightScaled = camera.viewportHeight * camera.zoom;

      final boolean worldWidthTooSmall = camWidthScaled >= bounds.getWorldWidth();
      final boolean worldHeightTooSmall = camHeightScaled >= bounds.getWorldHeight();    

      final float camLeft = camera.position.x - camWidthScaled / 2f;
      final float camBottom = camera.position.y + camHeightScaled / 2f;
      final float camRight = camera.position.x + camWidthScaled / 2f;
      final float camTop = camera.position.y - camHeightScaled / 2f;
      
      if (worldWidthTooSmall) {
         if (camera.zoom > bounds.getWorldWidth() / camWidthScaled) {
            camera.zoom = bounds.getWorldWidth() / camWidthScaled;
            camera.position.x = bounds.getWorldWidth() / 2f;
         }
         adjustedX = true;
      }
      if (worldHeightTooSmall) {
         if (camera.zoom > bounds.getWorldHeight() / camHeightScaled) {
            camera.zoom = bounds.getWorldHeight() / camHeightScaled;
            camera.position.y = bounds.getWorldHeight() / 2f;
         }
         adjustedY = true;
      }
      if (!worldWidthTooSmall) {         
         if (camLeft < 0f) {
            if (camera.zoom > baseZoom.floatValue()) {
               camera.zoom = baseZoom.floatValue();
            }
            camera.position.x = camWidthScaled / 2f;
            adjustedX = true;
         } else if (camRight > bounds.getWorldWidth()) {
            if (camera.zoom > baseZoom.floatValue()) {
               camera.zoom = baseZoom.floatValue();
            }
            camera.position.x = bounds.getWorldWidth() - camWidthScaled / 2f;
            adjustedX = true;
         }
      }
      if (!worldHeightTooSmall) {
         if (camTop < 0f) {
            if (camera.zoom > baseZoom.floatValue()) {
               camera.zoom = baseZoom.floatValue();
            }
            camera.position.y = camHeightScaled / 2f;
            adjustedY = true;
         } else if (camBottom > bounds.getWorldHeight()) {
            if (camera.zoom > baseZoom.floatValue()) {
               camera.zoom = baseZoom.floatValue();
            }
            camera.position.y = bounds.getWorldHeight() - camHeightScaled / 2f;
            adjustedY = true;
         }
      }
   }
}
