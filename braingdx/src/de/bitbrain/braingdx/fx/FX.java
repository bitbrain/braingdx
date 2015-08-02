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

package de.bitbrain.braingdx.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.tweens.SpriteTween;

/**
 * Provides special effect utilities
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public final class FX {

    private static final FX INSTANCE = new FX();

    private TweenManager tweenManager;

    private ScreenShake shake;

    private Sprite flash;

    private OrthographicCamera camera;

    private FX() {
        flash = new Sprite(GraphicsFactory.createTexture(2, 2, Color.WHITE));
        flash.setAlpha(0f);
    }

    public static FX getInstance() {
        return INSTANCE;
    }

    public void begin() {
        if (camera != null && shake != null) {
            camera.translate(shake.getShake().x, shake.getShake().y);
        }
    }

    public void end() {
        if (camera != null && shake != null) {
            camera.translate(-shake.getShake().x, -shake.getShake().y);
        }
    }

    public void init(TweenManager tweenManager, OrthographicCamera camera) {
        this.tweenManager = tweenManager;
        shake = new ScreenShake(this.tweenManager);
        this.camera = camera;
    }

    public void render(Batch batch, float delta) {
        flash.setPosition(camera.position.x - (camera.zoom * camera.viewportWidth) / 2, camera.position.y
                - (camera.zoom * camera.viewportHeight) / 2);
        flash.setSize(camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
        flash.draw(batch, 1f);
    }

    public void fadeIn(float duration, TweenEquation equation) {
        flash.setAlpha(1f);
        tweenManager.killTarget(flash);
        Tween.to(flash, SpriteTween.ALPHA, duration).target(0f).ease(equation).start(tweenManager);
    }

    public void fadeOut(float duration) {
        fadeOut(duration, TweenEquations.easeInQuad);
    }

    public void fadeOut(float duration, TweenEquation equation) {
        flash.setAlpha(0f);
        tweenManager.killTarget(flash);
        Tween.to(flash, SpriteTween.ALPHA, duration).target(1f).ease(equation).start(tweenManager);
    }

    public void fadeIn(float duration) {
        fadeIn(duration, TweenEquations.easeInQuad);
    }


    /**
     * Shakes the screen by the given intensity for the given duration
     *
     * @param intensity the intensity of the shake
     * @param duration the duration of the shake
     */
    public void shake(float intensity, float duration) {
        shake.shake(intensity, duration);
    }
}
