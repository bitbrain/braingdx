package de.bitbrain.braingdx.event;

/**
 * Listens to any {@link GameEvent} and triggers it.
 * 
 * @since 0.2.0
 * @author Miguel Gonzalez Sanchez
 * @param <T> the {@link GameEvent} type to use for this very listener.
 */
public interface GameEventListener<T extends GameEvent> {

   void onEvent(T event);
}
