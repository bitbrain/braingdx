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

package de.bitbrain.braingdx.tweens;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

/**
 * Provides animation utilities
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public final class TweenUtils {

    private static TweenManager tweenManager = SharedTweenManager.getInstance();

    /**
     * Fades the source color object into the target color in the given time
     *
     * @param sourceColor the source color
     * @param targetColor the target color
     * @param time given decimal time (in seconds)
     * @param equation tween equation
     */
    public static void toColor(Color sourceColor, Color targetColor, float time, TweenEquation equation) {
        tweenManager.killTarget(sourceColor, ColorTween.R);
        tweenManager.killTarget(sourceColor, ColorTween.G);
        tweenManager.killTarget(sourceColor, ColorTween.B);
        Tween.to(sourceColor, ColorTween.R, time).ease(equation).target(targetColor.r).start(tweenManager);
        Tween.to(sourceColor, ColorTween.G, time).ease(equation).target(targetColor.g).start(tweenManager);
        Tween.to(sourceColor, ColorTween.B, time).ease(equation).target(targetColor.b).start(tweenManager);
    }

    /**
     * Fades the source color object into the target color in the given time
     *
     * @param sourceColor the source color
     * @param targetColor the target color
     * @param time given decimal time (in seconds)
     */
    public static void toColor(Color sourceColor, Color targetColor, float time) {
        toColor(sourceColor, targetColor, time, TweenEquations.easeNone);
    }

    /**
     * Fades the source color object into the target color in 1 second
     *
     * @param sourceColor the source color
     * @param targetColor the target color
     */
    public static void toColor(Color sourceColor, Color targetColor) {
        toColor(sourceColor, targetColor, 1f, TweenEquations.easeNone);
    }
}
