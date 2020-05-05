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

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.VectorTween;
import de.bitbrain.braingdx.util.math.DoubleVector2;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.braingdx.world.GameWorld.WorldBounds;

import java.util.Random;

import static de.bitbrain.braingdx.util.BitUtils.haveSameSign;

/**
 * Using underlying vectors to calculate the camera tracking.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class VectorGameCamera implements GameCamera {

   // Interval in miliseconds between each movement
   public static final float STEP_INTERVAL = 0.05f;

   private final OrthographicCamera camera;
   private final GameWorld world;
   private final DoubleVector2 tmp = new DoubleVector2();
   private double velocityX, velocityY;
   private GameObject target;
   private double speedX = 0.001;
   private double speedY = 0.001;
   private double zoomScale = 0.0025f;
   private boolean focusRequested = false;
   private double defaultZoom = 1;
   private boolean worldBoundsStickiness = true;

   private final Rectangle previousWorldBounds = new Rectangle();
   private final Rectangle currentWorldBounds = new Rectangle();

   private final Vector2 shake = new Vector2(), lastShake = new Vector2();

   private int correctionX = 0;
   private int correctionY = 0;

   private final Random random = new Random();

   static {
      // it is important to tell Universal Tween Engine how
      // to translate the camera movement
      Tween.registerAccessor(Vector2.class, new VectorTween());
   }

   private float distanceThreshold = 0;

   private float zoomModeValue = 1f;
   private ZoomMode zoomMode = ZoomMode.TO_VALUE;

   public VectorGameCamera(OrthographicCamera camera, GameWorld world) {
      this.camera = camera;
      velocityX = 0;
      velocityY = 0;
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
      camera.position.x -= lastShake.x;
      camera.position.y -= lastShake.y;
      if (target != null) {
         currentWorldBounds.set(
               world.getBounds().getWorldOffsetX(),
               world.getBounds().getWorldOffsetY(),
               world.getBounds().getWorldWidth(),
               world.getBounds().getWorldHeight()
         );
         if (!currentWorldBounds.equals(previousWorldBounds)) {
            correctionX = 0;
            correctionY = 0;
         }
         if (focusRequested) {
            focusCentered(target);
            focusRequested = false;
         } else {
            applyTrackingVelocityAndZoom(delta);
         }
         if (worldBoundsStickiness) {
            applyWorldBounds();
         }
         previousWorldBounds.set(
               world.getBounds().getWorldOffsetX(),
               world.getBounds().getWorldOffsetY(),
               world.getBounds().getWorldWidth(),
               world.getBounds().getWorldHeight()
         );
      }
      camera.position.x += shake.x;
      camera.position.y += shake.y;
      camera.update();
      lastShake.set(shake);
   }

   @Override
   public float getZoomModeValue() {
      return zoomModeValue;
   }

   @Override
   public void setDistanceStoppingThreshold(float distanceThreshold) {
      this.distanceThreshold = distanceThreshold;
   }

   @Override
   public void resize(int width, int height) {
      if (target != null) {
         focusRequested = true;
      }
      camera.setToOrtho(false, width, height);
   }

   @Override
   public float getDefaultZoomFactor() {
      return (float) defaultZoom;
   }

   @Override
   public void setDefaultZoomFactor(float zoom) {
      this.defaultZoom = zoom;
      if (target == null) {
         camera.zoom = zoom;
      }
   }

   @Override
   public void zoom(float amount) {
      defaultZoom = defaultZoom * amount;
      if (target == null) {
         camera.zoom = (float) defaultZoom;
      }
   }

   @Override
   public void setZoom(float value, ZoomMode zoomMode) {
      this.zoomModeValue = value;
      this.zoomMode = zoomMode;
      setDefaultZoomFactor(calculateZoom(value, zoomMode));
   }

   @Override
   public void setZoom(float value) {
      setZoom(value, ZoomMode.TO_VALUE);
   }

   @Override
   public void setTargetTrackingSpeed(float speed) {
      setTargetTrackingSpeed(speed, speed);
   }

   @Override
   public void setTargetTrackingSpeed(float speedX, float speedY) {
      this.speedX = Math.abs(speedX);
      this.speedY = Math.abs(speedY);
   }

   @Override
   public void setZoomScalingFactor(float zoomScale) {
      this.zoomScale = zoomScale;
   }

   @Override
   public void focusCentered(GameObject object) {
      camera.position.x = object.getLeft() + object.getOffsetX() + object.getWidth() / 2f;
      camera.position.y = object.getTop() + object.getOffsetY() + object.getHeight() / 2f;
      if (worldBoundsStickiness) {
         correctionX = 0;
         correctionY = 0;
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
         correctionX = 0;
         correctionY = 0;
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
      this.camera.update(false);
   }

   @Override
   public Vector3 getPosition() {
      return this.camera.position.cpy();
   }

   @Override
   public float getDistanceTo(float targetX, float targetY) {
      Vector2 thisPosition = new Vector2(this.camera.position.x, this.camera.position.y);
      Vector2 targetPosition = new Vector2(targetX, targetY);
      return targetPosition.sub(thisPosition).len();
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
      float cameraZoom = camera.zoom;
      return camera.viewportWidth * cameraZoom;
   }

   @Override
   public float getScaledCameraHeight() {
      float cameraZoom = camera.zoom;
      return camera.viewportHeight * cameraZoom;
   }

   @Override
   public float getUnscaledCameraWidth() {
      return camera.viewportWidth;
   }

   @Override
   public float getUnscaledCameraHeight() {
      return camera.viewportHeight;
   }

   @Override
   public float getLeft() {
      return camera.position.x - getScaledCameraWidth() / 2f;
   }

   @Override
   public float getTop() {
      return camera.position.y - getScaledCameraHeight() / 2f;
   }

   @Override
   public float getTargetTrackingSpeed() {
      return getTargetTrackingSpeedX();
   }

   @Override
   public float getTargetTrackingSpeedX() {
      return (float) speedX;
   }

   @Override
   public float getTargetTrackingSpeedY() {
      return (float) speedY;
   }

   @Override
   public void shake(float strength, float duration) {
// Calculate the number of steps to take until radius is 0
      final int STEPS = Math.round(duration / STEP_INTERVAL);
      // Radius reduction on each iteration
      final float STRENGTH_STEP = strength / STEPS;
      // Do not forget to kill previous animations!
      SharedTweenManager.getInstance().killTarget(shake);
      for (int step = 0; step < STEPS; ++step) {
         // Step 1: Let's find a random angle
         double angle = Math.toRadians(random.nextFloat() * 360f);
         float x = (float) Math.floor(strength * Math.cos(angle));
         float y = (float) Math.floor(strength * Math.sin(angle));

         final int finalStep = step;

         // Step 2: ease to the calculated point. Do not forget to set
         // delay!
         Tween.to(shake, VectorTween.POS_X, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(x)
               .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int i, BaseTween<?> baseTween) {
                     if (finalStep == STEPS - 1) {
                        shake.set(0f, 0f);
                     }
                  }
               })
               .setCallbackTriggers(TweenCallback.COMPLETE)
               .ease(TweenEquations.easeInOutCubic).start(SharedTweenManager.getInstance());
         Tween.to(shake, VectorTween.POS_Y, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(y)
               .ease(TweenEquations.easeInOutCubic).start(SharedTweenManager.getInstance());

         // Step 3: reduce the radius of the screen shake circle
         strength -= STRENGTH_STEP;
      }
   }

   @Override
   public Vector2 getShake() {
      return shake;
   }

   @Override
   public void setStickToWorldBounds(boolean enabled) {
      this.worldBoundsStickiness = enabled;
   }

   private void applyTrackingVelocityAndZoom(float delta) {

      double targetLeft = target.getLeft() + target.getOffsetX();
      double targetWidth = target.getWidth();
      double targetTop = target.getTop() + target.getOffsetY();
      double targetHeight = target.getHeight();

      double cameraLeft = camera.position.x;
      double cameraTop = camera.position.y;

      velocityX = targetLeft + targetWidth / 2.0 - cameraLeft;
      velocityY = targetTop + targetHeight / 2.0 - cameraTop;

      tmp.set(velocityX, velocityY);

      double distance = tmp.len();

      if (distance < (distanceThreshold * camera.zoom)) {
         distance = 0.0;
      }

      double overAllSpeedX = distance * speedX;
      double overAllSpeedY = distance * speedY;
      double deltaX = velocityX * overAllSpeedX * delta;
      double deltaY = velocityY * overAllSpeedY * delta;

      camera.zoom = (float) (defaultZoom + zoomScale * distance);

      if (correctionX == 0 || !haveSameSign(deltaX, correctionX)) {
         camera.position.x = (float) (cameraLeft + deltaX);
         correctionX = 0;
      }
      if (correctionY == 0 || !haveSameSign(deltaY, correctionY)) {
         camera.position.y = (float) (cameraTop + deltaY);
         correctionY = 0;
      }
   }

   private void applyWorldBounds() {
      double worldLeft = world.getBounds().getWorldOffsetX();
      double worldTop = world.getBounds().getWorldOffsetY();
      double worldWidth = world.getBounds().getWorldWidth();
      double worldHeight = world.getBounds().getWorldHeight();

      double cameraZoom = camera.zoom;

      double cameraCenterX = camera.position.x;
      double cameraCenterY = camera.position.y;
      double cameraWidth = camera.viewportWidth;
      double cameraHeight = camera.viewportHeight;
      double cameraWidthScaled = camera.viewportWidth * cameraZoom;
      double cameraHeightScaled = camera.viewportHeight * cameraZoom;

      double cameraLeft = cameraCenterX - cameraWidthScaled / 2.0;
      double cameraTop = cameraCenterY - cameraHeightScaled / 2.0;
      double cameraRight = cameraCenterX + cameraWidthScaled / 2.0;
      double cameraBottom = cameraCenterY + cameraHeightScaled / 2.0;

      if (cameraWidthScaled > worldWidth) {
         camera.zoom = (float) (worldWidth / cameraWidth);
         cameraZoom = camera.zoom;
         cameraWidthScaled = camera.viewportWidth * cameraZoom;
         cameraHeightScaled = camera.viewportHeight * cameraZoom;
         cameraRight = cameraCenterX + cameraWidthScaled / 2.0;
         cameraTop = cameraCenterY - cameraHeightScaled / 2.0;
         cameraLeft = cameraCenterX - cameraWidthScaled / 2.0;
         cameraBottom = cameraCenterY + cameraHeightScaled / 2.0;
         setDefaultZoomFactor(camera.zoom);
      }
      if (cameraHeightScaled > worldHeight) {
         float newZoom = (float) (worldHeight / cameraHeight);
         if (newZoom < camera.zoom) {
            camera.zoom = newZoom;
            cameraZoom = camera.zoom;
            cameraWidthScaled = camera.viewportWidth * cameraZoom;
            cameraHeightScaled = camera.viewportHeight * cameraZoom;
            cameraRight = cameraCenterX + cameraWidthScaled / 2.0;
            cameraTop = cameraCenterY - cameraHeightScaled / 2.0;
            cameraLeft = cameraCenterX - cameraWidthScaled / 2.0;
            cameraBottom = cameraCenterY + cameraHeightScaled / 2.0;
            setDefaultZoomFactor(camera.zoom);
         }
      }

      // 2. adjust camera position
      if (cameraLeft < worldLeft) {
         camera.position.x = (float) (worldLeft + cameraWidthScaled / 2.0);
         correctionX = -1;
      } else if (cameraRight > worldLeft + worldWidth) {
         camera.position.x = (float) (worldLeft + worldWidth - cameraWidthScaled / 2.0);
         correctionX = 1;
      }
      if (cameraTop < worldTop) {
         camera.position.y = (float) (worldTop + cameraHeightScaled / 2.0);
         correctionY = -1;
      } else if (cameraBottom > worldTop + worldHeight) {
         camera.position.y = (float) (worldTop + worldHeight - cameraHeightScaled / 2.0);
         correctionY = 1;
      }
   }

   private float calculateZoom(float value, ZoomMode zoomMode) {
      if (zoomMode == ZoomMode.TO_VALUE) {
         return value;
      }
      if (zoomMode == ZoomMode.TO_WIDTH) {
         return value / camera.viewportWidth;
      } else {
         return value / camera.viewportHeight;
      }
   }
}
