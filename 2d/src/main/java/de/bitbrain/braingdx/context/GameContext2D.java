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

package de.bitbrain.braingdx.context;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerImpl;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.physics.PhysicsManager;
import de.bitbrain.braingdx.tmx.TiledMapManager;

/**
 * Provides access to the current game management.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public interface GameContext2D extends GameContext {

   Stage getWorldStage();

   World getBox2DWorld();

   ParticleManager getParticleManager();

   LightingManagerImpl getLightingManager();

   TiledMapManager getTiledMapManager();

   PhysicsManager getPhysicsManager();
}
