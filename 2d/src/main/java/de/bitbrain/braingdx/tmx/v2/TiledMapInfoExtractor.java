package de.bitbrain.braingdx.tmx.v2;

import com.badlogic.gdx.maps.MapProperties;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.world.GameObject;

public class TiledMapInfoExtractor implements GameEventRouter.GameEventInfoExtractor {

   @Override
   public boolean isSticky(GameObject object) {
      if (!object.hasAttribute(MapProperties.class)) {
         return false;
      }
      MapProperties properties = (MapProperties)object.getAttribute(MapProperties.class);
      return properties.get(Constants.STICKY, false, Boolean.class);
   }

   @Override
   public String getProducer(GameObject object) {
      if (!object.hasAttribute(MapProperties.class)) {
         return null;
      }
      MapProperties properties = (MapProperties)object.getAttribute(MapProperties.class);
      return properties.get(Constants.PRODUCER, null, String.class);
   }
}
