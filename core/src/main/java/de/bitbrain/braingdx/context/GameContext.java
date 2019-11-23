package de.bitbrain.braingdx.context;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.postprocessing.ShaderManager;
import de.bitbrain.braingdx.input.InputManager;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.screens.ScreenTransitions;
import de.bitbrain.braingdx.util.Resizeable;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameWorld;

public interface GameContext extends Disposable, Resizeable {

   BrainGdxGame getGame();

   AbstractScreen<?, ?> getScreen();

   GameWorld getGameWorld();

   Stage getStage();

   TweenManager getTweenManager();

   BehaviorManager getBehaviorManager();

   InputManager getInputManager();

   AudioManager getAudioManager();

   GameEventManager getEventManager();

   GameSettings getSettings();

   ShaderManager getShaderManager();

   GameCamera getGameCamera();

   ScreenTransitions getScreenTransitions();

   Color getBackgroundColor();

   void setBackgroundColor(Color color);

   void updateAndRender(float delta);

   GameObjectRenderManager getRenderManager();

   RenderPipeline getRenderPipeline();

   boolean isPaused();

   void setPaused(boolean paused);

   void addUpdateable(Updateable updateable);
}
