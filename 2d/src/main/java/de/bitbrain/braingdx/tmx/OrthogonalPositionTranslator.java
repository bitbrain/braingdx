package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.math.Vector2;

public class OrthogonalPositionTranslator implements PositionTranslator {

   private final State state;
   private final Vector2 tmp = new Vector2();

   public OrthogonalPositionTranslator(State state) {
      this.state = state;
   }

   @Override
   public Vector2 toWorld(float x, float y) {
      return new Vector2(x, y);
   }

   @Override
   public Vector2 toWorld(int indexX, int indexY) {
      return tmp.set(indexX * state.getCellWidth(), indexY * state.getCellHeight());
   }

   @Override
   public Vector2 toMap(float worldX, float worldY) {
      return tmp.set(worldX, worldY);
   }

   @Override
   public Vector2 toMap(int indexX, int indexY) {
      return tmp.set(indexX * state.getCellWidth(), indexY * state.getCellHeight());
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