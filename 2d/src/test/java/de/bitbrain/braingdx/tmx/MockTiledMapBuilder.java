package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockTiledMapBuilder {

   private TiledMap map;
   private MapLayers layers;
   private float size;

   public MockTiledMapBuilder(int xTiles, int yTiles, int tileSize) {
      map = mock(TiledMap.class);
      layers = new MapLayers();
      MapProperties properties = new MapProperties();
      properties.put(Constants.WIDTH, xTiles);
      properties.put(Constants.HEIGHT, yTiles);
      when(map.getProperties()).thenReturn(properties);
      this.size = tileSize;
   }

   public MockTiledMapBuilder addLayer() {
      TiledMapTileLayer layer = mock(TiledMapTileLayer.class);
      Cell cell = mock(Cell.class);
      MapProperties properties = new MapProperties();
      when(layer.getCell(anyInt(), anyInt())).thenReturn(cell);
      when(layer.getTileWidth()).thenReturn(size);
      when(layer.getTileHeight()).thenReturn(size);
      Mockito.when(layer.getProperties()).thenReturn(properties);
      layers.add(layer);
      return this;
   }

   public MockTiledMapBuilder addLayer(MapLayer layer) {
      layers.add(layer);
      if (layer instanceof TiledMapTileLayer) {
         when(((TiledMapTileLayer) layer).getTileWidth()).thenReturn(size);
         when(((TiledMapTileLayer) layer).getTileHeight()).thenReturn(size);
      }
      return this;
   }

   public TiledMap build() {
      when(map.getLayers()).thenReturn(layers);
      return map;
   }
}
