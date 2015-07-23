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

import com.badlogic.gdx.math.Vector2;

import java.security.SecureRandom;
import java.util.UUID;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.tweens.VectorTween;

/**
 * Provides screenshake behavior, implemented by the algorithm described
 * here: http://my-reality.de/2015/04/28/how-to-screenshake.html
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
class ScreenShake {

    // Interval in miliseconds between each movement
    public static final float STEP_INTERVAL = 0.05f;

    private Vector2 shake;

    // our tween manager provided by Universal Tween Engine
    private TweenManager tweenManager;

    // We use a random to select an angle at random
    private SecureRandom random = new SecureRandom(UUID.randomUUID().toString().getBytes());

    static {
        // it is important to tell Universal Tween Engine how
        // to translate the camera movement
        Tween.registerAccessor(Vector2.class, new VectorTween());
    }

    // Here we're getting our dependencies
    public ScreenShake(TweenManager tweenManager) {
        this.tweenManager = tweenManager;
        shake = new Vector2();
    }

    public Vector2 getShake() {
        return shake;
    }

    // strength is the maximum radius
    // duration is the time in miliseconds
    public void shake(float strength, final float duration) {
        // Calculate the number of steps to take until radius is 0
        final int STEPS = Math.round(duration / STEP_INTERVAL);
        // Radius reduction on each iteration
        final float STRENGTH_STEP = strength / STEPS;
        // Do not forget to kill previous animations!
        tweenManager.killTarget(shake);
        for (int step = 0; step < STEPS; ++step) {
            // Step 1: Let's find a random angle
            double angle = Math.toRadians(random.nextFloat() * 360f);
            float x = (float) Math.floor(strength * Math.cos(angle));
            float y = (float) Math.floor(strength * Math.sin(angle));

            // Step 2: ease to the calculated point. Do not forget to set
            // delay!
            Tween.to(shake, VectorTween.POS_X, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(x)
                    .ease(TweenEquations.easeInOutCubic).start(tweenManager);
            Tween.to(shake, VectorTween.POS_Y, STEP_INTERVAL).delay(step * STEP_INTERVAL).target(y)
                    .ease(TweenEquations.easeInOutCubic).start(tweenManager);

            // Step 3: reduce the radius of the screen shake circle
            strength -= STRENGTH_STEP;
        }
    }
}