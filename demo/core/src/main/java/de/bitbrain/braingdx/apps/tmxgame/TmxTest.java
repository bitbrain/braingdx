package de.bitbrain.braingdx.apps.tmxgame;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;

public class TmxTest extends BrainGdxGame {

   @Override
   protected GameAssetLoader getAssetLoader() {
      return new TmxAssetLoader();
   }

   @Override
   protected AbstractScreen<?> getInitialScreen() {
      return new TmxScreen(this);
   }
}
