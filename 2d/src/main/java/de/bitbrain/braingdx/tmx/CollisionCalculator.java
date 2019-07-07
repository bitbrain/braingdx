package de.bitbrain.braingdx.tmx;

import de.bitbrain.braingdx.world.GameObject;

class CollisionCalculator {

   public static void updateCollision(PositionTranslator positionTranslator, GameObject gameObject, boolean collision, float x, float y, int currentLayerIndex, State state) {
      int tileX = positionTranslator.toIndexX(x);
      int tileY = positionTranslator.toIndexY(y);
      updateCollision(gameObject, collision, tileX, tileY, currentLayerIndex, state);
   }

   public static void updateCollision(GameObject gameObject, boolean collision, int tileX, int tileY, int currentLayerIndex, State state) {
      state.getState(tileX, tileY, currentLayerIndex).setCollision(collision);
      state.getState(tileX, tileY, currentLayerIndex).setFingerprint(
            collision ? gameObject.getId() : null
      );
   }
}
