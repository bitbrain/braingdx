package de.bitbrain.braingdx.event;

import de.bitbrain.braingdx.util.GdxUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class GameEventManagerTest {

   private GameEventManager impl;
   private ConcreteGameEvent event;
   @Mock
   private GameEventListener<ConcreteGameEvent> listener;

   @Before
   public void beforeTest() {
      impl = new GameEventManagerImpl();
      GdxUtils.mockApplicationContext();
      event = new ConcreteGameEvent();
   }

   @Test
   public void testPublishEvent() {
      impl.register(listener, ConcreteGameEvent.class);
      impl.publish(event);
      inOrder(listener)
            .verify(listener, times(1))
            .onEvent(event);
   }

   @Test
   public void testUnregisterListener() {
      impl.register(listener, ConcreteGameEvent.class);
      impl.publish(event);
      impl.unregister(listener, ConcreteGameEvent.class);
      impl.publish(event);
      inOrder(listener)
            .verify(listener, times(1))
            .onEvent(event);
   }

   @Test
   public void testPublishEvent_MissingListenerNoExceptionThrown() {
      impl.publish(event);
   }

   private static class ConcreteGameEvent implements GameEvent {

   }
}
