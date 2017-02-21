package de.bitbrain.braingdx.apps.lighting;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;

public class LightingManagerTest extends BrainGdxGame {

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new AppGameAssetLoader();
   }

   @Override
   protected AbstractScreen<?> getInitialScreen() {
      return new LightingManagerScreen(this);
   }

}
