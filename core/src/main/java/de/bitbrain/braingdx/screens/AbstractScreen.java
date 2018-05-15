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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.GameContext2DImpl;
import de.bitbrain.braingdx.graphics.pipeline.ColoredRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.util.ViewportFactory;

/**
 * Abstract base class for screens
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public abstract class AbstractScreen<T extends BrainGdxGame> implements Screen {

   private T game;
   private Color backgroundColor = Color.BLACK.cpy();
   private ColoredRenderLayer coloredRenderLayer;
   private GameContext2DImpl gameContext2D;
   
   private final ViewportFactory viewportFactory = new ViewportFactory() {

	@Override
	public Viewport create(int width, int height) {
		return getViewport(width, height);
	}
	   
   };

   public AbstractScreen(T game) {
      this.game = game;
   }

   public T getGame() {
      return game;
   }

   @Override
   public final void show() {
      coloredRenderLayer = new ColoredRenderLayer();
      gameContext2D = new GameContext2DImpl(viewportFactory, getShaderConfig());
      ScreenTransitions.init(game, gameContext2D.getRenderPipeline(), this);
      onCreate(gameContext2D);
   }

   @Override
   public final void render(float delta) {
      Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      onUpdate(delta);
      gameContext2D.updateAndRender(delta);
   }

   @Override
   public final void resize(int width, int height) {
	   gameContext2D.resize(width, height);
   }
   
   public Color getBackgroundColor() {
	   return backgroundColor;
   }

   @Override
   public void pause() {

   }

   @Override
   public void resume() {

   }

   @Override
   public void hide() {

   }

   protected abstract void onCreate(GameContext context);

   protected void onUpdate(float delta) {
	   // noOp
   }

   protected ShaderConfig getShaderConfig() {
      return new ShaderConfig();
   }

   protected Viewport getViewport(int width, int height) {
      return new ScreenViewport();
   }

   @Override
   public void dispose() {
     gameContext2D.dispose();
   }

   public void setBackgroundColor(Color color) {
      this.backgroundColor = color;
      coloredRenderLayer.setColor(color);
      gameContext2D.getRenderPipeline().put(RenderPipeIds.BACKGROUND, coloredRenderLayer);
   }
}