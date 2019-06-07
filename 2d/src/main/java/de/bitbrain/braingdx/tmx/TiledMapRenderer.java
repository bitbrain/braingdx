/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.tmx;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer2D;

public class TiledMapRenderer extends RenderLayer2D {

   private final OrthogonalTiledMapRenderer mapRenderer;

   private final OrthographicCamera camera;

   public TiledMapRenderer(TiledMap map, OrthographicCamera camera) {
      this.camera = camera;
      mapRenderer = new OrthogonalTiledMapRenderer(map);
   }

   @Override
   public void render(Batch batch, float delta) {
      mapRenderer.setView(camera);
      mapRenderer.render();
   }

}
