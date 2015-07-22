package de.bitbrain.braingdx.tweens;

import com.badlogic.gdx.math.Vector2;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by miguel on 22.07.15.
 */
public class VectorTween implements TweenAccessor<Vector2> {

    public static final int POS_X = 1;

    public static final int POS_Y = 2;

    @Override
    public int getValues(Vector2 target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case POS_X:
                returnValues[0] = target.x;
                return 1;
            case POS_Y:
                returnValues[0] = target.y;
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Vector2 target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case POS_X:
                target.x = newValues[0];
                break;
            case POS_Y:
                target.y = newValues[0];
                break;
        }
    }

}