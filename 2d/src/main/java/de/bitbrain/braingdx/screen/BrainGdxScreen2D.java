package de.bitbrain.braingdx.screen;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.context.GameContext2DImpl;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.util.ArgumentFactory;

public abstract class BrainGdxScreen2D<T extends BrainGdxGame> extends AbstractScreen<T, GameContext2D> {

   public BrainGdxScreen2D(final T game) {
      super(game, new ArgumentFactory<AbstractScreen, GameContext2D>() {
         @Override
         public GameContext2D create(AbstractScreen screen) {
            return new GameContext2DImpl(screen.getViewportFactory(), new ShaderConfig(), game, screen);
         }
      });
   }
}
