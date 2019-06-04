package de.bitbrain.braingdx.ai.pathfinding.heuristics;

import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.world.GameObject;

/**
 * A heuristic that uses the tile that is closest to the target
 * as the next best tile.
 *
 * @author Kevin Glass
 */
public class ClosestHeuristic implements AStarHeuristic {

   @Override
   public float getCost(TiledMapAPI api, GameObject target, int x, int y, int tx, int ty) {
      float dx = tx - x;
      float dy = ty - y;

      float result = (float) (Math.sqrt((dx * dx) + (dy * dy)));

      return result;
   }
}