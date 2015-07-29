/* brainGDX project provides utilities for libGDX
 * Copyright (C) 2015 Miguel Gonzalez
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bitbrain.braingdx;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SpriteTween;
import de.bitbrain.braingdx.tweens.VectorTween;

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
    protected abstract AbstractScreen getInitialScreen();

    private void loadAssets() {
        AssetManager assetManager = SharedAssetManager.getInstance();
        GameAssetLoader loader = getAssetLoader();
        if (loader == null) {
            throw new RuntimeException("No asset loader has been specified.");
        }
        HashMap<String, Class<?> > mapping = new HashMap<String, Class<?> >();
        loader.put(mapping);
        for (Map.Entry<String, Class<?> > entry : mapping.entrySet()) {
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
