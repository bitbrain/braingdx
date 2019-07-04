package de.bitbrain.braingdx.tmx.v2;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.ai.pathfinding.PathFinder;
import de.bitbrain.braingdx.event.GameEventFactory;
import de.bitbrain.braingdx.movement.TiledCollisionResolver;
import de.bitbrain.braingdx.world.GameObject;

public interface TiledMapContext extends TiledCollisionResolver, Disposable {

   TiledMap getTiledMap();

   PathFinder getPathFinder();

   void setEventFactory(GameEventFactory eventFactory);

   int highestZIndexAt(int tileX, int tileY);

   int highestZIndexAt(float x, float y);

   int layerIndexOf(GameObject object);

   int lastLayerIndexOf(GameObject object);

   int getNumberOfRows();

   int getNumberOfColumns();

   void setLayerIndex(GameObject object, int layerIndex);

   GameObject getGameObjectAt(int tileX, int tileY, int layer);

   MapProperties getPropertiesAt(int tileX, int tileY, int layer);

   float getCellWidth();

   float getCellHeight();

   float getWorldWidth();

   float getWorldHeight();

   boolean isDebug();

   void setDebug(boolean enabled);

}
