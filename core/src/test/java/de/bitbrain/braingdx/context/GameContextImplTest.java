package de.bitbrain.braingdx.context;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.screens.ColorTransition;
import de.bitbrain.braingdx.util.ArgumentFactory;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ColorTransition.class)
public class GameContextImplTest {

   private GameContextImpl impl;

   @Before
   public void setup() throws Exception {
      final SpriteBatch mockBatch = mock(SpriteBatch.class);
      whenNew(SpriteBatch.class).withNoArguments().thenReturn(mockBatch);
      final OrthographicCamera mockCamera = mock(OrthographicCamera.class);
      whenNew(OrthographicCamera.class).withNoArguments().thenReturn(mockCamera);
      Gdx.app = mock(Application.class);
      Gdx.graphics = mock(Graphics.class);
      Gdx.files = mock(Files.class);
      Gdx.input = mock(Input.class);
      Preferences mockPreferences = mock(Preferences.class);
      when(Gdx.app.getPreferences(anyString())).thenReturn(mockPreferences);
      ShaderConfig shaderConfig = new ShaderConfig();
      ArgumentFactory<GameContext, GameCamera> gameCameraFactory = new ArgumentFactory<GameContext, GameCamera>() {
         @Override
         public GameCamera create(GameContext supplier) {
            return mock(GameCamera.class);
         }
      };
      BrainGdxGame game = mock(BrainGdxGame.class);
      AbstractScreen<?, ?> screen = mock(AbstractScreen.class);
      Stage stage = mock(Stage.class);
      when(stage.getViewport()).thenReturn(mock(Viewport.class));
      when(stage.getBatch()).thenReturn(mock(Batch.class));
      impl = new GameContextImpl(
            shaderConfig,
            stage,
            mock(RenderPipeline.class),
            gameCameraFactory,
            game,
            screen,
            mock(GameObjectRenderManager.class)
      );
   }

   @Test
   public void testDelta() {
      GameWorld world = impl.getGameWorld();
      GameObject object = world.addObject();
      GameWorld.GameWorldListener listener = mock(GameWorld.GameWorldListener.class);
      world.addListener(listener);
      impl.updateAndRender(100f);
      verify(listener).onUpdate(object, 100f);
   }

   @Test
   public void testDelta_WithPause() {
      GameWorld world = impl.getGameWorld();
      impl.setPaused(true);
      GameObject object = world.addObject();
      GameWorld.GameWorldListener listener = mock(GameWorld.GameWorldListener.class);
      world.addListener(listener);
      impl.updateAndRender(100f);
      verify(listener).onUpdate(object, 0f);
   }
}