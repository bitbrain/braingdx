package de.bitbrain.braingdx.tmx;

class CollisionCalculator {

    public static void updateCollision(boolean collision, float x, float y, int currentLayerIndex, State state) {
	int tileX = IndexCalculator.calculateIndex(x, state.getCellWidth());
	int tileY = IndexCalculator.calculateIndex(y, state.getCellWidth());
	state.getState(tileX, tileY, currentLayerIndex).setCollision(collision);
    }
}
