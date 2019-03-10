package de.bitbrain.braingdx.input.keyboard;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.bitbrain.braingdx.ui.Navigateable;

/**
 * Provides keyboard support for a given {@link Navigateable}
 */
public class NavigateableKeyboardInput extends InputAdapter {

   private final Navigateable navigateable;

   public NavigateableKeyboardInput(Navigateable navigateable) {
      this.navigateable = navigateable;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN) {
         navigateable.next();
         return true;
      }
      if (keycode == Input.Keys.W || keycode == Input.Keys.UP) {
         navigateable.previous();
         return true;
      }
      if (keycode == Input.Keys.ENTER) {
         navigateable.enter();
         return true;
      }
      return false;
   }
}
