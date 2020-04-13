package de.bitbrain.braingdx.math;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.world.GameObject;

public class QuadTree {

   private static final int SOUTH_EAST = 0;
   private static final int SOUTH_WEST = 1;
   private static final int NORTH_WEST = 2;
   private static final int NORTH_EAST = 3;

   private int level;
   private Array<GameObject> objects;
   private Rectangle bounds;
   private QuadTree[] nodes;
   private final int maxObjects;
   private final int maxLevel;

   private final GameObject tmp = new GameObject();

   public QuadTree(int maxObjects, int maxLevel, int level, Rectangle bounds) {
      this.maxObjects = maxObjects;
      this.maxLevel = maxLevel;
      this.level = level;
      this.bounds = bounds;
      objects = new Array<GameObject>();
      nodes = new QuadTree[4];
      tmp.setType("ROOT");
   }


   public void getZones(Array<Rectangle> allZones) {
      allZones.add(bounds);
      if (nodes[0] != null) {
         nodes[0].getZones(allZones);
         nodes[1].getZones(allZones);
         nodes[2].getZones(allZones);
         nodes[3].getZones(allZones);
      }
   }

   public void clear() {
      objects.clear();
      for (int i = 0; i < nodes.length; i++) {
         if (nodes[i] != null) {
            nodes[i].clear();
            nodes[i] = null;
         }
      }
   }

   public void insert(GameObject rect) {
      if (nodes[0] != null) {
         int index = getIndex(rect);
         if (index != -1) {
            nodes[index].insert(rect);
            return;
         }
      }

      objects.add(rect);

      if (objects.size > maxObjects && level < maxLevel) {
         if (nodes[0] == null) {
            split();
         }

         int i = 0;
         while (i < objects.size) {
            int index = getIndex(objects.get(i));
            if (index != -1) {
               nodes[index].insert(objects.removeIndex(i));
            } else {
               i++;
            }
         }
      }
   }

   public Array<GameObject> retrieve(Array<GameObject> list, Rectangle area) {
      tmp.setPosition(area.getX(), area.getY());
      tmp.setDimensions(area.getWidth(), area.getHeight());

      int index = getIndex(tmp);

      if (index != -1 & nodes[0] != null) {
         nodes[index].retrieve(list, area);
      } else {
         for (QuadTree node : nodes) {
            if (node != null && fitsInside(area, node)) {
               node.retrieve(list, area);
            }
         }
      }

      list.addAll(objects);

      return list;
   }

   public Array<GameObject> retrieveFast(Array<GameObject> list, Rectangle area) {
      tmp.setPosition(area.getX(), area.getY());
      tmp.setDimensions(area.getWidth(), area.getHeight());

      int index = getIndex(tmp);

      if (index != -1 & nodes[0] != null) {
         nodes[index].retrieveFast(list, area);
      }

      //  This if(..) is configurable: only process elements in MAX_LEVEL and MAX_LEVEL-1
      if (level == maxLevel || level == maxLevel - 1) {
         list.addAll(objects);
      }

      return list;
   }

   private void split() {
      float subWidth = (bounds.getWidth() * 0.5f);
      float subHeight = (bounds.getHeight() * 0.5f);
      float x = bounds.getX();
      float y = bounds.getY();

      nodes[SOUTH_EAST] = new QuadTree(maxObjects, maxLevel, level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
      nodes[SOUTH_WEST] = new QuadTree(maxObjects, maxLevel, level + 1, new Rectangle(x, y, subWidth, subHeight));
      nodes[NORTH_WEST] = new QuadTree(maxObjects, maxLevel, level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
      nodes[NORTH_EAST] = new QuadTree(maxObjects, maxLevel, level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));

   }

   private boolean fitsInside(Rectangle area, QuadTree node) {
      return area.contains(node.bounds) || area.overlaps(node.bounds);
   }

   private int getIndex(GameObject pRect) {
      int index = -1;
      float verticalMidpoint = bounds.getX() + (bounds.getWidth() * 0.5f);
      float horizontalMidpoint = bounds.getY() + (bounds.getHeight() * 0.5f);

      boolean topQuadrant = pRect.getTop() > horizontalMidpoint;
      boolean bottomQuadrant = pRect.getTop() + pRect.getHeight() < horizontalMidpoint;

      if (pRect.getLeft() < verticalMidpoint && pRect.getLeft() + pRect.getWidth() < verticalMidpoint) {
         if (topQuadrant) {
            index = NORTH_WEST;
         } else if (bottomQuadrant) {
            index = SOUTH_WEST;
         }
      } else if (pRect.getLeft() > verticalMidpoint) {
         if (topQuadrant) {
            index = NORTH_EAST;
         } else if (bottomQuadrant) {
            index = SOUTH_EAST;
         }
      }
      return index;
   }
}