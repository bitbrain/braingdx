package de.bitbrain.braingdx.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.*;

/**
 * An event router which manages object collisions and detects events happening in the
 * world. It then publishes events depending on the event configuration.
 * <p>
 * There are the following properties on any {@link de.bitbrain.braingdx.world.GameObject} which can be treated as an event:
 * <ul>
 * <li><b>sticky</b> - sticky events do not disappear after a trigger and will be triggered after re-entering</li>
 * <li><b>producer</b> - a custom producer type which overrides the default producer. The default producer is the game object colliding with the event object</li>
 * </ul>
 */
public class GameEventRouter extends BehaviorAdapter {

   public interface GameEventInfoExtractor {
      boolean isSticky(GameObject object);
      boolean isTriggerOnEnter(GameObject object);
      String getProducer(GameObject object);
   }

   private final GameEventManager eventManager;
   private final GameWorld gameWorld;
   private Rectangle sourceRect, targetRect;
   private GameEventFactory eventFactory;
   private Set<String> eventIds = new HashSet<String>();
   private Map<String, List<String>> enterMap = new HashMap<String, List<String>>();
   private Object[] identifiers;
   private final GameEventInfoExtractor extractor;

   public GameEventRouter(GameEventManager eventManager, GameWorld gameWorld, GameEventInfoExtractor extractor) {
      this.eventManager = eventManager;
      this.gameWorld = gameWorld;
      this.sourceRect = new Rectangle();
      this.targetRect = new Rectangle();
      this.extractor = extractor;
   }

   public void setEventFactory(GameEventFactory eventFactory) {
      this.eventFactory = eventFactory;
      if (eventFactory != null) {
         identifiers = eventFactory.identifiers();
      }
   }

   @Override
   public void update(GameObject source, GameObject target, float delta) {
      if (eventFactory == null || identifiers == null || identifiers.length < 1) {
         return;
      }
      if (!isSupported(source)) {
         return;
      }
      // Events do not collide!
      if (source.getType().equals(target.getType())) {
         return;
      }

      String producer = extractor.getProducer(source);

      if (producer != null && !producer.equals(target.getType())) {
         return;
      }

      sourceRect.set(source.getLeft(), source.getTop(), source.getWidth(), source.getHeight());
      targetRect.set(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());

      if (sourceRect.contains(targetRect) || sourceRect.overlaps(targetRect)) {
         if (extractor.isTriggerOnEnter(source)) {
            if (!enterMap.containsKey(source.getId()) || !enterMap.get(source.getId()).contains(target.getId())) {
               return;
            } else {
               enterMap.get(source.getId()).remove(target.getId());
            }
         }

         if (eventIds.contains(source.getId())) {
            // Event already consumed!
            gameWorld.remove(source);
            enterMap.remove(source.getId());
            return;
         }
         eventIds.add(source.getId());
         // Source is the event!
         GameEvent event = eventFactory.create(source, target);
         if (event != null) {
            eventManager.publish(event);
         } else {
            Gdx.app.log("WARN", "Unable to publish event for " + source + "! Not supported by EventFactory!");
         }
      } else if (extractor.isTriggerOnEnter(source) && target.hasMoved()) {
         if (!enterMap.containsKey(source.getId())) {
            enterMap.put(source.getId(), new ArrayList<String>());
         }
         List<String> objects = enterMap.get(source.getId());
         if (!objects.contains(target.getId())) {
            objects.add(target.getId());
         }
      }
      if (extractor.isSticky(source)) {
         if (eventIds.contains(source.getId())) {
            eventIds.remove(source.getId());
         }
      }

   }

   private boolean isSupported(GameObject object) {
      for (Object type : identifiers) {
         if (type.equals(object.getType())) {
            return true;
         }
      }
      return false;
   }
}
