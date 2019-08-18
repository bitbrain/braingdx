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
   public float toWorldX(int indexX) {
      return toWorldX(indexX * state.getCellWidth());
   }

   @Override
   public float toWorldY(int indexY) {
      return toWorldY(indexY * state.getCellHeight());
   }

   @Override
   public float toWorldX(float mapX) {
      return toWorld(mapX, 0f).x;
   }

   @Override
   public float toWorldY(float mapY) {
      return toWorld(0f, mapY).y;
   }

   @Override
   public Vector2 toWorld(float x, float y) {
      xAxis.set(state.getCellWidth() / 2f, -state.getCellHeight() / 2f);
      yAxis.set(state.getCellWidth() / 2f, state.getCellHeight() / 2f);
      float scalar  = yAxis.len() / state.getCellHeight();
      return xAxis.nor().scl(x).scl(scalar)
            .add(yAxis.nor().scl(y).scl(scalar))
            .add(0f, state.getCellHeight() / 2f);
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
      float mapX = toMapX(worldX);
      return (int) Math.floor(mapX / state.getCellWidth());
   }

   @Override
   public int toIndexY(float worldY) {
      float mapY = toMapY(worldY);
      return (int) Math.floor(mapY / state.getCellHeight());
   }
}
