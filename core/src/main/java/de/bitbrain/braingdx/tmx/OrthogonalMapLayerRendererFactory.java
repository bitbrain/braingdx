package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;

public class OrthogonalMapLayerRendererFactory implements MapLayerRendererFactory {

    @Override
    public GameObjectRenderer create(int index, TiledMap tiledMap, Camera camera) {
	if (!(camera instanceof OrthographicCamera)) {
	    throw new RuntimeException("OrthographicCamera must be provided for Orthographic TiledMaps!");
	}
	return new OrthogonalMapLayerRenderer(index, tiledMap, (OrthographicCamera) camera);
    }

}
