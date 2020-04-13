package de.bitbrain.braingdx.event;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class GameEventRouterTest {

   private GameEventRouter gameEventRouter;
   private GameEventManager gameEventManager;
   private GameWorld gameWorld;
   private MockEventListener eventListener;
   private MockInfoExtractor mockInfoExtractor;

   @Before
   public void beforeTest() {
      Gdx.app = mock(Application.class);
      mockInfoExtractor = new MockInfoExtractor();
      gameWorld = new GameWorld();
      gameEventManager = new GameEventManagerImpl();
      eventListener = new MockEventListener();
      gameEventRouter = new GameEventRouter(gameEventManager, gameWorld, mockInfoExtractor);
      gameEventRouter.setEventFactory(new MockEventFactory());
      gameEventManager.register(eventListener, MockEvent.class);
   }

   @Test
   public void testSkipFiringEvent_UnknownProducer() {
      GameObject producer = gameWorld.addObject();
      producer.setType("unknown-producer");
      GameObject event = gameWorld.addObject();
      event.setType("mockevent");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).isEmpty();
   }

   @Test
   public void testSkipFiringEvent_UnknownEventType() {
      GameObject producer = gameWorld.addObject();
      producer.setType("producer");
      GameObject event = gameWorld.addObject();
      event.setType("unknown-event");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).isEmpty();
   }

   @Test
   public void testSkipFiringEvent_NoCollision() {
      GameObject producer = gameWorld.addObject();
      producer.setType("producer");
      GameObject event = gameWorld.addObject();
      event.setType("mockevent");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      event.setPosition(16, 16);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).hasSize(0);
   }

   @Test
   public void testFireEventOnce() {
      GameObject producer = gameWorld.addObject();
      producer.setType("producer");
      GameObject event = gameWorld.addObject();
      event.setType("mockevent");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).hasSize(1);
      assertThat(eventListener.getEvents().get(0).getEventObject()).isEqualTo(event);
      assertThat(eventListener.getEvents().get(0).getProducerObject()).isEqualTo(producer);
   }

   @Test
   public void testFireEventMultiple_OnSticky() {
      mockInfoExtractor.setSticky(true);
      GameObject producer = gameWorld.addObject();
      producer.setType("producer");
      GameObject event = gameWorld.addObject();
      event.setType("mockevent");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).hasSize(2);
      assertThat(eventListener.getEvents().get(0).getEventObject()).isEqualTo(event);
      assertThat(eventListener.getEvents().get(0).getProducerObject()).isEqualTo(producer);
   }

   @Test
   public void testFireEventOnce_OnSticky_EnterOnly() {
      mockInfoExtractor.setSticky(true);
      mockInfoExtractor.setEnterOnly(true);
      GameObject producer = gameWorld.addObject();
      producer.setType("producer");
      GameObject event = gameWorld.addObject();
      event.setType("mockevent");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).isEmpty();

      producer.setPosition(16, 16);
      producer.setPosition(17, 17);
      gameEventRouter.update(event, producer, 0f);
      producer.setPosition(15, 15);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).hasSize(1);
      assertThat(eventListener.getEvents().get(0).getEventObject()).isEqualTo(event);
      assertThat(eventListener.getEvents().get(0).getProducerObject()).isEqualTo(producer);
   }

   @Test
   public void testFireEventOnce_EnterOnly() {
      mockInfoExtractor.setSticky(false);
      mockInfoExtractor.setEnterOnly(true);
      GameObject producer = gameWorld.addObject();
      producer.setType("producer");
      GameObject event = gameWorld.addObject();
      event.setType("mockevent");
      producer.setDimensions(16, 16);
      event.setDimensions(16, 16);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).isEmpty();

      producer.setPosition(16, 16);
      producer.setPosition(17, 17);
      gameEventRouter.update(event, producer, 0f);
      producer.setPosition(15, 15);
      gameEventRouter.update(event, producer, 0f);
      gameEventRouter.update(event, producer, 0f);
      assertThat(eventListener.getEvents()).hasSize(1);
      assertThat(eventListener.getEvents().get(0).getEventObject()).isEqualTo(event);
      assertThat(eventListener.getEvents().get(0).getProducerObject()).isEqualTo(producer);
   }

   private class MockInfoExtractor implements GameEventRouter.GameEventInfoExtractor {

      private boolean sticky;
      private boolean enterOnly;

      public void setSticky(boolean sticky) {
         this.sticky = sticky;
      }

      public void setEnterOnly(boolean enterOnly) {
         this.enterOnly = enterOnly;
      }

      @Override
      public boolean isSticky(GameObject object) {
         return sticky;
      }

      @Override
      public boolean isTriggerOnEnter(GameObject object) {
         return enterOnly;
      }

      @Override
      public String getProducer(GameObject object) {
         return "producer";
      }
   }

   private class MockEvent implements GameEvent {
      private final GameObject eventObject;
      private final GameObject producerObject;

      private MockEvent(GameObject eventObject, GameObject producerObject) {
         this.eventObject = eventObject;
         this.producerObject = producerObject;
      }

      public GameObject getEventObject() {
         return eventObject;
      }

      public GameObject getProducerObject() {
         return producerObject;
      }
   }

   private class MockEventFactory implements GameEventFactory {

      @Override
      public GameEvent create(GameObject eventObject, GameObject producerObject) {
         return new MockEvent(eventObject, producerObject);
      }

      @Override
      public Object[] identifiers() {
         return new Object[]{"mockevent"};
      }
   }

   private class MockEventListener implements GameEventListener<MockEvent> {

      private final List<MockEvent> events = new ArrayList<MockEvent>();

      @Override
      public void onEvent(MockEvent event) {
         events.add(event);
      }

      public List<MockEvent> getEvents() {
         return events;
      }
   }



}