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