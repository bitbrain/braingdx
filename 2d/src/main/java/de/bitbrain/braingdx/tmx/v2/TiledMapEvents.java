package de.bitbrain.braingdx.tmx.v2;

import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;

public final class TiledMapEvents {

   public static class OnLayerChangeEvent implements GameEvent {
      private final int lastLayerIndex;
      private final int currentLayerIndex;
      private final GameObject object;
      private final TiledMapContext context;

      public OnLayerChangeEvent(int lastLayerIndex, int currentLayerIndex, GameObject object, TiledMapContext context) {
         this.lastLayerIndex = lastLayerIndex;
         this.currentLayerIndex = currentLayerIndex;
         this.object = object;
         this.context = context;
      }

      public int getLastLayerIndex() {
         return lastLayerIndex;
      }

      public int getCurrentLayerIndex() {
         return currentLayerIndex;
      }

      public GameObject getObject() {
         return object;
      }

      public TiledMapContext getContext() {
         return context;
      }
   }

   public static class OnEnterCellEvent implements GameEvent {
      private final int xIndex;
      private final int yIndex;
      private final GameObject object;
      private final TiledMapContext context;

      public OnEnterCellEvent(int xIndex, int yIndex, GameObject object, TiledMapContext context) {
         this.xIndex = xIndex;
         this.yIndex = yIndex;
         this.object = object;
         this.context = context;
      }

      public int getxIndex() {
         return xIndex;
      }

      public int getyIndex() {
         return yIndex;
      }

      public GameObject getObject() {
         return object;
      }

      public TiledMapContext getContext() {
         return context;
      }
   }

   public static class OnLoadGameObjectEvent implements GameEvent {
      private final GameObject object;
      private final TiledMapContext context;

      public OnLoadGameObjectEvent(GameObject object, TiledMapContext context) {
         this.object = object;
         this.context = context;
      }

      public GameObject getObject() {
         return object;
      }

      public TiledMapContext getTiledMapContext() {
         return context;
      }
   }

   public static class BeforeLoadEvent implements GameEvent {
      private final TiledMap map;

      public BeforeLoadEvent(TiledMap map) {
         this.map = map;
      }

      public TiledMap getMap() {
         return map;
      }
   }

   public static class AfterLoadEvent implements GameEvent {
      private final TiledMap map;
      private final TiledMapContext context;

      public AfterLoadEvent(TiledMap map, TiledMapContext context) {
         this.map = map;
         this.context = context;
      }

      public TiledMapContext getTiledMapContext() {
         return context;
      }

      public TiledMap getTiledMap() {
         return map;
      }
   }

   public static class BeforeUnloadEvent implements GameEvent {
      private final TiledMapContext context;

      public BeforeUnloadEvent(TiledMapContext context) {
         this.context = context;
      }

      public TiledMapContext getTiledMapContext() {
         return context;
      }
   }

   public static class AfterUnloadEvent implements GameEvent {
   }
}
