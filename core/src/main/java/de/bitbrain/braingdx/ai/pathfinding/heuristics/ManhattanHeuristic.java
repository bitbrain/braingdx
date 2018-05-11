package de.bitbrain.braingdx.ai.pathfinding.heuristics;

import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;

/**
 * A heuristic that drives the search based on the Manhattan distance
 * between the current location and the target
 *
 * @author Kevin Glass
 */
public class ManhattanHeuristic implements AStarHeuristic {
    /**
     * The minimum movement cost from any one square to the next
     */
    private int minimumCost;

    /**
     * Create a new heuristic
     *
     * @param minimumCost The minimum movement cost from any one square to the next
     */
    public ManhattanHeuristic(int minimumCost) {
        this.minimumCost = minimumCost;
    }

   @Override
   public float getCost(TiledMapAPI api, GameObject target, int x, int y, int tx, int ty) {
      return minimumCost * (Math.abs(x - tx) + Math.abs(y - ty));
   }
}
