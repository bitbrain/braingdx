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
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class VectorGameCamera implements GameCamera {

   private static final MathContext PRECISION = MathContext.DECIMAL128;

   private final OrthographicCamera camera;
   private final GameWorld world;
   private final Vector2 tmp = new Vector2();
   private BigDecimal velocityX, velocityY;
   private GameObject target;
   private BigDecimal speed = new BigDecimal(6.2f, PRECISION);
   private BigDecimal zoomScale = new BigDecimal(0.0025f, PRECISION);
   private boolean focusRequested = false;
   private BigDecimal defaultZoom = new BigDecimal(1, PRECISION);
   private boolean worldBoundsStickiness = true;
   private boolean adjustedX = false;
   private boolean adjustedY = false;

   public VectorGameCamera(OrthographicCamera camera, GameWorld world) {
      this.camera = camera;
      velocityX = new BigDecimal(0f);
      velocityY = new BigDecimal(0f);
      this.world = world;
   }

   @Override
   public void setTrackingTarget(GameObject target) {
      setTrackingTarget(target, true);
   }

   @Override
   public void setTrackingTarget(GameObject target, boolean focus) {
      this.target = target;
      if (focus) {
         focusCentered();
      }
   }

   @Override
   public void update(float delta) {
      if (target == null)
         return;
      if (focusRequested) {
         focusCentered(target);
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
            velocityX = left.add(width.divide(BigDecimal.valueOf(2.0))).subtract(camLeft);
         }
         if (!adjustedY) {
            velocityY = top.add(height.divide(BigDecimal.valueOf(2.0))).subtract(camTop);
         }

         tmp.set(velocityX.floatValue(), velocityY.floatValue());
         BigDecimal distance = BigDecimal.valueOf(tmp.len());
         BigDecimal overAllSpeed = distance.multiply(speed);

         BigDecimal deltaX = velocityX.multiply(overAllSpeed).multiply(preciseDelta);
         BigDecimal deltaY = velocityY.multiply(overAllSpeed).multiply(preciseDelta);
         if (!adjustedX) {
            camera.position.x = camLeft.add(deltaX).floatValue();
         }
         if (!adjustedY) {
            camera.position.y = camTop.add(deltaY).floatValue();
         }
         if (!adjustedX && !adjustedY) {
            camera.zoom = zoomScale.multiply(distance).add(defaultZoom).floatValue();
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
   public float getDefaultZoomFactor() {
      return defaultZoom.floatValue();
   }

   @Override
   public void setDefaultZoomFactor(float zoom) {
      this.defaultZoom = new BigDecimal(zoom, PRECISION);
   }

   @Override
   public void zoom(float amount) {
      defaultZoom = defaultZoom.multiply(new BigDecimal(amount, PRECISION));
   }

   @Override
   public void setTargetTrackingSpeed(float speed) {
      this.speed = new BigDecimal(Math.abs(speed), PRECISION);
   }

   @Override
   public void setZoomScalingFactor(float zoomScale) {
      this.zoomScale = new BigDecimal(zoomScale, PRECISION);
   }

   @Override
   public void focusCentered(GameObject object) {
      camera.position.x = object.getLeft() + object.getOffset().x + object.getWidth() / 2f;
      camera.position.y = object.getTop() + object.getOffset().y + object.getHeight() / 2f;
   }

   @Override
   public void focusCentered() {
      focusRequested = true;
      if (target == null) {
         WorldBounds bounds = world.getBounds();
         camera.position.x = bounds.getWorldOffsetX() + bounds.getWorldWidth() / 2f;
         camera.position.y = bounds.getWorldOffsetY() + bounds.getWorldHeight() / 2f;
      }
   }

   @Override
   public Camera getInternalCamera() {
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
            if (camera.zoom > defaultZoom.floatValue()) {
               camera.zoom = defaultZoom.floatValue();
            }
            camera.position.x = camWidthScaled / 2f;
            adjustedX = true;
         } else if (camRight > bounds.getWorldWidth()) {
            if (camera.zoom > defaultZoom.floatValue()) {
               camera.zoom = defaultZoom.floatValue();
            }
            camera.position.x = bounds.getWorldWidth() - camWidthScaled / 2f;
            adjustedX = true;
         }
      }
      if (!worldHeightTooSmall) {
         if (camTop < 0f) {
            if (camera.zoom > defaultZoom.floatValue()) {
               camera.zoom = defaultZoom.floatValue();
            }
            camera.position.y = camHeightScaled / 2f;
            adjustedY = true;
         } else if (camBottom > bounds.getWorldHeight()) {
            if (camera.zoom > defaultZoom.floatValue()) {
               camera.zoom = defaultZoom.floatValue();
            }
            camera.position.y = bounds.getWorldHeight() - camHeightScaled / 2f;
            adjustedY = true;
         }
      }
   }
}
