package de.bitbrain.braingdx.math;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.world.GameObject;

public class QuadTree {

   private final int maxEntities;

   private final int maxLevels;

   // Spatial bounds of this node
   private Rectangle boundary;

   // All entities in this quad
   private Array<GameObject> entities;

   private GameObject tmp = new GameObject();

   // The level of this node
   private int level;

   // Child QuadTrees
   private Array<QuadTree> childNodes;

   // Scratch variables
   private static Vector2 BOUNDS_CENTER = null;

   public QuadTree(int maxEntities, int maxLevels, int level, Rectangle boundary) {
      this.maxEntities = maxEntities;
      this.maxLevels = maxLevels;
      this.level = level;
      this.boundary = boundary;

      entities = new Array<GameObject>(true, maxEntities);

      // NW, NE, SE, SW (Clockwise)
      childNodes = new Array<QuadTree>(true, 4);

      if (BOUNDS_CENTER == null) {
         BOUNDS_CENTER = new Vector2();
      }
   }

   /**
    * Clear out all entities and null out all child nodes
    */
   public void clear() {
      // Clear the entities
      entities.clear();

      // Clear out each child node
      for (QuadTree currNode : childNodes) {
         if (currNode != null) {
            currNode.clear();
            currNode = null;
         }
      }

      // Lastly, clear out the nodes list
      childNodes.clear();
   }

   /**
    * Subdivide the node into 4 child nodes
    */
   private void subdivide() {
      float width_div2 = boundary.width / 2;
      float height_div2 = boundary.height / 2;
      float x = boundary.x;
      float y = boundary.y;

      // Create four child node which fully divide the boundary of this node
      Rectangle nwRect = new Rectangle(x, y + height_div2, width_div2, height_div2);
      childNodes.add(new QuadTree(maxEntities, maxLevels, level + 1, nwRect));

      Rectangle neRect = new Rectangle(x + width_div2, y + height_div2, width_div2, height_div2);
      childNodes.add(new QuadTree(maxEntities, maxLevels, level + 1, neRect));

      Rectangle seRect = new Rectangle(x + width_div2, y, width_div2, height_div2);
      childNodes.add(new QuadTree(maxEntities, maxLevels, level + 1, seRect));

      Rectangle swRect = new Rectangle(x, y, width_div2, height_div2);
      childNodes.add(new QuadTree(maxEntities, maxLevels, level + 1, swRect));
   }

   /**
    * Determine which node the entity belongs to. -1 means object cannot completely fit within a child node
    * and is part of the parent node
    */
   private int getIndex(GameObject entity) {
      int index = -1;
      BOUNDS_CENTER = boundary.getCenter(BOUNDS_CENTER);

      // Object can completely fit within the top quadrants
      boolean topQuadrant = entity.getTop() > BOUNDS_CENTER.y;

      // Object can completely fit within the bottom quadrants
      boolean bottomQuadrant = entity.getTop() + entity.getHeight() < BOUNDS_CENTER.y;

      // Object can completely fit within the left quadrants
      if (entity.getLeft() < BOUNDS_CENTER.x && entity.getLeft() + entity.getWidth() < BOUNDS_CENTER.x) {
         if (topQuadrant) {
            index = 0;
         } else if (bottomQuadrant) {
            index = 3;
         }
      }
      // Object can completely fit within the right quadrants
      else if (entity.getLeft() > BOUNDS_CENTER.x) {
         if (topQuadrant) {
            index = 1;
         } else if (bottomQuadrant) {
            index = 2;
         }
      }

      // If we get here, the object can not fit completely in a child node, and will be part of the parent node
      return index;
   }

   /**
    * Insert an entity into the appropriate node, subdividing if necessary.
    *
    * @param entity
    */

   public void insert(GameObject entity) {
      // If we have any child nodes, see if the entity could be contained completely inside of one
      // of them
      if (childNodes.size > 0) {
         int index = getIndex(entity);

         // If full containment is possible, recursively insert in that node.
         if (index != -1) {
            childNodes.get(index).insert(entity);

            return;
         }
      }

      // Add the entity to the list of entities for the node we are in
      entities.add(entity);

      // If we've exceeded the max number of entities for this node (And have more that we could subdivide),
      // attempt to subdivide and insert further
      if (entities.size > maxEntities && level < maxLevels) {
         // Only subdivide if we haven't
         if (childNodes.size == 0) {
            subdivide();
         }

         int i = 0;
         while (i < entities.size) {
            // Move and insert what we can into the child nodes. If it can't be fully contained in the
            // child nodes, leave it at this level.
            int index = getIndex(entities.get(i));
            if (index != -1) {
               GameObject poppedEntity = entities.removeIndex(i);
               QuadTree nodeToAddTo = childNodes.get(index);
               nodeToAddTo.insert(poppedEntity);
            } else {
               i++;
            }
         }
      }
   }

   /**
    * Return all entities that could collide with the given object
    */
   public Array<GameObject> retrieve(Array<GameObject> entitiesToReturn, Rectangle area) {
      // If we have any child nodes, see if the entity could be contained completely inside of one
      // of them
      tmp.setPosition(area.x, area.y);
      tmp.setDimensions(area.width, area.height);
      if (childNodes.size > 0) {
         int index = getIndex(tmp);

         // If full containment is possible, recurse retrieval in that node.
         if (index != -1) {
            QuadTree nodeToRetrieveFrom = childNodes.get(index);
            nodeToRetrieveFrom.retrieve(entitiesToReturn, area);
         }
      }

      // Add all the entities of the node we are in.
      entitiesToReturn.addAll(entities);

      return entitiesToReturn;
   }
}