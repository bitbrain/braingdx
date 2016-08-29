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

package de.bitbrain.braingdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import de.bitbrain.braingdx.assets.loader.ParticleLoader;

/**
 * Singleton implementation of an asset manager
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class SharedAssetManager {

    private static AssetManager instance = null;

    private SharedAssetManager() {
    }

    /**
     * Provides the internal asset manager instance
     */
    public static AssetManager getInstance() {

        if (instance == null) {
            loadInternal();
        }

        return instance;
    }

    /**
     * Provides an asset
     */
    public static <T> T get(String s, Class<T> clss) {
        return getInstance().get(s, clss);
    }

    /**
     * Disposes the asset manager
     */
    public static void dispose() {
        if (instance != null) {
            getInstance().dispose();
            instance = null;
        }
    }

    public static void reload() {
        instance.dispose();
        loadInternal();
    }

    private static void loadInternal() {

        if (Gdx.files == null) {
            throw new RuntimeException("LibGDX is not initialized yet!");
        }

        if (Gdx.files.isLocalStorageAvailable()) {
            instance = new AssetManager();
            instance.setLoader(ParticleEffect.class, new ParticleLoader(new InternalFileHandleResolver()));
        }
    }
}
