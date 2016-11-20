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

package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;

class CombinedRenderPipe implements RenderPipe {

    private final RenderLayer layer;

    private boolean enabled = true;

    public CombinedRenderPipe(RenderLayer layer) {
	this.layer = layer;
    }

    @Override
    public boolean isEnabled() {
	return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
	this.enabled = enabled;
    }

    @Override
    public void addEffects(PostProcessorEffect... effects) {
	// TODO Auto-generated method stub

    }

    public void draw(Batch batch, float delta) {
	if (isEnabled()) {
	    layer.render(batch, delta);
	}
    }

    @Override
    public boolean hasShaderSupport() {
	return layer.hasShaderSupport();
    }
}
