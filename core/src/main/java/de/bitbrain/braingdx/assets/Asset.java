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

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton implementation of an asset manager
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class Asset {

   public static FileHandleResolver fileHandleResolver = new InternalFileHandleResolver();
   private static AssetManager instance = null;
   private static Map<Class<?>, String> assetFolders = new HashMap<Class<?>, String>();
   private static Map<String, Class<?>> assets = new HashMap<String, Class<?>>();

   private Asset() {
   }

   public static <T> T get(String relativePath, Class<T> assetClass) {
      AssetManager assetManager = getAssetManager();
      String path = assetFolders.containsKey(assetClass) && !assetFolders.get(assetClass).trim().isEmpty()
                  && !assetFolders.get(assetClass).equals(".")
                  && !assetFolders.get(assetClass).equals("/")
            ? assetFolders.get(assetClass) + "/" + relativePath
            : relativePath;
      return assetManager.get(path, assetClass);
   }

   /**
    * Provides the internal asset manager instance
    */
   private static AssetManager getAssetManager() {
      if (instance == null)
         loadInternal();

      return instance;
   }

   public static void load(String key, Class<?> value) {
      if (!assetFolders.containsKey(value)) {
         int lastIndex = key.lastIndexOf("/");
         if (lastIndex == -1) {
            lastIndex = key.lastIndexOf("\\");
         }
         if (lastIndex != -1) {
            String folder = key.substring(0, lastIndex);
            assetFolders.put(value, folder);
         }
      }
      AssetManager assetManager = getAssetManager();
      assetManager.load(key, value);
      assets.put(key, value);
   }

   public static void reload() {
      if (instance != null) {
         instance.dispose();
         loadInternal();
         for (Map.Entry<String, Class<?>> entry : assets.entrySet()) {
            load(entry.getKey(), entry.getValue());
         }
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

   public static void finishLoading() {
      if (instance != null) {
         instance.finishLoading();
      }
   }
}
