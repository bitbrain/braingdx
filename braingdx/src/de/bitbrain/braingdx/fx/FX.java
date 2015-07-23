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

import com.badlogic.gdx.graphics.OrthographicCamera;

import aurelienribon.tweenengine.TweenManager;

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

    private OrthographicCamera camera;

    private FX() { }

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
