package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.math.Vector2;

public class IsometricPositionTranslator implements PositionTranslator {

   private final State state;
   private final Vector2 xAxis, yAxis;

   public IsometricPositionTranslator(State state) {
      this.state = state;
      this.xAxis = new Vector2();
      this.yAxis = new Vector2();
   }

   @Override
   public Vector2 toWorld(float mapX, float mapY) {
      int originX = (int) (state.getMapIndexHeight() * state.getCellWidth() / 2f);
      float tileY = mapX / state.getCellHeight();
      float tileX = mapY / state.getCellWidth();
      return new Vector2(
            (tileX - tileY) * state.getCellWidth() / 2f + originX,
            (tileX + tileY) * state.getCellHeight() / 2f
      );
   }

   @Override
   public Vector2 toWorld(int indexX, int indexY) {
      xAxis.set(state.getCellWidth() / 2f, -state.getCellHeight() / 2f);
      yAxis.set(state.getCellWidth() / 2f, state.getCellHeight() / 2f);
      return toWorld(indexX * xAxis.len(), indexY * yAxis.len());
   }

   @Override
   public Vector2 toMap(float worldX, float worldY) {
      worldX -= state.getMapIndexHeight() * state.getCellWidth() / 2f;
      float tileY = worldY / state.getCellHeight();
      float tileX = worldX / state.getCellWidth();
      return new Vector2(
            (tileY + tileX) * state.getCellHeight(),
            (tileY - tileX) * state.getCellHeight()
      );
   }

   @Override
   public Vector2 toMap(int indexX, int indexY) {
      xAxis.set(state.getCellWidth() / 2f, -state.getCellHeight() / 2f);
      yAxis.set(state.getCellWidth() / 2f, state.getCellHeight() / 2f);
      return new Vector2(
            indexX * xAxis.len(),
            indexY * yAxis.len()
      );
   }

   @Override
   public int toIndexX(float worldX) {
      xAxis.set(state.getCellWidth() / 2f, -state.getCellHeight() / 2f);
      Vector2 mapPos = toMap(worldX, 0f);
      return (int)Math.floor(mapPos.x / xAxis.len());
   }

   @Override
   public int toIndexY(float worldY) {
      yAxis.set(state.getCellWidth() / 2f, state.getCellHeight() / 2f);
      Vector2 mapPos = toMap(0f, worldY);
      return (int)Math.floor(mapPos.y / yAxis.len());
   }
}
