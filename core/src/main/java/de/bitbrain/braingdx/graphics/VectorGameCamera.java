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
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.util.math.BigDecimalVector2;
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
   private final BigDecimalVector2 tmp = new BigDecimalVector2();
   private BigDecimal velocityX, velocityY;
   private GameObject target;
   private BigDecimal speed = new BigDecimal(6.2f, PRECISION);
   private BigDecimal zoomScale = new BigDecimal(0.0025f, PRECISION);
   private boolean focusRequested = false;
   private BigDecimal defaultZoom = new BigDecimal(1, PRECISION);
   private boolean worldBoundsStickiness = true;

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
         applyTrackingVelocityAndZoom(delta);
      }
      if (worldBoundsStickiness) {
         applyWorldBounds();
      }
      camera.update();
   }

   @Override
   public void resize(int width, int height) {
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
      if (target == null) {
         camera.zoom = zoom;
      }
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
      if (worldBoundsStickiness) {
         applyWorldBounds();
      }
   }

   @Override
   public void focusCentered() {
      focusRequested = true;
      if (target == null) {
         WorldBounds bounds = world.getBounds();
         camera.position.x = bounds.getWorldOffsetX() + bounds.getWorldWidth() / 2f;
         camera.position.y = bounds.getWorldOffsetY() + bounds.getWorldHeight() / 2f;
      }
      if (worldBoundsStickiness) {
         applyWorldBounds();
      }
   }

   @Override
   public Camera getInternalCamera() {
      return camera;
   }

   @Override
   public void setPosition(float x, float y) {
      this.camera.position.x = x;
      this.camera.position.y = y;
      if (worldBoundsStickiness) {
         applyWorldBounds();
      }
   }

   @Override
   public Vector3 getPosition() {
      return this.camera.position.cpy();
   }

   @Override
   public float getDistanceTo(float targetX, float targetY) {
      BigDecimalVector2 thisPosition = new BigDecimalVector2(this.camera.position.x, this.camera.position.y);
      BigDecimalVector2 targetPosition = new BigDecimalVector2(targetX, targetY);
      return targetPosition.sub(thisPosition).len().floatValue();
   }

   @Override
   public float getDistanceTo(GameObject gameObject) {
      return getDistanceTo(
            gameObject.getLeft() + gameObject.getWidth() / 2f,
            gameObject.getTop() + gameObject.getHeight() / 2f
      );
   }

   @Override
   public float getScaledCameraWidth() {
      BigDecimal cameraZoom = new BigDecimal(camera.zoom, PRECISION);
      return new BigDecimal(camera.viewportWidth, PRECISION).multiply(cameraZoom).floatValue();
   }

   @Override
   public float getScaledCameraHeight() {
      BigDecimal cameraZoom = new BigDecimal(camera.zoom, PRECISION);
      return new BigDecimal(camera.viewportHeight, PRECISION).multiply(cameraZoom).floatValue();
   }

   @Override
   public void setStickToWorldBounds(boolean enabled) {
      this.worldBoundsStickiness = enabled;
   }

   private void applyTrackingVelocityAndZoom(float delta) {
      BigDecimal preciseDelta = new BigDecimal(delta, PRECISION);

      BigDecimal targetLeft = new BigDecimal(target.getLeft() + target.getOffset().x, PRECISION);
      BigDecimal targetWidth = new BigDecimal(target.getWidth(), PRECISION);
      BigDecimal targetTop = new BigDecimal(target.getTop() + target.getOffset().y, PRECISION);
      BigDecimal targetHeight = new BigDecimal(target.getHeight(), PRECISION);

      BigDecimal cameraLeft = new BigDecimal(camera.position.x, PRECISION);
      BigDecimal cameraTop = new BigDecimal(camera.position.y, PRECISION);

      velocityX = targetLeft.add(targetWidth.divide(BigDecimal.valueOf(2.0), PRECISION)).subtract(cameraLeft);
      velocityY = targetTop.add(targetHeight.divide(BigDecimal.valueOf(2.0), PRECISION)).subtract(cameraTop);

      tmp.set(velocityX, velocityY);
      BigDecimal distance = tmp.len();
      BigDecimal overAllSpeed = distance.multiply(speed);
      BigDecimal deltaX = velocityX.multiply(overAllSpeed).multiply(preciseDelta);
      BigDecimal deltaY = velocityY.multiply(overAllSpeed).multiply(preciseDelta);

      camera.zoom = zoomScale.multiply(distance).add(defaultZoom).floatValue();
      camera.position.x = cameraLeft.add(deltaX).floatValue();
      camera.position.y = cameraTop.add(deltaY).floatValue();
   }

   private void applyWorldBounds() {
      BigDecimal worldLeft = new BigDecimal(world.getBounds().getWorldOffsetX(), PRECISION);
      BigDecimal worldTop = new BigDecimal(world.getBounds().getWorldOffsetY(), PRECISION);
      BigDecimal worldWidth = new BigDecimal(world.getBounds().getWorldWidth(), PRECISION);
      BigDecimal worldHeight = new BigDecimal(world.getBounds().getWorldHeight(), PRECISION);

      BigDecimal cameraZoom = new BigDecimal(camera.zoom, PRECISION);

      BigDecimal cameraCenterX = new BigDecimal(camera.position.x, PRECISION);
      BigDecimal cameraCenterY = new BigDecimal(camera.position.y, PRECISION);
      BigDecimal cameraWidth = new BigDecimal(camera.viewportWidth, PRECISION);
      BigDecimal cameraHeight = new BigDecimal(camera.viewportHeight, PRECISION);
      BigDecimal cameraWidthScaled = new BigDecimal(camera.viewportWidth, PRECISION).multiply(cameraZoom);
      BigDecimal cameraHeightScaled = new BigDecimal(camera.viewportHeight, PRECISION).multiply(cameraZoom);

      BigDecimal cameraLeft = cameraCenterX.subtract(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
      BigDecimal cameraTop = cameraCenterY.subtract(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
      BigDecimal cameraRight = cameraCenterX.add(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
      BigDecimal cameraBottom = cameraCenterY.add(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION));

      if (cameraWidthScaled.compareTo(worldWidth) > 0) {
         camera.zoom = worldWidth.divide(cameraWidth, PRECISION).floatValue();
         cameraZoom = new BigDecimal(camera.zoom, PRECISION);
         cameraWidthScaled = new BigDecimal(camera.viewportWidth, PRECISION).multiply(cameraZoom);
         cameraHeightScaled = new BigDecimal(camera.viewportHeight, PRECISION).multiply(cameraZoom);
         cameraRight = cameraCenterX.add(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
         cameraTop = cameraCenterY.subtract(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
         cameraLeft = cameraCenterX.subtract(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
         cameraBottom = cameraCenterY.add(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
      }
      if (cameraHeightScaled.compareTo(worldHeight) > 0) {
         float newZoom = worldHeight.divide(cameraHeight, PRECISION).floatValue();
         if (newZoom < camera.zoom) {
            camera.zoom = newZoom;
            cameraZoom = new BigDecimal(camera.zoom, PRECISION);
            cameraWidthScaled = new BigDecimal(camera.viewportWidth, PRECISION).multiply(cameraZoom);
            cameraHeightScaled = new BigDecimal(camera.viewportHeight, PRECISION).multiply(cameraZoom);
            cameraRight = cameraCenterX.add(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
            cameraTop = cameraCenterY.subtract(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
            cameraLeft = cameraCenterX.subtract(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
            cameraBottom = cameraCenterY.add(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION));
         }
      }

      // 2. adjust camera position
      if (cameraLeft.compareTo(worldLeft) < 0) {
         camera.position.x = worldLeft.add(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION)).floatValue();
      }
      if (cameraRight.compareTo(worldLeft.add(worldWidth)) > 0) {
         camera.position.x = worldLeft.add(worldWidth).subtract(cameraWidthScaled.divide(new BigDecimal(2, PRECISION), PRECISION)).floatValue();
      }
      if (cameraTop.compareTo(worldTop) < 0) {
         camera.position.y = worldTop.add(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION)).floatValue();
      }
      if (cameraBottom.compareTo(worldTop.add(worldHeight)) > 0) {
         camera.position.y = worldTop.add(worldHeight).subtract(cameraHeightScaled.divide(new BigDecimal(2, PRECISION), PRECISION)).floatValue();
      }
   }
}
