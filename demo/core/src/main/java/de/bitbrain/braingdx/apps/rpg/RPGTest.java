package de.bitbrain.braingdx.apps.rpg;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;

public class RPGTest extends BrainGdxGame {

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new RPGAssetLoader();
   }

   @Override
   protected AbstractScreen<?> getInitialScreen() {
      return new RPGScreen(this);
   }

}
