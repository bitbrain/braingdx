/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;
import de.bitbrain.braingdx.graphics.postprocessing.ShaderManager;
import de.bitbrain.braingdx.graphics.shader.BatchPostProcessor;
import de.bitbrain.braingdx.screens.ScreenTransitions;
import de.bitbrain.braingdx.tmx.TiledMapManager;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Provides access to the current game management.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public interface GameContext {

   GameWorld getGameWorld();

   Stage getStage();

   Stage getWorldStage();

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

   GameEventManager getEventManager();

   GameSettings getSettings();

   ShaderManager getShaderManager();

   void updateAndRender(float delta);
}
