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

package de.bitbrain.braingdx;

import aurelienribon.tweenengine.Tween;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.BitmapFontBaker;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.graphics.postprocessing.effects.*;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.*;
import de.bitbrain.braingdx.util.StringRandomizer;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.braingdx.world.GameObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of a brainGdx driven game
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class BrainGdxGame extends Game {

   @Override
   public final void create() {
      loadAssets();
      initTweens();
      setScreen(getInitialScreen());
   }

   @Override
   public void dispose() {
      super.dispose();
      BitmapFontBaker.dispose();
   }

   protected abstract GameAssetLoader getAssetLoader();

   protected abstract AbstractScreen<?, ?> getInitialScreen();

   private void loadAssets() {
      final AssetManager assetManager = SharedAssetManager.getInstance();
      final GameAssetLoader loader = getAssetLoader();
      if (loader == null)
         throw new RuntimeException("No asset loader has been specified.");
      final HashMap<String, Class<?>> mapping = new HashMap<String, Class<?>>();
      loader.put(mapping);
      for (final Map.Entry<String, Class<?>> entry : mapping.entrySet()) {
         assetManager.load(entry.getKey(), entry.getValue());
      }
      assetManager.finishLoading();
   }

   private void initTweens() {
      // Normal tweens
      Tween.registerAccessor(Actor.class, new ActorTween());
      Tween.registerAccessor(Color.class, new ColorTween());
      Tween.registerAccessor(Sprite.class, new SpriteTween());
      Tween.registerAccessor(Vector2.class, new VectorTween());
      Tween.registerAccessor(GameObject.class, new GameObjectTween());
      Tween.registerAccessor(GameCamera.class, new GameCameraTween());
      Tween.registerAccessor(StringRandomizer.class, new StringRandomizerTween());
      Tween.registerAccessor(ValueProvider.class, new ValueTween());
      // Shader tweens
      Tween.registerAccessor(Bloom.class, new BloomShaderTween());
      Tween.registerAccessor(CrtMonitor.class, new CrtMonitorShaderTween());
      Tween.registerAccessor(Zoomer.class, new ZoomerShaderTween());
      Tween.registerAccessor(Vignette.class, new VignetteShaderTween());
      Tween.registerAccessor(MotionBlur.class, new MotionBlurShaderTween());
   }
}
