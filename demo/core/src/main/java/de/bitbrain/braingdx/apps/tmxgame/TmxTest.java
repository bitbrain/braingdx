package de.bitbrain.braingdx.apps.tmxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
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
      Gdx.graphics.setDisplayMode(1280, 1024, false);
      return new TmxScreen(this);
   }
}
