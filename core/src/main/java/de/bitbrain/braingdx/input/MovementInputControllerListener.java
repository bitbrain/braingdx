package de.bitbrain.braingdx.input;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Xbox;
import de.bitbrain.braingdx.behavior.movement.Movement;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.util.Updateable;

import java.util.HashMap;
import java.util.Map;

import static de.bitbrain.braingdx.behavior.movement.Orientation.*;
import static de.bitbrain.braingdx.input.MovementInputControllerListener.Mode.*;

/**
 * Changes a {@link Movement} depending on trigger position. This can be changed with the
 * {@link Mode} which defualts to both.
 *
 * @since 0.4.10
 */
public class MovementInputControllerListener extends ControllerAdapter implements Updateable {

   public enum Mode {
      LEFT_STICK_ONLY, RIGHT_STICK_ONLY, BOTH_STICKS
   }

   private final Movement<Orientation> movement;

   private Map<Integer, Float> axis = new HashMap<Integer, Float>();

   private Orientation currentOrientation = null;

   private float attackTolerance = 0.5f;
   private float triggerTolerance = 0.2f;
   private Mode mode = BOTH_STICKS;
   private boolean pov = true;

   public MovementInputControllerListener(Movement<Orientation> movement) {
      this.movement = movement;
   }

   public void setMode(Mode mode) {
      this.mode = mode;
   }

   public void setPovEnabled(boolean povEnabled) {
      this.pov = povEnabled;
   }

   public float getAttackTolerance() {
      return attackTolerance;
   }

   public float getTriggerTolerance() {
      return triggerTolerance;
   }

   public void setAttackTolerance(float attackTolerance) {
      this.attackTolerance = attackTolerance;
   }

   public void setTriggerTolerance(float triggerTolerance) {
      this.triggerTolerance = triggerTolerance;
   }

   @Override
   public boolean axisMoved(Controller controller, int axisIndex, float value) {
      axis.put(axisIndex, value);
      if (mode == BOTH_STICKS || mode == LEFT_STICK_ONLY) {
         if (axisIndex == Xbox.L_STICK_HORIZONTAL_AXIS && value < -attackTolerance) {
            currentOrientation = LEFT;
            return true;
         }
         if (axisIndex == Xbox.L_STICK_HORIZONTAL_AXIS && value > attackTolerance) {
            currentOrientation = RIGHT;
            return true;
         }
         if (axisIndex == Xbox.L_STICK_VERTICAL_AXIS && value < -attackTolerance) {
            currentOrientation = UP;
            return true;
         }
         if (axisIndex == Xbox.L_STICK_VERTICAL_AXIS && value > attackTolerance) {
            currentOrientation = DOWN;
            return true;
         }
      }
      if (mode == BOTH_STICKS || mode == RIGHT_STICK_ONLY) {
         if (axisIndex == Xbox.R_STICK_HORIZONTAL_AXIS && value < -attackTolerance) {
            currentOrientation = LEFT;
            return true;
         }
         if (axisIndex == Xbox.R_STICK_HORIZONTAL_AXIS && value > attackTolerance) {
            currentOrientation = RIGHT;
            return true;
         }
         if (axisIndex == Xbox.R_STICK_VERTICAL_AXIS && value < -attackTolerance) {
            currentOrientation = UP;
            return true;
         }
         if (axisIndex == Xbox.R_STICK_VERTICAL_AXIS && value > attackTolerance) {
            currentOrientation = DOWN;
            return true;
         }
      }
      return false;
   }

   @Override
   public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
      if (!pov) {
         return false;
      }
      if (value == PovDirection.north) {
         currentOrientation = UP;
         return true;
      }
      if (value == PovDirection.south) {
         currentOrientation = DOWN;
         return true;
      }
      if (value == PovDirection.west) {
         currentOrientation = LEFT;
         return true;
      }
      if (value == PovDirection.east) {
         currentOrientation = RIGHT;
         return true;
      }
      if (value == PovDirection.center) {
         currentOrientation = null;
      }
      return false;
   }

   @Override
   public void update(float delta) {

      // Left trigger
      Float leftX = axis.get(Xbox.L_STICK_HORIZONTAL_AXIS);
      Float leftY = axis.get(Xbox.L_STICK_VERTICAL_AXIS);

      if (leftX != null && leftY != null) {
         if (leftX < triggerTolerance && leftX > -triggerTolerance && leftY < triggerTolerance && leftY > -triggerTolerance) {
            currentOrientation = null;
            axis.clear();
         }
      }

      // Right trigger
      Float rightX = axis.get(Xbox.R_STICK_HORIZONTAL_AXIS);
      Float rightY = axis.get(Xbox.R_STICK_VERTICAL_AXIS);

      if (rightX != null && rightY != null) {
         if (rightX < triggerTolerance && rightX > -triggerTolerance && rightY < triggerTolerance && rightY > -triggerTolerance) {
            currentOrientation = null;
            axis.clear();
         }
      }

      if (currentOrientation != null) {
         movement.move(currentOrientation);
      }
   }
}
