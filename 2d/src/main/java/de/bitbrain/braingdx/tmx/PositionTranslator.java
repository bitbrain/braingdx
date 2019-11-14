package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.math.Vector2;

/**
 * Translates positions depending on the current map orientation
 */
public interface PositionTranslator {
   Vector2 toWorld(float mapX, float mapY);
   Vector2 toWorld(int indexX, int indexY);
   Vector2 toMap(float worldX, float worldY);
   Vector2 toMap(int indexX, int indexY);
   int toIndexX(float worldX);
   int toIndexY(float worldY);

}