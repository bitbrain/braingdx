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

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipeline;

/**
 * Screen transition utilities.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0
 * @since 1.0
 */
public class ScreenTransitions {

   private final BrainGdxGame game;

   private final AbstractScreen<?, ?> from;

   private final ColorTransition defaultTransition = new ColorTransition();

   private final RenderPipeline renderPipeline;

   public ScreenTransitions(RenderPipeline renderPipeline, BrainGdxGame game, AbstractScreen<?, ?> from) {
      this.renderPipeline = renderPipeline;
      this.game = game;
      this.from = from;
   }

   public void in(float duration) {
      in((TransitionCallback) null, duration);
   }

   public void in() {
      in(Transitionable.DEFAULT_DURATION);
   }

   public void in(Transitionable transition) {
      in(transition, Transitionable.DEFAULT_DURATION);
   }

   public void in(Transitionable transition, float duration) {
      in(transition, null, duration);
   }

   public void in(TransitionCallback callback, float duration) {
      in(defaultTransition, callback, duration);
   }

   public void in(Transitionable transition, TransitionCallback callback, float duration) {
      if (transition instanceof RenderLayer2D) {
         renderPipeline.put(ScreenTransitions.class.getSimpleName(), (RenderLayer2D)transition);
      }
      transition.in(callback, duration);
   }

   public void out(float duration) {
      out((TransitionCallback) null, null, duration);
   }

   public void out() {
      out(Transitionable.DEFAULT_DURATION);
   }

   public void out(AbstractScreen<?, ?> to, float duration) {
      out(defaultTransition, null, to, duration);
   }

   public void out(TransitionCallback callback, AbstractScreen<?, ?> to, float duration) {
      out(defaultTransition, callback, to, duration);
   }

   public void out(TransitionCallback callback, float duration) {
      out(defaultTransition, callback, null, duration);
   }

   public void outIn(Transitionable transition, TransitionCallback callback, float duration) {
      if (transition instanceof RenderLayer2D) {
         renderPipeline.put(ScreenTransitions.class.getSimpleName(), (RenderLayer2D)transition);
      }
      defaultTransition.outIn(callback, duration);
   }

   public void outIn(TransitionCallback callback, float duration) {
      outIn(defaultTransition, callback, duration);
   }

   public void outIn(Transitionable transition, float duration) {
      outIn(transition, null, duration);
   }

   public void outIn(float duration) {
      outIn(defaultTransition, null, duration);
   }

   public void outIn() {
      outIn(defaultTransition, null, Transitionable.DEFAULT_DURATION);
   }

   public void out(Transitionable transition, final AbstractScreen<?, ?> to, float duration) {
      out(transition, null, to, duration);
   }

   public void out(Transitionable transition, final TransitionCallback callback, final AbstractScreen<?, ?> to,
                   float duration) {
      if (transition instanceof RenderLayer2D) {
         renderPipeline.put(ScreenTransitions.class.getSimpleName(), (RenderLayer2D)transition);
      }
      transition.out(new TransitionCallback() {
         @Override
         public void beforeTransition() {
            if (callback != null) {
               callback.beforeTransition();
            }
         }

         @Override
         public void afterTransition() {
            if (to != null) {
               from.dispose();
               game.setScreen(to);
            }
            if (callback != null) {
               callback.afterTransition();
            }
         }
      }, duration);
   }
}
