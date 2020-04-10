package de.bitbrain.braingdx.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.util.Updateable;

public class InputManagerImpl implements InputManager, Updateable, Disposable {

   private final InputMultiplexer inputMultiplexer;

   public InputManagerImpl() {
      this.inputMultiplexer = new InputMultiplexer();
   }

   public InputMultiplexer getMultiplexer() {
      return inputMultiplexer;
   }

   @Override
   public void register(ControllerListener controllerListener) {
      Controllers.addListener(controllerListener);
   }

   @Override
   public void register(InputProcessor inputAdapter) {
      inputMultiplexer.addProcessor(inputAdapter);
   }

   @Override
   public void clear() {
      inputMultiplexer.clear();
      Controllers.clearListeners();
   }

   @Override
   public void dispose() {
      Controllers.clearListeners();
   }

   @Override
   public void update(float delta) {
      for (ControllerListener listener : Controllers.getListeners()) {
         if (listener instanceof Updateable) {
            ((Updateable)listener).update(delta);
         }
      }
      for (InputProcessor processor : inputMultiplexer.getProcessors()) {
         if (processor instanceof Updateable) {
            ((Updateable) processor).update(delta);
         }
      }
   }
}
