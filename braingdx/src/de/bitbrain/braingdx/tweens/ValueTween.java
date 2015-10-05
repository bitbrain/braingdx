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
import de.bitbrain.braingdx.util.ValueProvider;

/**
 * Tweening accessor for values
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class ValueTween implements TweenAccessor<ValueProvider> {

    public static final int VALUE = 1;

    @Override
    public int getValues(ValueProvider target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case VALUE:
                returnValues[0] = target.getValue();
                return 1;
        }
        return 0;
    }

    @Override
    public void setValues(ValueProvider target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case VALUE:
                target.setValue(newValues[0]);
                break;
        }
    }
}
