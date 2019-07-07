package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Tracks a camera and make it following a target
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public interface GameCamera {

   /**
    * Sets a new threshold to where the camera should stop tracking.
    * The distance also takes camera zoom into account.
    * Defaults to 0.
    *
    * @param distanceThreshold
    */
   void setDistanceStoppingThreshold(float distanceThreshold);

   /**
    * Sets a new speed value
    *
    * @param speed
    */
   void setTargetTrackingSpeed(float speed);

   /**
    * Sets a new speed value
    */
   void setTargetTrackingSpeed(float speedX, float speedY);

   /**
    * Sets a new zoom scale factor
    *
    * @param zoomScale
    */
   void setZoomScalingFactor(float zoomScale);

   /**
    * Enables or disables the camera to stick to the current
    * {@link GameWorld} bounds. By default this is enabled.
    */
   void setStickToWorldBounds(boolean enabled);

   /**
    * Provides the current base zoom
    *
    * @return baseZoom
    */
   float getDefaultZoomFactor();

   /**
    * Sets the base zoom
    *
    * @param zoom baseZoom
    */
   void setDefaultZoomFactor(float zoom);

   /**
    * Zooms the current baseZoom.
    *
    * @param amount the base zoom amount
    */
   void zoom(float amount);

   /**
    * Focuses directly on the target
    */
   void focusCentered();

   /**
    * When no target is defined or the given object is the target, it focuses the
    * game camera onto the given object.
    */
   void focusCentered(GameObject object);

   /**
    * Sets a new target. Focuses by default.
    *
    * @param target the target
    */
   void setTrackingTarget(GameObject target);

   /**
    * Sets a new target and determines if focusCentered or not.
    *
    * @param target the target
    * @param focus  focusCentered target on attach
    */
   void setTrackingTarget(GameObject target, boolean focus);

   /**
    * Provides the internal camera
    *
    * @return the internal camera object
    */
   Camera getInternalCamera();

   /**
    * Sets the cameras position.
    *
    * @param x
    * @param y
    */
   void setPosition(float x, float y);

   /**
    * Sets the cameras position.
    */
   Vector3 getPosition();

   /**
    * Gets the distance between the center of this camera and the target position.
    *
    * @param targetX target x
    * @param targetY target y
    * @return the distance
    */
   float getDistanceTo(float targetX, float targetY);

   /**
    * Gets the distance between the center of this camera and the center of the given game object.
    *
    * @param gameObject the game object
    * @return the distance
    */
   float getDistanceTo(GameObject gameObject);

   /**
    * Returns the scaled width of this camera
    */
   float getScaledCameraWidth();

   /**
    * Returns the scaled height of this camera
    */
   float getScaledCameraHeight();

   /**
    * Returns the currently configured tracking speed
    */
   float getTargetTrackingSpeed();

   float getTargetTrackingSpeedX();

   float getTargetTrackingSpeedY();

   /**
    * Applies screen shake to this camera.
    *
    * @param strength the strength of the effect
    * @param duration the length of the effect in seconds
    */
   void shake(float strength, float duration);

   void update(float delta);

   void resize(int width, int height);
}
