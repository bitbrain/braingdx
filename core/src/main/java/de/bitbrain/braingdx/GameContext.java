package de.bitbrain.braingdx;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.screens.ScreenTransitions;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Provides access to the current game management.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public interface GameContext {
   GameWorld getGameWorld();

   Stage getStage();

   World getBox2DWorld();

   ParticleManager getParticleManager();

   TweenManager getTweenManager();

   BehaviorManager getBehaviorManager();

   GameObjectRenderManager getRenderManager();

   RenderPipeline getRenderPipeline();

   GameCamera getGameCamera();

   LightingManager getLightingManager();

   InputMultiplexer getInput();

   TiledMapManager getTiledMapManager();

   ScreenTransitions getScreenTransitions();

   AudioManager getAudioManager();
}
