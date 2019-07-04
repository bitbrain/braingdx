package de.bitbrain.braingdx.tmx.v2;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import org.mockito.Mockito;

public class MockTiledTileLayerBuilder {

   private TiledMapTileLayer layer = Mockito.mock(TiledMapTileLayer.class);

   MockTiledTileLayerBuilder addCell(int x, int y) {
      return addCell(x, y, false);
   }

   MockTiledTileLayerBuilder addCell(int x, int y, boolean collision) {
      Cell cell = Mockito.mock(Cell.class);
      TiledMapTile tile = Mockito.mock(TiledMapTile.class);
      MapProperties layerProperties = new MapProperties();
      MapProperties properties = new MapProperties();
      properties.put(Constants.COLLISION, collision);
      Mockito.when(tile.getProperties()).thenReturn(properties);
      Mockito.when(cell.getTile()).thenReturn(tile);
      Mockito.when(layer.getCell(x, y)).thenReturn(cell);
      Mockito.when(layer.getProperties()).thenReturn(layerProperties);
      return this;
   }

   public MockTiledTileLayerBuilder collision(boolean collision) {
      layer.getProperties().put(Constants.COLLISION, collision ? "true" : "false");
      return this;
   }

   public TiledMapTileLayer build() {
      return layer;
   }
}
