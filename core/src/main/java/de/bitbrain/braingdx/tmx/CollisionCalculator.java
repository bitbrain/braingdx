package de.bitbrain.braingdx.tmx;

import java.util.HashMap;
import java.util.Map;

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
	Boolean[][] collisions = getLayerCollisions(currentLayerIndex, state);
	collisions[tileX][tileY] = collision;
    }

    public static Boolean[][] getLayerCollisions(int layerIndex, State state) {
	Map<Integer, Boolean[][]> collisionMap = state.getCollisions();
	if (collisionMap.isEmpty()) {
	    collisionMap = new HashMap<Integer, Boolean[][]>();
	    state.setCollsions(collisionMap);
	}
	Boolean[][] collisions = collisionMap.get(layerIndex);
	if (collisions == null) {
	    collisions = new Boolean[state.getMapIndexWidth()][state.getMapIndexHeight()];
	    collisionMap.put(layerIndex, collisions);
	}
	return collisions;
    }
}
