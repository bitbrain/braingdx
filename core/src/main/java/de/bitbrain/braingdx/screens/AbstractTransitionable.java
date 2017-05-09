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

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

/**
 * Abstract implementation of {@link Transitionable} which unifies tweening functionality.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0
 * @since 1.0
 */
abstract class AbstractTransitionable implements Transitionable {

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
      SharedTweenManager.getInstance().killTarget(getTarget());
      resetTarget();
      Tween tween = Tween.to(getTarget(), getTweenType(), durationInMs).target(0f).ease(TweenEquations.easeInCubic);
      if (callback != null) {
         callback.beforeTransition();
         tween.setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int arg0, BaseTween<?> arg1) {
               callback.afterTransition();
            }
         });
      }
      ;
      tween.start(SharedTweenManager.getInstance());
   }

   @Override
   public void out(final TransitionCallback callback, float durationInMs) {
      SharedTweenManager.getInstance().killTarget(getTarget());
      Tween tween = Tween.to(getTarget(), getTweenType(), durationInMs).target(1f).ease(TweenEquations.easeInCubic);
      if (callback != null) {
         callback.beforeTransition();
         tween.setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int arg0, BaseTween<?> arg1) {
               callback.afterTransition();
            }
         });
      }
      ;
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

   protected abstract void resetTarget();
   protected abstract Object getTarget();
   protected abstract int getTweenType();

}
