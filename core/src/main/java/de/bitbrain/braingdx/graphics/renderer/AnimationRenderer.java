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

package de.bitbrain.braingdx.graphics.renderer;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.graphics.animation.Animation;
import de.bitbrain.braingdx.graphics.animation.AnimationSupplier;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Renderer implementation for animations
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class AnimationRenderer implements GameObjectRenderer {

   private final AnimationSupplier<GameObject> supplier;

   private final Vector2 scale = new Vector2(1f, 1f);

   public AnimationRenderer(AnimationSupplier<GameObject> supplier) {
      this.supplier = supplier;
   }

   public AnimationRenderer scale(float x, float y) {
      this.scale.set(x, y);
      return this;
   }

   @Override
   public void init() {
      // noOp
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      Animation animation = supplier.supplyFor(object);

      animation.render(batch, object.getLeft() + object.getOffset().x, object.getTop() + object.getOffset().y,
            object.getWidth() * scale.x, object.getHeight() * scale.y, delta);
   }
}
