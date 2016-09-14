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

import de.bitbrain.braingdx.tweening.TweenAccessor;
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
