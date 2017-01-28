package de.bitbrain.braingdx.tmx;

import static org.mockito.Mockito.mock;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;

public class MockMapLayerRendererFactory implements MapLayerRendererFactory {

    @Override
    public GameObjectRenderer create(int index, TiledMap tiledMap, Camera camera) {
	return mock(GameObjectRenderer.class);
    }

}
