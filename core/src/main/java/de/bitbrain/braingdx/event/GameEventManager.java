package de.bitbrain.braingdx.event;

/**
 * Manages game events. Register an {@link GameEventListener} in order to
 * listen to game events, then published by this manager. When there is no
 * {@link GameEventListener} registered for a published event, this manager
 * will not throw any exception, but will log an error.
 * 
 * @since 0.2.0
 * @author Miguel Gonzalez Sanchez
 */
public interface GameEventManager {
   
   /**
    * Registers a new listener to this manager. It is possible to register more
    * than one {@link GameEventListener} of the same type.
    * 
    * @param listener the listener to register
    * @param clazz the class of the event to listen on
    */
   <T extends GameEvent> void register(GameEventListener<T> listener, Class<T> clazz);
   
   /**
    * Unregisters an existing listener. Will log an error when the listener does not exist.
    */
   public <T extends GameEvent> void unregister(GameEventListener<T> listener, Class<T> clazz);
   
   /**
    * Publishes a new game event to all registered listeners.
    */
   <T extends GameEvent> void publish(T event);
   
   /**
    * Clears all listeners.
    */
   void clear();
   
   /**
    * Clears all listeners listening on the defined {@link GameEvent}.
    */
   <T extends GameEvent> void clear(Class<T> clazz);
}
