package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.event.GameEvent;
import de.bitbrain.braingdx.world.GameObject;

public final class TiledMapEvents {

   public static class OnLayerChangeEvent implements GameEvent {
      private final int lastLayerIndex;
      private final int currentLayerIndex;
      private final GameObject object;
      private final TiledMapAPI api;
      public OnLayerChangeEvent(int lastLayerIndex, int currentLayerIndex, GameObject object, TiledMapAPI api) {
         this.lastLayerIndex = lastLayerIndex;
         this.currentLayerIndex = currentLayerIndex;
         this.object = object;
         this.api = api;
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

      public TiledMapAPI getApi() {
         return api;
      }
   }

   public static class OnEnterCellEvent implements GameEvent {
      private final int xIndex;
      private final int yIndex;
      private final GameObject object;
      private final TiledMapAPI api;

      public OnEnterCellEvent(int xIndex, int yIndex, GameObject object, TiledMapAPI api) {
         this.xIndex = xIndex;
         this.yIndex = yIndex;
         this.object = object;
         this.api = api;
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

      public TiledMapAPI getApi() {
         return api;
      }
   }

   public static class OnLoadGameObjectEvent implements GameEvent {
      private final GameObject object;
      private final TiledMapAPI api;

      public OnLoadGameObjectEvent(GameObject object, TiledMapAPI api) {
         this.object = object;
         this.api = api;
      }

      public GameObject getObject() {
         return object;
      }

      public TiledMapAPI getTiledMapAPI() {
         return api;
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
      private final TiledMapAPI api;

      public AfterLoadEvent(TiledMap map, TiledMapAPI api) {
         this.map = map;
         this.api = api;
      }

      public TiledMapAPI getTiledMapAPI() {
         return api;
      }

      public TiledMap getTiledMap() {
         return map;
      }
   }

   public static class BeforeUnloadEvent implements GameEvent {
      private final TiledMapAPI api;

      public BeforeUnloadEvent(TiledMapAPI api) {
         this.api = api;
      }

      public TiledMapAPI getTiledMapAPI() {
         return api;
      }
   }

   public static class AfterUnloadEvent implements GameEvent {   }
}
