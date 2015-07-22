package de.bitbrain.braingdx.fx;

import com.badlogic.gdx.math.Vector2;

import java.security.SecureRandom;
import java.util.UUID;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.tweens.VectorTween;

/**
 * Created by miguel on 22.07.15.
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