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
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class AnimationRenderer implements GameObjectRenderer {

   private final AnimationSupplier<GameObject> supplier;

   private final Vector2 scale = new Vector2(1f, 1f);
   private final Vector2 offset = new Vector2();

   public AnimationRenderer(AnimationSupplier<GameObject> supplier) {
      this.supplier = supplier;
   }

   public AnimationRenderer scale(float x, float y) {
      this.scale.set(x, y);
      return this;
   }

   public Vector2 getOffset() {
      return offset;
   }

   public void setOffset(float x, float y) {
      offset.set(x, y);
   }

   @Override
   public void init() {
      // noOp
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      Animation animation = supplier.supplyFor(object);

      float width = object.getWidth() * scale.x * object.getScale().x;
      float height = object.getHeight() * scale.y * object.getScale().y;

      float x = object.getLeft() + object.getOffset().x + offset.x;
      float y = object.getTop() + object.getOffset().y + offset.y;

      if (object.getScale().x < 0) {
         x -= width;
      }
      if (object.getScale().y < 0) {
         y -= height;
      }

      animation.render(batch, x, y, width, height, delta, object.getColor());
   }
}
