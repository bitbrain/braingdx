package de.bitbrain.braingdx.screen;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext2D;
import de.bitbrain.braingdx.context.GameContext2DImpl;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.util.ArgumentFactory;

public abstract class AbstractBrainGdxScreen2D<T extends BrainGdxGame, C extends GameContext2D> extends AbstractScreen<T, C> {

   public AbstractBrainGdxScreen2D(final T game) {
      super(game, new ArgumentFactory<AbstractScreen<T, C>, C>() {
         @Override
         public C create(AbstractScreen<T, C> screen) {
            return (C) new GameContext2DImpl(screen.getViewportFactory(), new ShaderConfig(), game, screen);
         }
      });
   }

   public AbstractBrainGdxScreen2D(final T game, ArgumentFactory<AbstractScreen<T, C>, C> argumentFactory) {
      super(game, argumentFactory);
   }
}
