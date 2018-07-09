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
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.tweens.ColorTween;

/**
 * Implementation which fades a texture.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0
 * @since 1.0
 */
public class ColorTransition extends AbstractTransitionable implements RenderLayer, Disposable {

   public static interface ScreenFadeCallback {
      void afterFade();
   }

   private SpriteBatch batch = new SpriteBatch();

   private Texture texture;

   private Color color = Color.WHITE.cpy();

   private Color fadeToColor;

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
   
   public void setColor(Color color) {
	   fadeToColor = color;
	   dispose();
	   initTexture();
   }

   private void initTexture() {
      Pixmap map = new Pixmap(2, 2, Format.RGBA8888);
      map.setColor(fadeToColor);
      map.fill();
      texture = new Texture(map);
      map.dispose();
   }

   @Override
   protected void resetTarget() {
      color.a = 1f;
   }

   @Override
   protected Object getTarget() {
      return color;
   }

   @Override
   protected int getTweenType() {
      return ColorTween.A;
   }

}
