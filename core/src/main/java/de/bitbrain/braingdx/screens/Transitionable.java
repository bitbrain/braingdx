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

import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;

/**
 * Allows transitions.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0
 * @since 1.0
 */
public interface Transitionable extends RenderLayer {

   /**
    * The default fade duration.
    */
   float DEFAULT_DURATION = 1f;

   /**
    * Fades in with the default duration.
    */
   void in();

   /**
    * Fades out with the default duration.
    */
   void out();

   /**
    * Fades out and in with the default duration.
    */
   void outIn();

   /**
    * Fades in with the given transition callback and duration specified.
    * 
    * @param callback the duration callback.
    * @param durationInMs the duration in ms.
    */
   void in(TransitionCallback callback, float durationInMs);

   /**
    * Fades out with the given transition callback and duration specified.
    * 
    * @param callback the duration callback.
    * @param durationInMs the duration in ms.
    */
   void out(TransitionCallback callback, float durationInMs);

   /**
    * Fades out and in with the given transition callback and duration specified.
    * 
    * @param callback the duration callback.
    * @param durationInMs the duration in ms.
    */
   void outIn(TransitionCallback callback, float durationInMs);
}
