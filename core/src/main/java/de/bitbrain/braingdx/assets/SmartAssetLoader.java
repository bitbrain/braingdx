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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads assets defined by an interface. The default strategy looks as follows:
 * <p>
 * <ul>
 * <li>Textures</li>
 * <li>Sounds</li>
 * <li>Musics</li>
 * <li>Fonts</li>
 * <li>Particles</li>
 * <li>TiledMaps</li>
 * </ul>
 * In order to extend or change the list make use of the {@link SmartAssetLoaderConfiguration}.
 *
 * @author Miguel Gonzalez Sanchez
 */
public class SmartAssetLoader implements GameAssetLoader {

   private final SmartAssetLoaderConfiguration configuration;
   private final Class<?> target;
   public SmartAssetLoader(Class<?> target) {
      this(target, defaultConfiguration());
   }

   public SmartAssetLoader(Class<?> target, SmartAssetLoaderConfiguration configuration) {
      this.target = target;
      this.configuration = configuration;
   }

   public static SmartAssetLoaderConfiguration defaultConfiguration() {
      final Map<String, Class<?>> mapping = new HashMap<String, Class<?>>();
      mapping.put("Textures", Texture.class);
      mapping.put("Sounds", Sound.class);
      mapping.put("Musics", Music.class);
      mapping.put("BitmapFonts", BitmapFont.class);
      mapping.put("Fonts", FreeTypeFontGenerator.class);
      mapping.put("Particles", ParticleEffect.class);
      mapping.put("TiledMaps", TiledMap.class);
      return new SmartAssetLoaderConfiguration() {
         @Override
         public Map<String, Class<?>> getClassMapping() {
            return mapping;
         }
      };
   }

   @Override
   public void put(Map<String, Class<?>> assets) {
      // for every sub-class, check for the given type
      for (Class<?> subclass : target.getDeclaredClasses()) {
         String categoryName = subclass.getSimpleName();
         Class<?> assetClassType = configuration.getClassMapping().get(categoryName);
         if (assetClassType != null) {
            putMembers(subclass, assets, assetClassType);
         } else {
            Gdx.app.log("WARN", "Asset category " + categoryName + " not defined in SmartAssetLoaderConfiguration!");
         }
      }
   }

   private void putMembers(Class<?> subclass, Map<String, Class<?>> assets, Class<?> assetClassType) {
      for (Field field : subclass.getFields()) {
         try {
            Object path = field.get(null);
            if (path instanceof String) {
               assets.put((String) path, assetClassType);
               Gdx.app.log("INFO", "Registering asset: path=" + path + ", class=" + assetClassType.getName());
            } else {
               Gdx.app.log("WARN", "Invalid property type in '" + subclass.getName() + "::" + field.getName() + "! Only java.lang.String is allowed.");
            }
         } catch (IllegalArgumentException e) {
            Gdx.app.log("WARN", "Unable to load field value.");
         } catch (IllegalAccessException e) {
            Gdx.app.log("WARN", "Unable to load field value.");
         }
      }
   }

   /**
    * Defines how types and which types should get mapped.
    */
   public static interface SmartAssetLoaderConfiguration {
      Map<String, Class<?>> getClassMapping();
   }

}
