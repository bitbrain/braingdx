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
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.PointLight2DTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;

public class LightingManagerImpl implements LightingManager, Disposable {

   private final RayHandler handler;
   private final OrthographicCamera camera;
   private final LightFactory lightFactory;
   private final BehaviorManager behaviorManager;
   private Color ambientLightColor = Color.WHITE.cpy();
   private int rays;

   private boolean disposed = false;

   static {
      Tween.registerAccessor(PointLight.class, new PointLight2DTween());
   }

   public LightingManagerImpl(RayHandler rayHandler, BehaviorManager behaviorManager, OrthographicCamera camera) {
      this(rayHandler, camera, behaviorManager, new LightFactory() {

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

   public LightingManagerImpl(RayHandler rayHandler, OrthographicCamera camera, BehaviorManager behaviorManager, LightFactory lightFactory) {
      this.handler = rayHandler;
      this.behaviorManager = behaviorManager;
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
   public PointLight createPointLight(Vector2 pos, float distance, Color color) {
      return createPointLight(pos.x, pos.y, distance, color);
   }

   @Override
   public PointLight createPointLight(float x, float y, float distance, Color color) {
      return lightFactory.newPointLight(handler, rays, color, distance, x, y);
   }

   @Override
   public PointLight createPointLight(float distance, Color color) {
      return createPointLight(0f, 0f, distance, color);
   }

   @Override
   public DirectionalLight createDirectionalLight(Color color, float degree) {
      return lightFactory.newDirectionalLight(handler, rays, color, degree);
   }

   @Override
   public ChainLight createChainLight(float distance, int direction, Color color) {
      return createChainLight(distance, direction, color);
   }

   @Override
   public ChainLight createChainLight(float distance, int direction, Color color, float... chain) {
      return lightFactory.newChainLight(handler, direction, color, distance, direction, chain);
   }

   @Override
   public ConeLight createConeLight(float distance, float directionDegree, float coneDegree, Color color) {
      return createConeLight(0f, 0f, distance, directionDegree, coneDegree, color);
   }

   @Override
   public ConeLight createConeLight(float x, float y, float distance, float directionDegree, float coneDegree,
                                 Color color) {
      return lightFactory.newConeLight(handler, rays, color, distance, x, y, directionDegree, coneDegree);
   }

   @Override
   public ConeLight createConeLight(Vector2 pos, float distance, float directionDegree, float coneDegree, Color color) {
      return createConeLight(pos.x, pos.y, distance, directionDegree, coneDegree, color);
   }

   @Override
   public void destroyLight(final Light light) {
      Gdx.app.postRunnable(new Runnable() {
         @Override
         public void run() {
            if (light != null) {
               light.remove();
            }
         }
      });
   }

   @Override
   public void clear() {
      handler.removeAll();
   }

   @Override
   public void attach(Light light, GameObject object, boolean centered) {
      behaviorManager.apply(new LightBehavior(light, centered), object);
   }

   @Override
   public void attach(Light light, GameObject object) {
      attach(light, object, 0f, 0f);
   }

   @Override
   public void attach(Light light, GameObject object, float offsetX, float offsetY) {
      behaviorManager.apply(new LightBehavior(light, offsetX, offsetY), object);
   }

   public void render() {
      if (disposed) {
         return;
      }
      handler.renderOnly();
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

   public void beforeRender() {
      handler.setAmbientLight(ambientLightColor);
      handler.setCombinedMatrix(camera);
      handler.update();
      handler.prepareRender();
   }

   public interface LightFactory {
      PointLight newPointLight(RayHandler handler, int rays, Color color, float distance, float x, float y);

      DirectionalLight newDirectionalLight(RayHandler handler, int rays, Color color, float degree);

      ChainLight newChainLight(RayHandler handler, int rays, Color color, float distance, int direction,
                               float... chain);

      ConeLight newConeLight(RayHandler handler, int rays, Color color, float distance, float x, float y,
                             float directionDegree, float coneDegree);
   }

   private class LightBehavior extends BehaviorAdapter {

      private final Light light;

      private final float offsetX, offsetY;

      private boolean centered = false;

      LightBehavior(Light light, float offsetX, float offsetY) {
         this.light = light;
         this.offsetX = offsetX;
         this.offsetY = offsetY;
      }

      LightBehavior(Light light, boolean centered) {
         this.light = light;
         this.offsetX = 0f;
         this.offsetY = 0f;
         this.centered = centered;
      }

      @Override
      public void update(GameObject source, float delta) {
         super.update(source, delta);
         if (centered) {
            light.setPosition(source.getLeft() + source.getOffsetX() + source.getWidth() / 2f,
                  source.getTop() + source.getOffsetY() + source.getHeight() / 2f);
         } else {
            light.setPosition(source.getLeft() + source.getOffsetX() + offsetX,
                  source.getTop() + source.getOffsetY() + offsetY);
         }
      }

      @Override
      public void onDetach(GameObject source) {
         this.light.remove(true);
      }
   }
}