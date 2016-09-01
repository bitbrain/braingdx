/* Copyright 2016 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.tweening.tweens;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.bitbrain.braingdx.tweening.TweenAccessor;

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
