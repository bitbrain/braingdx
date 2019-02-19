package de.bitbrain.braingdx.world;

import com.badlogic.gdx.math.Vector2;

public final class GameObjectUtils {

   private static final Vector2 vector = new Vector2();

   public static float distanceBetween(GameObject a, GameObject b) {
      vector.x = b.getLeft() - a.getLeft();
      vector.y = b.getTop() - a.getTop();
      return vector.len();
   }
}
