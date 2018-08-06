package de.bitbrain.braingdx.event;

/**
 * Listens to any {@link GameEvent} and triggers it.
 *
 * @param <T> the {@link GameEvent} type to use for this very listener.
 * @author Miguel Gonzalez Sanchez
 * @since 0.2.0
 */
public interface GameEventListener<T extends GameEvent> {

   void onEvent(T event);
}
