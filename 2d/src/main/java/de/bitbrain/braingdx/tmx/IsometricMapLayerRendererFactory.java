package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.renderer.GameObject2DRenderer;
import de.bitbrain.braingdx.world.GameObject;

public class IsometricMapLayerRendererFactory implements MapLayerRendererFactory {

   @Override
   public IsometricMapLayerRenderer create(int index, TiledMap tiledMap, Camera camera) {
      return new IsometricMapLayerRenderer(index, (OrthographicCamera)camera, tiledMap);
   }

   @Override
   public GameObjectRenderManager.GameObjectRenderer createDebug(TiledMapContext context, State state, Camera camera) {
      return new GameObject2DRenderer() {
         @Override
         public void render(GameObject object, Batch batch, float delta) {
            // TODO implement me
         }
      };
   }
}
