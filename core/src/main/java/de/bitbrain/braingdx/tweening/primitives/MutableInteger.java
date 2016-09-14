package de.bitbrain.braingdx.tweening.primitives;

import de.bitbrain.braingdx.tweening.TweenAccessor;

/**
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class MutableInteger extends Number implements TweenAccessor<MutableInteger> {

    private static final long serialVersionUID = 1L;
    private int value;

    public MutableInteger(int value) {
	this.value = value;
    }

    public void setValue(int value) {
	this.value = value;
    }

    @Override
    public int intValue() {
	return (int) value;
    }

    @Override
    public long longValue() {
	return (long) value;
    }

    @Override
    public float floatValue() {
	return (float) value;
    }

    @Override
    public double doubleValue() {
	return (double) value;
    }

    @Override
    public int getValues(MutableInteger target, int tweenType, float[] returnValues) {
	returnValues[0] = target.value;
	return 1;
    }

    @Override
    public void setValues(MutableInteger target, int tweenType, float[] newValues) {
	target.value = (int) newValues[0];
    }
}
