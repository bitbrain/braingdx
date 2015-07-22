package de.bitbrain.braingdx.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import de.bitbrain.braingdx.GameObject;

/**
 * Created by miguel on 22.07.15.
 */
public class GameObjectTween implements TweenAccessor<GameObject> {

    public static final int SCALE_X = 1;
    public static final int SCALE_Y = 2;
    public static final int POS_X = 3;
    public static final int POS_Y = 4;
    public static final int WIDTH = 5;
    public static final int HEIGHT = 6;
    public static final int ALPHA = 7;
    public static final int SCALE = 8;

    @Override
    public int getValues(GameObject target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case SCALE:
                returnValues[0] = target.getScale().x;
                return 1;
            case SCALE_X:
                returnValues[0] = target.getScale().x;
                return 1;
            case SCALE_Y:
                returnValues[0] = target.getScale().y;
                return 1;
        }
        return 0;
    }

    @Override
    public void setValues(GameObject target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case SCALE:
                target.getScale().x = newValues[0];
                target.getScale().y = newValues[0];
                break;
            case SCALE_X:
                target.getScale().x = newValues[0];
                break;
            case SCALE_Y:
                target.getScale().y = newValues[0];
                break;
        }
    }

}