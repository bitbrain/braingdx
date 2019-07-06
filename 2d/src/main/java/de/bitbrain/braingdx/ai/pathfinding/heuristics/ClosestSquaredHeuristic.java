package de.bitbrain.braingdx.ai.pathfinding.heuristics;

import de.bitbrain.braingdx.tmx.TiledMapContext;
import de.bitbrain.braingdx.world.GameObject;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile. In this case the sqrt is removed
 * and the distance squared is used instead
 *
 * @author Kevin Glass
 */
public class ClosestSquaredHeuristic implements AStarHeuristic {

   @Override
   public float getCost(TiledMapContext context, GameObject target, int x, int y, int tx, int ty) {
      float dx = tx - x;
      float dy = ty - y;

      return ((dx * dx) + (dy * dy));
   }
}
