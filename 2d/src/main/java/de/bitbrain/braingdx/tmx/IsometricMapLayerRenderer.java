package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import de.bitbrain.braingdx.graphics.renderer.GameObject2DRenderer;
import de.bitbrain.braingdx.world.GameObject;

public class IsometricMapLayerRenderer extends GameObject2DRenderer {

   private final TiledMapTileLayer layer;
   private final OrthographicCamera camera;
   private final TiledMap map;
   private IsometricTiledMapRenderer renderer;

   public IsometricMapLayerRenderer(
         int layerIndex,
         OrthographicCamera camera,
         TiledMap map) {
      layer = (TiledMapTileLayer) map.getLayers().get(layerIndex);
      this.camera = camera;
      this.map = map;
   }

   @Override
   public void render(GameObject object, Batch batch, float delta) {
      if (renderer == null) {
         renderer = new IsometricTiledMapRenderer(map, batch);
      }
      AnimatedTiledMapTile.updateAnimationBaseTime();
      renderer.setView(camera);
      renderer.renderTileLayer(layer);
   }
}
