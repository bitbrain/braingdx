/*******************************************************************************
 * Copyright 2012 bmanuel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.bitbrain.braingdx.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public final class ShaderLoader {

   public static String BasePath = "";
   public static boolean Pedantic = true;
   public static PathResolver PathResolver = new InternalPathResolver();
   private ShaderLoader() {
   }

   public static ShaderProgram fromFile(String vertexFileName, String fragmentFileName) {
      return ShaderLoader.fromFile(vertexFileName, fragmentFileName, "");
   }

   public static ShaderProgram fromFile(String vertexFileName, String fragmentFileName, String defines) {
      String log = "\"" + vertexFileName + "/" + fragmentFileName + "\"";
      if (defines.length() > 0) {
         log += " w/ (" + defines.replace("\n", ", ") + ")";
      }
      log += "...";
      Gdx.app.log("ShaderLoader", "Compiling " + log);
      String vpSrc = PathResolver.resolve(BasePath + vertexFileName + ".vertex").readString();
      String fpSrc = PathResolver.resolve(BasePath + fragmentFileName + ".fragment").readString();

      ShaderProgram program = ShaderLoader.fromString(vpSrc, fpSrc, vertexFileName, fragmentFileName, defines);
      return program;
   }

   public static ShaderProgram fromString(String vertex, String fragment, String vertexName, String fragmentName) {
      return ShaderLoader.fromString(vertex, fragment, vertexName, fragmentName, "");
   }

   public static ShaderProgram fromString(String vertex, String fragment, String vertexName, String fragmentName,
                                          String defines) {
      ShaderProgram.pedantic = ShaderLoader.Pedantic;
      ShaderProgram shader = new ShaderProgram(defines + "\n" + vertex, defines + "\n" + fragment);

      if (!shader.isCompiled()) {
         Gdx.app.error("ShaderLoader", shader.getLog());
         System.exit(-1);
      }

      return shader;
   }

   public static interface ShaderPathResolver {
      FileHandle load(String path);
   }
}
