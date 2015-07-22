package de.bitbrain.braingdx.fx;

import aurelienribon.tweenengine.TweenManager;

/**
 * Created by miguel on 22.07.15.
 */
public final class FX {

    private static final FX INSTANCE = new FX();

    private TweenManager tweenManager;

    private ScreenShake screenShake;

    private FX() { }

    public static FX getInstance() {
        return INSTANCE;
    }

    public void setTweenManager(TweenManager tweenManager) {
        this.tweenManager = tweenManager;
        screenShake = new ScreenShake(this.tweenManager);
    }
}
