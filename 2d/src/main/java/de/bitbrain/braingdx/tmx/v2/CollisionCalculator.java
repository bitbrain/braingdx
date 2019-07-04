package de.bitbrain.braingdx.tmx.v2;

import de.bitbrain.braingdx.world.GameObject;

class CollisionCalculator {

   public static void updateCollision(GameObject gameObject, boolean collision, float x, float y, int currentLayerIndex, State state) {
      int tileX = IndexCalculator.calculateIndex(x, state.getCellWidth());
      int tileY = IndexCalculator.calculateIndex(y, state.getCellWidth());
      updateCollision(gameObject, collision, tileX, tileY, currentLayerIndex, state);
   }

   public static void updateCollision(GameObject gameObject, boolean collision, int tileX, int tileY, int currentLayerIndex, State state) {
      state.getState(tileX, tileY, currentLayerIndex).setCollision(collision);
      state.getState(tileX, tileY, currentLayerIndex).setFingerprint(
            collision ? gameObject.getId() : null
      );
   }
}
