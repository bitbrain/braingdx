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

package de.bitbrain.braingdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

/**
 * Implementation which fades a texture.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0
 * @since 1.0
 */
public class ColorTransition implements RenderLayer, Disposable, Transitionable {

   public static interface ScreenFadeCallback {
      void afterFade();
   }

   private SpriteBatch batch = new SpriteBatch();

   private Texture texture;

   private Color color = Color.WHITE.cpy();

   private final Color fadeToColor;

   public ColorTransition(Color color) {
      this.fadeToColor = color;
   }

   public ColorTransition() {
      color.a = 0f;
      fadeToColor = Color.BLACK;
   }

   @Override
   public void beforeRender() {
      // noOp
   }

   @Override
   public void in() {
      in(null, DEFAULT_DURATION);
   }

   @Override
   public void out() {
      out(null, DEFAULT_DURATION);
   }

   @Override
   public void outIn() {
      outIn(null, DEFAULT_DURATION);
   }

   @Override
   public void in(final TransitionCallback callback, float durationInMs) {
      SharedTweenManager.getInstance().killTarget(color);
      color.a = 1f;
      Tween tween = Tween.to(color, ColorTween.A, durationInMs)
           .target(0f)
           .ease(TweenEquations.easeInCubic);
      if (callback != null) {
         callback.beforeTransition();
         tween.setCallbackTriggers(TweenCallback.COMPLETE)
              .setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int arg0, BaseTween<?> arg1) {
                  }              
           });
      };
      tween.start(SharedTweenManager.getInstance());
   }

   @Override
   public void out(final TransitionCallback callback, float durationInMs) {
      SharedTweenManager.getInstance().killTarget(color);
      Tween tween = Tween.to(color, ColorTween.A, durationInMs)
            .target(1f)
            .ease(TweenEquations.easeInCubic);
       if (callback != null) {
          callback.beforeTransition();
       tween.setCallbackTriggers(TweenCallback.COMPLETE)
            .setCallback(new TweenCallback() {
                @Override
                public void onEvent(int arg0, BaseTween<?> arg1) {
                   callback.afterTransition();
                }              
            });
       };
       tween.start(SharedTweenManager.getInstance());
   }

   @Override
   public void outIn(final TransitionCallback callback, final float durationInMs) {
      out(new TransitionCallback() {
         @Override
         public void beforeTransition() {
            if (callback != null) {
               callback.beforeTransition();
            }
         }
         @Override
         public void afterTransition() {
            in(new TransitionCallback() {
               @Override
               public void beforeTransition() {
                  // noOp
               }
               @Override
               public void afterTransition() {
                  if (callback != null) {
                     callback.afterTransition();
                  }
               }               
            }, durationInMs / 2f);
         }
      }, durationInMs / 2f);
   }

   @Override
   public void render(Batch batch, float delta) {
      if (texture == null) {
         initTexture();
      }
      this.batch.setColor(color);
      this.batch.begin();
      this.batch.draw(texture, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
      this.batch.end();
   }

   @Override
   public void dispose() {
      if (texture != null) {
         texture.dispose();
      }
   }

   private void initTexture() {
      Pixmap map = new Pixmap(2, 2, Format.RGBA8888);
      map.setColor(fadeToColor);
      map.fill();
      texture = new Texture(map);
      map.dispose();
   }

}
