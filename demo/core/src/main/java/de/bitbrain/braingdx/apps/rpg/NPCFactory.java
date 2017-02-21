package de.bitbrain.braingdx.apps.rpg;

import de.bitbrain.braingdx.behavior.movement.MovementController;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

public class NPCFactory {

   private final GameWorld world;
   private final int rasterSize;

   public NPCFactory(int rasterSize, GameWorld world) {
      this.rasterSize = rasterSize;
      this.world = world;
   }

   public GameObject spawn(int xMapIndex, int yMapIndex, int type, MovementController<Orientation> controller) {
      GameObject object = world.addObject();
      float x = xMapIndex * rasterSize;
      float y = yMapIndex * rasterSize;
      object.setPosition(x, y);
      object.setType(type);
      object.setDimensions(rasterSize, rasterSize * 1.3f);
      return object;
   }
}
