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
import de.bitbrain.braingdx.assets.annotations.AssetSource;

import java.lang.reflect.Field;
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
 *
 * @author Miguel Gonzalez Sanchez
 */
public class SmartAssetLoader implements GameAssetLoader {

   private final Class<?> target;

   public SmartAssetLoader(Class<?> target) {
      this.target = target;
   }

   @Override
   public void put(Map<String, Class<?>> assets) {
      // for every sub-class, check for the given type
      for (Class<?> subclass : target.getDeclaredClasses()) {
         putMembers(subclass, assets, subclass.getAnnotation(AssetSource.class));
      }
      putMembers(target, assets, target.getAnnotation(AssetSource.class));
   }

   private void putMembers(Class<?> subclass, Map<String, Class<?>> assets, AssetSource source) {
      for (Field field : subclass.getFields()) {
         if (field.getAnnotation(AssetSource.class) != null) {
            source = field.getAnnotation(AssetSource.class);
         }
         if (source == null) {
            Gdx.app.error("ERROR", "Field " + field.getName() + " does has a missing asset source. Ignoring!");
            continue;
         }
         try {
            Object path = field.get(null);
            if (path instanceof String) {
               assets.put(source.directory() + "/" + path, source.assetClass());
               Gdx.app.log("INFO", "Registering asset: path=" + source.directory() + "/" + path + ", class=" + source.assetClass().getName());
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
