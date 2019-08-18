package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.math.Vector2;

public class OrthogonalPositionTranslator implements PositionTranslator {

   private final State state;

   public OrthogonalPositionTranslator(State state) {
      this.state = state;
   }

   @Override
   public float toWorldX(int indexX) {
      return (float) Math.floor(indexX * state.getCellWidth());
   }

   @Override
   public float toWorldY(int indexY) {
      return (float) Math.floor(indexY * state.getCellHeight());
   }

   @Override
   public float toWorldX(float mapX) {
      return mapX;
   }

   @Override
   public float toWorldY(float mapY) {
      return mapY;
   }

   @Override
   public Vector2 toWorld(float x, float y) {
      return new Vector2(x, y);
   }

   @Override
   public float toMapX(float worldX) {
      return worldX;
   }

   @Override
   public float toMapY(float worldY) {
      return worldY;
   }

   @Override
   public int toIndexX(float worldX) {
      return calculateIndex(worldX, state.getCellWidth());
   }

   @Override
   public int toIndexY(float worldY) {
      return calculateIndex(worldY, state.getCellHeight());
   }

   private static int calculateIndex(float value, float cellSize) {
      return (int) Math.round(Math.floor(value / cellSize));
   }
}
