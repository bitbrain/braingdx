/* Copyright 2016 Miguel Gonzalez Sanchez
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

import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.particles.ParticleManager;

/**
 * Convenient for {@link ParticleRenderer} instances.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class ParticleRendererFactory {

    private final ParticleManager particleManager;

    private final BehaviorManager behaviorManager;

    public ParticleRendererFactory(ParticleManager particleManager, BehaviorManager behaviorManager) {
	this.particleManager = particleManager;
	this.behaviorManager = behaviorManager;
    }

    public ParticleRenderer create(String effectPath) {
	return new ParticleRenderer(particleManager, behaviorManager, effectPath);
    }
}
