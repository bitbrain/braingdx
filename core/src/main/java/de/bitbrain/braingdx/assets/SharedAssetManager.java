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

package de.bitbrain.braingdx.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Singleton implementation of an asset manager
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class SharedAssetManager extends AssetManager {

   public static FileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
   private static AssetManager instance = null;

   private SharedAssetManager() {
   }

   /**
    * Provides the internal asset manager instance
    */
   public static AssetManager getInstance() {

      if (instance == null)
         loadInternal();

      return instance;
   }

   public static void reload() {
      if (instance != null) {
         instance.dispose();
         loadInternal();
      }
   }

   private static void loadInternal() {
      if (Gdx.files == null)
         throw new RuntimeException("LibGDX is not initialized yet!");

      instance = new AssetManager();
      instance.setLoader(TiledMap.class, new TmxMapLoader(fileHandleResolver));
      instance.setLoader(FreeTypeFontGenerator.class,
            new FreeTypeFontGeneratorLoader(fileHandleResolver));
   }
}
