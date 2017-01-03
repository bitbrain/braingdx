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

package de.bitbrain.braingdx.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.graphics.particles.ManagedParticleEffect;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renderer implementation for particles.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class ParticleRenderer extends BehaviorAdapter implements GameObjectRenderer {

    private final ParticleManager particleManager;

    private final BehaviorManager behaviorManager;

    private final String effectPath;

    private ManagedParticleEffect effect;

    private boolean attached = false;

    public ParticleRenderer(ParticleManager particleManager, BehaviorManager behaviorManager, String effectPath) {
	this.particleManager = particleManager;
	this.effectPath = effectPath;
	this.behaviorManager = behaviorManager;
    }

    @Override
    public void init() {
	effect = particleManager.create(effectPath);
    }

    @Override
    public void onAttach(GameObject source) {
	attached = true;
    }

    @Override
    public void onDetach(GameObject source) {
	particleManager.free(effect);
    }

    @Override
    public void render(GameObject object, Batch batch, float delta) {
	if (!attached) {
	    behaviorManager.apply(this, object);
	}
	effect.render(object.getLeft() + object.getOffset().x + object.getWidth() / 2f,
		object.getTop() + object.getOffset().y + object.getHeight() / 2f, batch, delta);
    }
}
