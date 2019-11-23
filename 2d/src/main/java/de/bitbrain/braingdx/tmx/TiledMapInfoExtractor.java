package de.bitbrain.braingdx.tmx;

import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.world.GameObject;

public class TiledMapInfoExtractor implements GameEventRouter.GameEventInfoExtractor {

   @Override
   public boolean isSticky(GameObject object) {
      return object.getOrSetAttribute(Constants.STICKY, false);
   }

   @Override
   public String getProducer(GameObject object) {
      if (!object.hasAttribute(Constants.PRODUCER)) {
         return null;
      }
      return object.getOrSetAttribute(Constants.PRODUCER, null);
   }
}
