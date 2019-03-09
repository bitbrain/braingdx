package de.bitbrain.braingdx.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;

/**
 * Registers input events
 */
public interface InputManager {
   void register(ControllerListener controllerListener);
   void register(InputProcessor inputAdapter);
}
