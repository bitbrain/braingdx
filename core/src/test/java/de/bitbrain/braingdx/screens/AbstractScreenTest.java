package de.bitbrain.braingdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.context.GameContext;
import de.bitbrain.braingdx.util.ArgumentFactory;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class AbstractScreenTest {

   @Test
   public void testRender() {
      Gdx.gl = mock(GL20.class);
      BrainGdxGame game = mock(BrainGdxGame.class);
      final GameContext context = mock(GameContext.class);
      when(context.getBackgroundColor()).thenReturn(Color.BLACK);
      final AtomicBoolean onUpdateCalled = new AtomicBoolean();
      ArgumentFactory<AbstractScreen, GameContext> contextArgumentFactory = new ArgumentFactory<AbstractScreen, GameContext>() {
         @Override
         public GameContext create(AbstractScreen supplier) {
            return context;
         }
      };
      AbstractScreen<?, ?> screen = new AbstractScreen<BrainGdxGame, GameContext>(game, contextArgumentFactory) {
         @Override
         protected void onCreate(GameContext context) {

         }

         @Override
         protected void onUpdate(float delta) {
            onUpdateCalled.set(true);
         }
      };
      screen.show();
      screen.render(10f);
      verify(context).updateAndRender(10f);
      assertTrue(onUpdateCalled.get());

   }

}