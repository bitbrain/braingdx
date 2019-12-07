package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.ai.pathfinding.AStarPathFinder;
import de.bitbrain.braingdx.ai.pathfinding.PathFinder;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.event.GameEventRouter;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.List;

public class TiledMapContextImpl implements TiledMapContext {

   private final TiledMap tiledMap;
   private final State state;
   private final GameWorld gameWorld;
   private final GameEventRouter eventRouter;
   private final GameObjectRenderManager renderManager;
   private final GameObjectUpdater gameObjectUpdater;
   private final BehaviorManager behaviorManager;
   private final PositionTranslator positionTranslator;
   private boolean debug;

   private PathFinder pathFinder;

   public TiledMapContextImpl(
         TiledMap tiledMap,
         State state,
         GameWorld gameWorld,
         GameEventRouter eventRouter,
         GameObjectRenderManager renderManager,
         BehaviorManager behaviorManager,
         GameEventManager gameEventManager,
         PositionTranslator positionTranslator) {
      this.tiledMap = tiledMap;
      this.state = state;
      this.gameWorld = gameWorld;
      this.eventRouter = eventRouter;
      this.renderManager = renderManager;
      this.behaviorManager = behaviorManager;
      this.positionTranslator = positionTranslator;
      // Apply behaviours
      gameObjectUpdater = new GameObjectUpdater(this, state, gameEventManager, positionTranslator);
      this.behaviorManager.apply(gameObjectUpdater);
   }

   @Override
   public TiledMap getTiledMap() {
      return tiledMap;
   }

   @Override
   public PathFinder getPathFinder() {
      if (pathFinder == null) {
         this.pathFinder = new AStarPathFinder(this, (short) 100, false);
      }
      return this.pathFinder;
   }

   @Override
   public void setEventFactory(GameEventFactory eventFactory) {
      this.eventRouter.setEventFactory(eventFactory);
   }

   @Override
   public int highestZIndexAt(int tileX, int tileY) {
      Integer[][] heightMap = state.getHeightMap();
      if (verifyIndex(tileX, tileY) && heightMap != null) {
         return heightMap[tileX][tileY];
      } else {
         return -1;
      }
   }

   @Override
   public int highestZIndexAt(float x, float y) {
      int tileX = (int) Math.floor(x / state.getCellWidth());
      int tileY = (int) Math.floor(y / state.getCellWidth());
      return highestZIndexAt(tileX, tileY);
   }

   @Override
   public int layerIndexOf(GameObject object) {
      if (object.hasAttribute(Constants.LAYER_INDEX)) {
         return (Integer) object.getAttribute(Constants.LAYER_INDEX);
      } else {
         return -1;
      }
   }

   @Override
   public int lastLayerIndexOf(GameObject object) {
      if (object.hasAttribute(Constants.LAST_LAYER_INDEX)) {
         return (Integer) object.getAttribute(Constants.LAST_LAYER_INDEX);
      } else {
         return layerIndexOf(object);
      }
   }

   @Override
   public int getNumberOfRows() {
      return state.getMapIndexHeight();
   }

   @Override
   public int getNumberOfColumns() {
      return state.getMapIndexWidth();
   }

   @Override
   public void setLayerIndex(GameObject object, int layerIndex) {
      if (layerIndex > state.getNumberOfLayers() - 1) {
         throw new TiledMapException("Layer index is too high: " + layerIndex);
      }
      object.setAttribute(Constants.LAYER_INDEX, layerIndex);
   }

   @Override
   public GameObject getGameObjectAt(int tileX, int tileY, int layer) {
      List<GameObject> objects = gameWorld.getObjects();
      for (int i = 0; i < objects.size(); ++i) {
         GameObject worldObject = objects.get(i);
         int objectTileX = positionTranslator.toIndexX(worldObject.getLeft());
         int objectTileY = positionTranslator.toIndexY(worldObject.getTop());
         int layerIndex = layerIndexOf(worldObject);
         if (objectTileX == tileX && objectTileY == tileY && layer == layerIndex) {
            return worldObject;
         }
      }
      return null;
   }

   @Override
   public MapProperties getPropertiesAt(int tileX, int tileY, int layer) {
      return state.getState(tileX, tileY, layer).getProperties();
   }

   @Override
   public PositionTranslator getPositionTranslator() {
      return positionTranslator;
   }

   @Override
   public float getCellWidth() {
      return state.getCellWidth();
   }

   @Override
   public float getCellHeight() {
      return state.getCellHeight();
   }

   @Override
   public float getWorldWidth() {
      return getCellWidth() * getNumberOfColumns();
   }

   @Override
   public float getWorldHeight() {
      return getCellHeight() * getNumberOfRows();
   }

   @Override
   public boolean isDebug() {
      return debug;
   }

   @Override
   public void setDebug(boolean enabled) {
      this.debug = enabled;
   }

   @Override
   public boolean isCollision(int tileX, int tileY, int layer) {
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision();
   }

   @Override
   public boolean isExclusiveCollision(int tileX, int tileY, int layer, GameObject source) {
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && !state.getState(tileX, tileY, layer).isFingerprint(source.getId());
   }

   @Override
   public boolean isInclusiveCollision(int tileX, int tileY, int layer, GameObject source) {
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && state.getState(tileX, tileY, layer).isFingerprint(source.getId());
   }

   @Override
   public boolean isCollision(float x, float y, int layer) {
      int tileX = positionTranslator.toIndexX(x);
      int tileY = positionTranslator.toIndexY(y);
      return isCollision(tileX, tileY, layer);
   }

   @Override
   public boolean isExclusiveCollision(GameObject object) {
      return isExclusiveCollision(object.getLeft(), object.getHeight(), layerIndexOf(object), object);
   }

   @Override
   public boolean isInclusiveCollision(GameObject object) {
      return isInclusiveCollision(object.getLeft(), object.getHeight(), layerIndexOf(object), object);
   }

   @Override
   public boolean isExclusiveCollision(float x, float y, int layer, GameObject object) {
      int tileX = positionTranslator.toIndexX(x);
      int tileY = positionTranslator.toIndexY(y);
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && !state.getState(tileX, tileY, layer).isFingerprint(object.getId());
   }

   @Override
   public boolean isInclusiveCollision(float x, float y, int layer, GameObject object) {
      int tileX = positionTranslator.toIndexX(x);
      int tileY = positionTranslator.toIndexY(y);
      if (!verifyIndex(tileX, tileY)) {
         return true;
      }
      return state.getState(tileX, tileY, layer).isCollision()
            && state.getState(tileX, tileY, layer).isFingerprint(object.getId());
   }

   @Override
   public boolean isCollision(GameObject object, int tileOffsetX, int tileOffsetY) {
      int layer = layerIndexOf(object);
      int tileX = positionTranslator.toIndexX(object.getLeft()) + tileOffsetX;
      int tileY = positionTranslator.toIndexY(object.getTop()) + tileOffsetY;
      return isCollision(tileX, tileY, layer);
   }

   @Override
   public void dispose() {
      for (String id : state.getLayerIds()) {
         gameWorld.clearGroup(id);
         renderManager.unregister(id);
      }
      state.clear();
      behaviorManager.remove(gameObjectUpdater);
   }

   private boolean verifyIndex(int indexX, int indexY) {
      return indexX >= 0 && indexY >= 0 && indexX < state.getMapIndexWidth() && indexY < state.getMapIndexHeight();
   }
}
