package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Camera;

import de.bitbrain.braingdx.GameObject;

/**
 * Tracks a camera and make it following a target
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public interface GameCamera {

    /**
     * Updates the tracker in a frame step
     *
     * @param delta the frame delta
     */
    void update(float delta);

    /**
     * Sets a new speed value
     *
     * @param speed
     */
    void setSpeed(float speed);

    /**
     * Sets a new zoom scale factor
     *
     * @param zoomScale
     */
    void setZoomScale(float zoomScale);

    /**
     * Focuses directly on the target
     */
    void focus();

    /**
     * Sets a new target
     *
     * @param target the target
     */
    void setTarget(GameObject target);

    /**
     * Provides the internal camera
     * 
     * @return the internal camera object
     */
    Camera getInternal();

}
