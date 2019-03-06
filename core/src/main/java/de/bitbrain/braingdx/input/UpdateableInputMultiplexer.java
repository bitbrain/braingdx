package de.bitbrain.braingdx.input;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import de.bitbrain.braingdx.util.Updateable;

public class UpdateableInputMultiplexer extends InputMultiplexer implements Updateable {

   @Override
   public void update(float delta) {
      for (InputProcessor processor : getProcessors()) {
         if (processor instanceof Updateable) {
            ((Updateable) processor).update(delta);
         }
      }
   }
}
