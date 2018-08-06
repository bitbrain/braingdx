package de.bitbrain.braingdx.event;

import de.bitbrain.braingdx.world.GameObject;

/**
 * Creates {@link GameEvent} objects.
 */
public interface GameEventFactory {

   /**
    * Creates an event depending on the given event object
    * and producer object provided. The <code>eventObject</code> is the game object of the event, represented within
    * the current game world as a physical entity. Additionally, the optional <code>producerObject</code> is the
    * cause of this event.
    *
    * @param eventObject    the event object
    * @param producerObject the cause of this event to trigger
    * @return a new {@link GameEvent} object
    */
   GameEvent create(GameObject eventObject, GameObject producerObject);

   /**
    * Provides an array of objects which identify as a type for a valid event object.
    */
   Object[] identifiers();
}
