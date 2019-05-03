package de.bitbrain.braingdx.input.controller;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import de.bitbrain.braingdx.ui.Navigateable;

/**
 * Provides controller support for a given {@link Navigateable}
 */
public class NavigateableControllerInput extends ControllerAdapter {

   private final Navigateable navigateable;

   public NavigateableControllerInput(Navigateable navigateable) {
      this.navigateable = navigateable;
   }

   @Override
   public boolean axisMoved(Controller controller, int axisIndex, float value) {
      if (axisIndex == Xbox.L_STICK_VERTICAL_AXIS && value == -1) {
         navigateable.previous();
         return true;
      }
      if (axisIndex == Xbox.L_STICK_VERTICAL_AXIS && value == 1) {
         navigateable.next();
         return true;
      }
      if (axisIndex == Xbox.R_STICK_VERTICAL_AXIS && value == -1) {
         navigateable.previous();
         return true;
      }
      if (axisIndex == Xbox.R_STICK_VERTICAL_AXIS && value == 1) {
         navigateable.next();
         return true;
      }
      return false;
   }

   @Override
   public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
      if (value == PovDirection.north || value == PovDirection.west) {
         navigateable.previous();
         return true;
      }
      if (value == PovDirection.south || value == PovDirection.east) {
         navigateable.next();
         return true;
      }
      return false;
   }

   @Override
   public boolean buttonDown(Controller controller, int buttonIndex) {
      if (buttonIndex == getSubmitButton(controller)) {
         this.navigateable.enter();
         return true;
      }
      return false;
   }

   private int getSubmitButton(Controller controller) {
      if (Xbox.isXboxController(controller)) {
         return Xbox.A;
      }
      return -1;
   }
}
