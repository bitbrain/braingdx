package de.bitbrain.braingdx.ai.pathfinding;

import de.bitbrain.braingdx.world.GameObject;

/**
 * @author Copyright (c) 2016 Alan Boglioli
 */
public interface PathFinder {

   /**
    * Find a path from the starting location provided (sx,sy) to the target
    * location (tx,ty) avoiding blockages and attempting to honour costs
    * provided by the tile map.
    *
    * @param target the {@link GameObject} to move
    * @param sx     the x coordinate of the start location
    * @param sy     the y coordinate of the start location
    * @param tx     the x coordinate of the target location
    * @param ty     the y coordinate of the target location
    * @return The path found from start to end, or null if no path can be found.
    */
   public Path findPath(GameObject target, int tx, int ty);
}