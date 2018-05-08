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
package de.bitbrain.braingdx.graphics.lighting;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Manages box2d lights internally and stores them in memory.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class LightingManager {

   private static final int DEFAULT_RAYS = 80;

   public static class LightingConfig {
      boolean shadows = true;
      boolean diffuseLighting = true;
      boolean blur = true;
      boolean culling = true;
      boolean gammaCorrection = true;
      int rays = DEFAULT_RAYS;

      public LightingConfig shadows(boolean enabled) {
         this.shadows = enabled;
         return this;
      }

      public LightingConfig diffuseLighting(boolean enabled) {
         this.diffuseLighting = enabled;
         return this;
      }

      public LightingConfig blur(boolean enabled) {
         this.blur = enabled;
         return this;
      }

      public LightingConfig culling(boolean enabled) {
         this.culling = enabled;
         return this;
      }

      public LightingConfig gammaCorrection(boolean enabled) {
         this.gammaCorrection = enabled;
         return this;
      }

      public LightingConfig rays(int rays) {
         this.rays = rays;
         return this;
      }

   }

   public static interface LightFactory {
      PointLight newPointLight(RayHandler handler, int rays, Color color, float distance, float x, float y);
      DirectionalLight newDirectionalLight(RayHandler handler, int rays, Color color, float degree);
      ChainLight newChainLight(RayHandler handler, int rays, Color color, float distance, int direction,
            float... chain);
      ConeLight newConeLight(RayHandler handler, int rays, Color color, float distance, float x, float y,
            float directionDegree, float coneDegree);
   }

   private final RayHandler handler;

   private final Map<String, PointLight> pointLights = new HashMap<String, PointLight>();

   private final Map<String, DirectionalLight> dirLights = new HashMap<String, DirectionalLight>();

   private final Map<String, ChainLight> chainLights = new HashMap<String, ChainLight>();

   private final Map<String, ConeLight> coneLights = new HashMap<String, ConeLight>();

   private final OrthographicCamera camera;

   private int rays;

   private final LightFactory lightFactory;

   public LightingManager(RayHandler rayHandler, OrthographicCamera camera) {
      this(rayHandler, camera, new LightFactory() {

         @Override
         public PointLight newPointLight(RayHandler handler, int rays, Color color, float distance, float x, float y) {
            return new PointLight(handler, rays, color, distance, x, y);
         }

         @Override
         public DirectionalLight newDirectionalLight(RayHandler handler, int rays, Color color, float degree) {
            return new DirectionalLight(handler, rays, color, degree);
         }

         @Override
         public ChainLight newChainLight(RayHandler handler, int rays, Color color, float distance, int direction,
               float... chain) {
            return new ChainLight(handler, rays, color, distance, direction, chain);
         }

         @Override
         public ConeLight newConeLight(RayHandler handler, int rays, Color color, float distance, float x, float y,
               float directionDegree, float coneDegree) {
            return new ConeLight(handler, rays, color, distance, x, y, directionDegree, coneDegree);
         }

      });
   }

   public LightingManager(RayHandler rayHandler, OrthographicCamera camera, LightFactory lightFactory) {
      this.handler = rayHandler;
      this.camera = camera;
      setConfig(new LightingConfig());
      setAmbientLight(Color.WHITE);
      this.lightFactory = lightFactory;
   }

   public void setConfig(LightingConfig lightingConfig) {
      this.handler.setShadows(lightingConfig.shadows);
      this.handler.setBlur(lightingConfig.blur);
      this.handler.setCulling(lightingConfig.culling);
      RayHandler.setGammaCorrection(lightingConfig.gammaCorrection);
      RayHandler.useDiffuseLight(lightingConfig.diffuseLighting);
      this.rays = lightingConfig.rays;
   }

   public void setAmbientLight(Color color) {
      handler.setAmbientLight(color);
   }

   public PointLight addPointLight(String id, Vector2 pos, float distance, Color color) {
      return addPointLight(id, pos.x, pos.y, distance, color);
   }

   public PointLight addPointLight(String id, float x, float y, float distance, Color color) {
      PointLight light = lightFactory.newPointLight(handler, rays, color, distance, x, y);
      pointLights.put(id, light);
      return light;
   }

   public DirectionalLight addDirectionalLight(String id, Color color, float degree) {
      DirectionalLight light = lightFactory.newDirectionalLight(handler, rays, color, degree);
      dirLights.put(id, light);
      return light;
   }

   public ChainLight addChainLight(String id, float distance, int direction, Color color) {
      return addChainLight(id, distance, direction, color);
   }

   public ChainLight addChainLight(String id, float distance, int direction, Color color, float... chain) {
      ChainLight light = lightFactory.newChainLight(handler, direction, color, distance, direction, chain);
      chainLights.put(id, light);
      return light;
   }

   public ConeLight addConeLight(String id, float x, float y, float distance, float directionDegree, float coneDegree,
         Color color) {
      ConeLight light = lightFactory.newConeLight(handler, rays, color, distance, x, y, directionDegree, coneDegree);
      coneLights.put(id, light);
      return light;
   }

   public boolean removePointLight(String id) {
      PointLight light = pointLights.remove(id);
      if (light != null) {
         light.remove();
         return true;
      }
      return false;
   }

   public boolean removeDirectionalLight(String id) {
      DirectionalLight light = dirLights.remove(id);
      if (light != null) {
         light.remove();
         return true;
      }
      return false;
   }

   public boolean removeChainLight(String id) {
      ChainLight light = chainLights.remove(id);
      if (light != null) {
         light.remove();
         return true;
      }
      return false;
   }

   public boolean removeConeLight(String id) {
      ConeLight light = coneLights.remove(id);
      if (light != null) {
         light.remove();
         return true;
      }
      return false;
   }

   public void clear() {
      pointLights.clear();
      dirLights.clear();
      chainLights.clear();
      coneLights.clear();
      handler.removeAll();
   }

   void render(Batch batch, float delta) {
      handler.renderOnly();
   }

   void beforeRender() {
      handler.setCombinedMatrix(camera);
      handler.update();
      handler.prepareRender();
   }

   void resize(int width, int height) {
      handler.resizeFBO(width, height);
   }
}