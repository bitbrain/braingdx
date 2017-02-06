package de.bitbrain.braingdx.tmx;

import org.mockito.Mockito;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class MockTiledTileLayerBuilder {

    private TiledMapTileLayer layer = Mockito.mock(TiledMapTileLayer.class);

    MockTiledTileLayerBuilder addCell(int x, int y) {
	Cell cell = Mockito.mock(Cell.class);
	TiledMapTile tile = Mockito.mock(TiledMapTile.class);
	MapProperties properties = new MapProperties();
	Mockito.when(tile.getProperties()).thenReturn(properties);
	Mockito.when(cell.getTile()).thenReturn(tile);
	Mockito.when(layer.getCell(x, y)).thenReturn(cell);
	return this;
    }

    public TiledMapTileLayer build() {
	return layer;
    }
}
