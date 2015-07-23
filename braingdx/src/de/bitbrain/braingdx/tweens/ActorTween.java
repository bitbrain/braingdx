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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import aurelienribon.tweenengine.TweenAccessor;

/**
 * Created by miguel on 22.07.15.
 */
public class ActorTween implements TweenAccessor<Actor> {

    public static final int ALPHA = 1;

    public static final int POPUP = 2;

    public static final int SCALE = 3;

    public static final int XY = 4;

    public static final int SIZE = 5;

    public static final int ROTATION = 6;

    @Override
    public int getValues(Actor target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case ALPHA:
                returnValues[0] = target.getColor().a;
                return 1;
            case POPUP:
                returnValues[0] = target.getY();
                return 1;
            case SCALE:
                returnValues[0] = target.getScaleX();
                returnValues[1] = target.getScaleY();
                return 1;
            case XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 1;
            case SIZE:
                returnValues[0] = target.getWidth();
                returnValues[1] = target.getHeight();
                return 1;
            case ROTATION:
                returnValues[0] = target.getRotation();
                return 1;
            default:
                return 0;
        }
    }

    @Override
    public void setValues(Actor target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case ALPHA:
                target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
                break;
            case POPUP:
                target.setPosition(target.getX(), newValues[0]);
                break;
            case ROTATION:
                target.setRotation(newValues[0]);
                break;
            case SCALE:
                if (target instanceof Label) {
                    ((Label) target).setFontScale(newValues[0]);
                }
                target.setScaleX(newValues[0]);
                target.setScaleY(newValues[1]);
                break;
            case XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            case SIZE:
                target.setWidth(newValues[0]);
                target.setHeight(newValues[1]);
                break;
        }
    }

}