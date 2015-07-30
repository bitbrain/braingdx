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

import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Tween facility for sprites
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class SpriteTween implements TweenAccessor<Sprite> {

    public static final int BOUNCE = 1;
    public static final int ALPHA = 2;
    public static final int ROTATION = 3;
    public static final int SCALE = 4;
    public static final int COLOR = 5;
    public static final int COLOR_R = 6;
    public static final int COLOR_G = 7;
    public static final int COLOR_B = 8;

    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case BOUNCE:
                returnValues[0] = target.getY();
                return 1;
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            case SCALE:
                returnValues[0] = target.getScaleX();
                return 1;
            case COLOR:
                if (returnValues.length == 3) {
                    returnValues[0] = target.getColor().r;
                    returnValues[1] = target.getColor().g;
                    returnValues[2] = target.getColor().b;
                    return 1;
                }
            case COLOR_R:
                returnValues[0] = target.getColor().r;
                return 1;
            case COLOR_G:
                returnValues[0] = target.getColor().g;
                return 1;
            case COLOR_B:
                returnValues[0] = target.getColor().b;
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case BOUNCE:
                target.setY(newValues[0]);
                break;
            case ALPHA:
                target.setAlpha(newValues[0]);
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case SCALE:
                target.setScale(newValues[0]);
                break;
            case COLOR:
                if (newValues.length == 3) {
                    target.setColor(newValues[0], newValues[1], newValues[2], target.getColor().a);
                }
                break;
            case COLOR_R:
                target.setColor(newValues[0], target.getColor().g, target.getColor().b, target.getColor().a);
                break;
            case COLOR_G:
                target.setColor(target.getColor().r, newValues[0], target.getColor().b, target.getColor().a);
                break;
            case COLOR_B:
                target.setColor(target.getColor().r, target.getColor().g, newValues[0], target.getColor().a);
                break;
        }
    }

}