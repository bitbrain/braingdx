package de.bitbrain.braingdx.tmx;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class MockTiledMapBuilder {

    private TiledMap map;
    private MapLayers layers;

    public MockTiledMapBuilder(int xTiles, int yTiles, int tileSize) {
	map = mock(TiledMap.class);
	layers = new MapLayers();
	MapProperties properties = new MapProperties();
	properties.put(Constants.WIDTH, xTiles);
	properties.put(Constants.HEIGHT, yTiles);
	when(map.getProperties()).thenReturn(properties);
    }

    public MockTiledMapBuilder addLayer() {
	layers.add(mock(TiledMapTileLayer.class));
	return this;
    }

    public MockTiledMapBuilder addLayer(MapLayer layer) {
	layers.add(layer);
	return this;
    }

    public TiledMap build() {
	when(map.getLayers()).thenReturn(layers);
	return map;
    }
}
