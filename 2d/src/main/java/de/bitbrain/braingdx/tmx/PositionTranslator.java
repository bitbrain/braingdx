package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.math.Vector2;

/**
 * Translates positions depending on the current map orientation
 */
public interface PositionTranslator {

   float toWorldX(int indexX);

   float toWorldY(int indexY);

   float toWorldX(float mapX);

   float toWorldY(float mapY);

   Vector2 toWorld(float x, float y);

   float toMapX(float worldX);

   float toMapY(float worldY);

   int toIndexX(float worldX);

   int toIndexY(float worldY);

}