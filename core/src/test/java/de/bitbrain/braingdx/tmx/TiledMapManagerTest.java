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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;

import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.BehaviorManagerAdapter;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

/**
 * Tests and verifies the integrity of the {@link TiledMapManager} implementation used within
 * braingdx.
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
@RunWith(MockitoJUnitRunner.class)
public class TiledMapManagerTest {

    @Mock
    private GameObjectRenderManager renderManager;

    @Mock
    private OrthographicCamera camera;

    private TiledMapManager tiledMapManager;

    private GameWorld world;

    @Before
    public void beforeTest() {
	world = new GameWorld(camera);
	BehaviorManager behaviorManager = new BehaviorManager();
	world.addListener(new BehaviorManagerAdapter(behaviorManager));
	tiledMapManager = new TiledMapManagerImpl(behaviorManager, world, renderManager) {
	    @Override
	    protected Map<TiledMapType, MapLayerRendererFactory> createFactories() {
		Map<TiledMapType, MapLayerRendererFactory> mockMap = new HashMap<TiledMapType, MapLayerRendererFactory>();
		for (TiledMapType type : TiledMapType.values()) {
		    mockMap.put(type, new MockMapLayerRendererFactory());
		}
		return mockMap;
	    }
	};
    }

    @Test(expected = NullPointerException.class)
    public void load_WithNullValues() throws TiledMapLoadException {
	tiledMapManager.load(null, null, null);
    }

    @Test(expected = TiledMapLoadException.class)
    public void load_withInvalidTiledMapWidth() throws TiledMapLoadException {
	TiledMap map = new MockTiledMapBuilder(0, 1, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test(expected = TiledMapLoadException.class)
    public void load_withInvalidTiledMapHeight() throws TiledMapLoadException {
	TiledMap map = new MockTiledMapBuilder(1, 0, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test(expected = TiledMapLoadException.class)
    public void load_withInvalidTiledMapDimensions() throws TiledMapLoadException {
	TiledMap map = new MockTiledMapBuilder(0, 0, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test(expected = TiledMapLoadException.class)
    public void load_withInvalidMapLayers() throws TiledMapLoadException {
	TiledMap map = new MockTiledMapBuilder(1, 0, 1).build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test
    public void load_withNoMapObjects() throws TiledMapLoadException {
	TiledMap map = new MockTiledMapBuilder(1, 1, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
	assertThat(world.size()).isEqualTo(1);
	inOrder(renderManager).verify(renderManager, calls(1)).register(any(), any(GameObjectRenderer.class));
    }

    @Test
    public void load_withMapObjects() throws TiledMapLoadException {
	final String type = "game_object";
	TiledMap map = new MockTiledMapBuilder(1, 1, 1)
		.addLayer()
		.addLayer()
		.addLayer(new MockObjectLayerBuilder()
			.addObject(0, 0, type)
			.addObject(0, 0, type)
			.build())
		.addLayer()
		.build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
	assertThat(world.size()).isEqualTo(5);
	inOrder(renderManager).verify(renderManager, calls(3)).register(any(), any(GameObjectRenderer.class));
    }
    
    @Test
    public void load_withMapObjectsValidAPI() throws TiledMapLoadException {
	final String type = "game_object";
	TiledMapListener listenerMock = mock(TiledMapListener.class);
	ArgumentCaptor<GameObject> gameObjectCaptor = ArgumentCaptor.forClass(GameObject.class);
	ArgumentCaptor<TiledMapAPI> apiCaptor = ArgumentCaptor.forClass(TiledMapAPI.class);
	Mockito.doNothing().when(listenerMock).onLoad(gameObjectCaptor.capture(), apiCaptor.capture());
	TiledMap map = new MockTiledMapBuilder(1, 1, 1)
		.addLayer()
		.addLayer()
		.addLayer(new MockObjectLayerBuilder()
			.addObject(0, 0, type)
			.addObject(0, 0, type)
			.build())
		.addLayer()
		.build();
	tiledMapManager.addListener(listenerMock);
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
	assertThat(gameObjectCaptor.getAllValues()).hasSize(2);
	assertThat(apiCaptor.getValue()).isNotNull();
	assertThat(apiCaptor.getValue().getNumberOfColumns()).isEqualTo(1);
	assertThat(apiCaptor.getValue().getNumberOfRows()).isEqualTo(1);
    }
}
