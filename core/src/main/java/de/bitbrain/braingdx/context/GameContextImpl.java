package de.bitbrain.braingdx.context;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.audio.AudioManagerImpl;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.BehaviorManagerAdapter;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventManagerImpl;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.event.GraphicsSettingsChangeEvent;
import de.bitbrain.braingdx.graphics.postprocessing.ShaderManager;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.input.InputManager;
import de.bitbrain.braingdx.input.InputManagerImpl;
import de.bitbrain.braingdx.screens.ScreenTransitions;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.ArgumentFactory;
import de.bitbrain.braingdx.util.Resizeable;
import de.bitbrain.braingdx.util.ViewportFactory;
import de.bitbrain.braingdx.world.GameWorld;

public class GameContextImpl implements GameContext, Disposable, Resizeable {

   private final GameWorld world;
   private final BehaviorManager behaviorManager;
   private final Stage stage;
   private final TweenManager tweenManager = SharedTweenManager.getInstance();
   private final InputManagerImpl inputManager;
   private final GameEventManager eventManager;
   private final AudioManager audioManager;
   private final GameSettings settings;
   private final ShaderManager shaderManager;
   private final GameCamera gameCamera;
   private final ScreenTransitions transitions;
   private Color backgroundColor = Color.BLACK.cpy();

   public GameContextImpl(
         ShaderConfig shaderConfig,
         ViewportFactory viewportFactory,
         ArgumentFactory<GameContext, GameCamera> gameCameraFactory) {
      eventManager = new GameEventManagerImpl();
      settings = new GameSettings(eventManager);
      shaderManager = new ShaderManager(shaderConfig, eventManager, settings.getGraphics());
      world = new GameWorld();
      behaviorManager = new BehaviorManager(world);
      inputManager = new InputManagerImpl();
      stage = new Stage(viewportFactory.create(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera()));
      gameCamera = gameCameraFactory.create(this);
      audioManager = new AudioManagerImpl(//
            gameCamera,//
            tweenManager,//
            SharedAssetManager.getInstance(),//
            world,//
            behaviorManager//
      );
      transitions = ScreenTransitions.getInstance();
      wire();
   }

   @Override
   public GameWorld getGameWorld() {
      return world;
   }

   @Override
   public Stage getStage() {
      return stage;
   }

   @Override
   public TweenManager getTweenManager() {
      return tweenManager;
   }

   @Override
   public BehaviorManager getBehaviorManager() {
      return behaviorManager;
   }

   @Override
   public InputManager getInputManager() {
      return inputManager;
   }

   @Override
   public AudioManager getAudioManager() {
      return audioManager;
   }

   @Override
   public GameEventManager getEventManager() {
      return eventManager;
   }

   @Override
   public GameSettings getSettings() {
      return settings;
   }

   @Override
   public ShaderManager getShaderManager() {
      return shaderManager;
   }

   @Override
   public GameCamera getGameCamera() {
      return gameCamera;
   }

   @Override
   public ScreenTransitions getScreenTransitions() {
      return transitions;
   }

   @Override
   public void dispose() {
      world.clear();
      stage.dispose();
      inputManager.dispose();
      tweenManager.killAll();
      eventManager.clear();
   }

   public void updateAndRender(float delta) {
      inputManager.update(delta);
      behaviorManager.update(delta);
      tweenManager.update(delta);
      gameCamera.update(delta);
      stage.act(delta);
   }

   @Override
   public void resize(int width, int height) {
      gameCamera.resize(width, height);
      stage.getViewport().update(width, height, true);
      eventManager.publish(new GraphicsSettingsChangeEvent());
   }

   @Override
   public Color getBackgroundColor() {
      return backgroundColor;
   }

   @Override
   public void setBackgroundColor(Color color) {
      this.backgroundColor = color;
   }

   private void wire() {
      world.addListener(new BehaviorManagerAdapter(behaviorManager));
      inputManager.register(stage);
      Gdx.input.setInputProcessor(inputManager.getMultiplexer());
   }
}
