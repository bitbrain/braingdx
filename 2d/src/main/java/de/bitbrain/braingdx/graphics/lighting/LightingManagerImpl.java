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

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import box2dLight.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

import java.util.HashMap;
import java.util.Map;

public class LightingManagerImpl implements LightingManager, Disposable {

   private final RayHandler handler;
   private final Map<String, PointLight> pointLights = new HashMap<String, PointLight>();
   private final Map<String, DirectionalLight> dirLights = new HashMap<String, DirectionalLight>();
   private final Map<String, ChainLight> chainLights = new HashMap<String, ChainLight>();
   private final Map<String, ConeLight> coneLights = new HashMap<String, ConeLight>();
   private final OrthographicCamera camera;
   private final LightFactory lightFactory;
   private Color ambientLightColor = Color.WHITE.cpy();
   private int rays;

   private boolean disposed = false;

   public LightingManagerImpl(RayHandler rayHandler, OrthographicCamera camera) {
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

   public LightingManagerImpl(RayHandler rayHandler, OrthographicCamera camera, LightFactory lightFactory) {
      this.handler = rayHandler;
      this.camera = camera;
      setConfig(new LightingConfig());
      setAmbientLight(Color.WHITE.cpy());
      this.lightFactory = lightFactory;
   }

   @Override
   public void setConfig(LightingConfig lightingConfig) {
      this.handler.setShadows(lightingConfig.shadows);
      this.handler.setBlur(lightingConfig.blur);
      this.handler.setCulling(lightingConfig.culling);
      RayHandler.setGammaCorrection(lightingConfig.gammaCorrection);
      RayHandler.useDiffuseLight(lightingConfig.diffuseLighting);
      this.rays = lightingConfig.rays;
   }

   @Override
   public void setAmbientLight(Color ambientLightColor) {
      this.ambientLightColor = ambientLightColor.cpy();
   }

   /**
    * Sets a new ambient light with a fading transition.
    */
   @Override
   public void setAmbientLight(Color color, float interval, TweenEquation equation) {
      SharedTweenManager.getInstance().killTarget(ambientLightColor);
      Tween.to(ambientLightColor, ColorTween.R, interval)
            .target(color.r)
            .ease(equation)
            .start(SharedTweenManager.getInstance());
      Tween.to(ambientLightColor, ColorTween.G, interval)
            .target(color.g)
            .ease(equation)
            .start(SharedTweenManager.getInstance());
      Tween.to(ambientLightColor, ColorTween.B, interval)
            .target(color.b)
            .ease(equation)
            .start(SharedTweenManager.getInstance());
      Tween.to(ambientLightColor, ColorTween.A, interval)
            .target(color.a)
            .ease(equation)
            .start(SharedTweenManager.getInstance());
   }

   @Override
   public PointLight addPointLight(String id, Vector2 pos, float distance, Color color) {
      return addPointLight(id, pos.x, pos.y, distance, color);
   }

   @Override
   public PointLight addPointLight(String id, float x, float y, float distance, Color color) {
      PointLight light = lightFactory.newPointLight(handler, rays, color, distance, x, y);
      pointLights.put(id, light);
      return light;
   }

   @Override
   public DirectionalLight addDirectionalLight(String id, Color color, float degree) {
      DirectionalLight light = lightFactory.newDirectionalLight(handler, rays, color, degree);
      dirLights.put(id, light);
      return light;
   }

   @Override
   public ChainLight addChainLight(String id, float distance, int direction, Color color) {
      return addChainLight(id, distance, direction, color);
   }

   @Override
   public ChainLight addChainLight(String id, float distance, int direction, Color color, float... chain) {
      ChainLight light = lightFactory.newChainLight(handler, direction, color, distance, direction, chain);
      chainLights.put(id, light);
      return light;
   }

   @Override
   public ConeLight addConeLight(String id, float x, float y, float distance, float directionDegree, float coneDegree,
                                 Color color) {
      ConeLight light = lightFactory.newConeLight(handler, rays, color, distance, x, y, directionDegree, coneDegree);
      coneLights.put(id, light);
      return light;
   }

   @Override
   public void removePointLight(final String id) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            PointLight light = pointLights.remove(id);
            if (light != null) {
               light.remove();
            }
         }
      });
   }

   @Override
   public void removeDirectionalLight(final String id) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            DirectionalLight light = dirLights.remove(id);
            if (light != null) {
               light.remove();
            }
         }
      });
   }

   @Override
   public void removeChainLight(final String id) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            ChainLight light = chainLights.remove(id);
            if (light != null) {
               light.remove();
            }
         }
      });
   }

   @Override
   public void removeConeLight(final String id) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            ConeLight light = coneLights.remove(id);
            if (light != null) {
               light.remove();
            }
         }
      });
   }

   @Override
   public void clear() {
      pointLights.clear();
      dirLights.clear();
      chainLights.clear();
      coneLights.clear();
      handler.removeAll();
   }

   public void update() {

   }

   void render() {
      if (disposed) {
         return;
      }
      handler.setAmbientLight(ambientLightColor);
      handler.setCombinedMatrix(camera);
      handler.prepareRender();
      handler.updateAndRender();
   }

   public void resize(int width, int height) {
      handler.resizeFBO(width, height);
   }

   @Override
   public void dispose() {
      if (!disposed) {
         handler.dispose();
         disposed = true;
      }
   }

   public interface LightFactory {
      PointLight newPointLight(RayHandler handler, int rays, Color color, float distance, float x, float y);

      DirectionalLight newDirectionalLight(RayHandler handler, int rays, Color color, float degree);

      ChainLight newChainLight(RayHandler handler, int rays, Color color, float distance, int direction,
                               float... chain);

      ConeLight newConeLight(RayHandler handler, int rays, Color color, float distance, float x, float y,
                             float directionDegree, float coneDegree);
   }
}