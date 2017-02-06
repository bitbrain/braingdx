package de.bitbrain.braingdx.tmx;

import de.bitbrain.braingdx.world.GameObject;

class CollisionCalculator {

    public static void updateCollision(boolean collision, GameObject object, int currentLayerIndex, State state) {
	updateCollision(collision, object.getLeft(), object.getTop(), currentLayerIndex, state);
    }

    public static void updateCollision(boolean collision, float x, float y, int currentLayerIndex, State state) {
	int tileX = IndexCalculator.calculateXIndex(x, state.getMapIndexWidth());
	int tileY = IndexCalculator.calculateYIndex(y, state.getMapIndexHeight());
	updateCollision(collision, tileX, tileY, currentLayerIndex, state);
    }

    public static void updateCollision(boolean collision, int tileX, int tileY, int currentLayerIndex, State state) {
	state.getState(tileX, tileY, currentLayerIndex).setCollision(collision);
    }
}
