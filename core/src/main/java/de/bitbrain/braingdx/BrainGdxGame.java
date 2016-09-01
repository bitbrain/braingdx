/* Copyright 2016 Miguel Gonzalez Sanchez
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

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweening.Tween;
import de.bitbrain.braingdx.tweening.tweens.ActorTween;
import de.bitbrain.braingdx.tweening.tweens.ColorTween;
import de.bitbrain.braingdx.tweening.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweening.tweens.SpriteTween;
import de.bitbrain.braingdx.tweening.tweens.VectorTween;

/**
 * Base implementation of a brainGdx driven game
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public abstract class BrainGdxGame extends Game {

    @Override
    public final void create() {
	loadAssets();
	initTweens();
	setScreen(getInitialScreen());
    }

    protected abstract GameAssetLoader getAssetLoader();

    protected abstract AbstractScreen<?> getInitialScreen();

    private void loadAssets() {
	AssetManager assetManager = SharedAssetManager.getInstance();
	GameAssetLoader loader = getAssetLoader();
	if (loader == null) {
	    throw new RuntimeException("No asset loader has been specified.");
	}
	HashMap<String, Class<?>> mapping = new HashMap<String, Class<?>>();
	loader.put(mapping);
	for (Map.Entry<String, Class<?>> entry : mapping.entrySet()) {
	    assetManager.load(entry.getKey(), entry.getValue());
	}
	assetManager.finishLoading();
    }

    private void initTweens() {
	Tween.registerAccessor(Actor.class, new ActorTween());
	Tween.registerAccessor(Color.class, new ColorTween());
	Tween.registerAccessor(Sprite.class, new SpriteTween());
	Tween.registerAccessor(Vector2.class, new VectorTween());
	Tween.registerAccessor(GameObject.class, new GameObjectTween());
    }
}
