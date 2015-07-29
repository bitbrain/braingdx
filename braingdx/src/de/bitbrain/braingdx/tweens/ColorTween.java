package de.bitbrain.braingdx.tweens;

import com.badlogic.gdx.graphics.Color;

import aurelienribon.tweenengine.TweenAccessor;

public class ColorTween implements TweenAccessor<Color> {

    public static final int R = 1;
    public static final int G = 2;
    public static final int B = 3;
    public static final int A = 4;
    public static final int ALL = 5;

    @Override
    public int getValues(Color target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case R:
                returnValues[0] = target.r;
                return 1;
            case G:
                returnValues[0] = target.g;
                return 1;
            case B:
                returnValues[0] = target.b;
                return 1;
            case A:
                returnValues[0] = target.a;
                return 1;
            case ALL:
                if (returnValues.length == 3) {
                    returnValues[0] = target.r;
                    returnValues[1] = target.g;
                    returnValues[2] = target.b;
                    return 1;
                } else if (returnValues.length == 4) {
                    returnValues[0] = target.r;
                    returnValues[1] = target.g;
                    returnValues[2] = target.b;
                    returnValues[3] = target.a;
                    return 1;
                }
        }
        return 0;
    }

    @Override
    public void setValues(Color target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case R:
                target.r = newValues[0];
                break;
            case G:
                target.g = newValues[0];
                break;
            case B:
                target.b = newValues[0];
                break;
            case A:
                target.a = newValues[0];
                break;
            case ALL:
                if (newValues.length == 3) {
                    target.r = newValues[0];
                    target.g = newValues[1];
                    target.b = newValues[2];
                } else if (newValues.length == 4) {
                    target.r = newValues[0];
                    target.g = newValues[1];
                    target.b = newValues[2];
                    target.a = newValues[3];
                }
                break;
        }
    }
}
