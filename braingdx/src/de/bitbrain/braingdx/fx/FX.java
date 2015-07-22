package de.bitbrain.braingdx.fx;

import com.badlogic.gdx.graphics.OrthographicCamera;

import aurelienribon.tweenengine.TweenManager;

/**
 * Created by miguel on 22.07.15.
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

    public void shake(float intensity, float duration) {
        shake.shake(intensity, duration);
    }
}
