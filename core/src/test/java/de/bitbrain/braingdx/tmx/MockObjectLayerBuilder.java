package de.bitbrain.braingdx.tmx;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;

public class MockObjectLayerBuilder {

    private MapObjects objects = new MapObjects();

    MockObjectLayerBuilder addObject(float x, float y) {
	MapObject object = new MapObject();
	MapProperties properties = object.getProperties();
	properties.put(Constants.X, x);
	properties.put(Constants.Y, y);
	objects.add(object);
	return this;
    }

    public MapLayer build() {
	MapLayer layer = mock(MapLayer.class);
	when(layer.getObjects()).thenReturn(objects);
	return layer;
    }
}
