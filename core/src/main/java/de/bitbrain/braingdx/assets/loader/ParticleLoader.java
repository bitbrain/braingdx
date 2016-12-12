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

package de.bitbrain.braingdx.assets.loader;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.utils.Array;

/**
 * Loader implementation for Particle effects
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class ParticleLoader extends SynchronousAssetLoader<ParticleEffect, ParticleLoader.ParticleParameter> {

    public ParticleLoader(FileHandleResolver resolver) {
	super(resolver);
    }

    static public class ParticleParameter extends AssetLoaderParameters<ParticleEffect> {
	// do nothing here
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, ParticleParameter parameter) {
	return null;
    }

    @Override
    public ParticleEffect load(AssetManager assetManager, String fileName, FileHandle file,
	    ParticleParameter parameter) {
	final ParticleEffect effect = new ParticleEffect();
	effect.load(file, resolve(""));
	return effect;
    }
}
