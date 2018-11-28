package de.bitbrain.braingdx.util;

/**
 * This interface provides dynamic information about an enabling state.
 *
 * @param <T> the target which should be checked to be enabled
 * @author Miguel Gonzalez Sanchez
 * @since 0.4
 */
public interface Enabler<T> {
   boolean isEnabledFor(T target);
}
