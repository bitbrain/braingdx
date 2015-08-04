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

import aurelienribon.tweenengine.TweenAccessor;
import de.bitbrain.braingdx.GameObject;

/**
 * Tween facility for game objects
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class GameObjectTween implements TweenAccessor<GameObject> {

    public static final int SCALE_X = 100;
    public static final int SCALE_Y = 102;
    public static final int POS_X = 103;
    public static final int POS_Y = 104;
    public static final int SCALE = 105;
    public static final int ALPHA = ColorTween.A;
    public static final int R = ColorTween.R;
    public static final int G = ColorTween.G;
    public static final int B = ColorTween.B;

    private ColorTween colorTween = new ColorTween();

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
            case POS_X:
                returnValues[0] = target.getLeft();
                return 1;
            case POS_Y:
                returnValues[0] = target.getTop();
                return 1;
            case ALPHA: case R:
            case G: case B:
                return colorTween.getValues(target.getColor(), tweenType, returnValues);
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
            case POS_X:
                target.setPosition(newValues[0], target.getTop());
                break;
            case POS_Y:
                target.setPosition(target.getLeft(), newValues[0]);
                break;
            case ALPHA: case R:
            case G: case B:
                colorTween.setValues(target.getColor(), tweenType, newValues);
                break;
        }
    }

}