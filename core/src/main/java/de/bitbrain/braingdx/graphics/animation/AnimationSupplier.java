package de.bitbrain.braingdx.graphics.animation;

public interface AnimationSupplier<T> {

    Animation supplyFor(T type);
}
