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
    public void load_WithNullValues() throws TiledMapException {
	tiledMapManager.load(null, null, null);
    }

    @Test(expected = TiledMapException.class)
    public void load_withInvalidTiledMapWidth() throws TiledMapException {
	TiledMap map = new MockTiledMapBuilder(0, 1, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test(expected = TiledMapException.class)
    public void load_withInvalidTiledMapHeight() throws TiledMapException {
	TiledMap map = new MockTiledMapBuilder(1, 0, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test(expected = TiledMapException.class)
    public void load_withInvalidTiledMapDimensions() throws TiledMapException {
	TiledMap map = new MockTiledMapBuilder(0, 0, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test(expected = TiledMapException.class)
    public void load_withInvalidMapLayers() throws TiledMapException {
	TiledMap map = new MockTiledMapBuilder(1, 0, 1).build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
    }

    @Test
    public void load_withNoMapObjects() throws TiledMapException {
	TiledMap map = new MockTiledMapBuilder(1, 1, 1).addLayer().build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
	assertThat(world.size()).isEqualTo(1);
	inOrder(renderManager).verify(renderManager, calls(1)).register(any(), any(GameObjectRenderer.class));
    }

    @Test
    public void load_withMapObjects() throws TiledMapException {
	final String typeA = "typeA";
	final String typeB = "typeB";
	TiledMap map = new MockTiledMapBuilder(5, 5, 5)
		.addLayer()
		.addLayer()
		.addLayer(new MockObjectLayerBuilder()
			.addObject(0, 0, 2, typeA)
			.addObject(0, 0, 6, typeB)
			.build())
		.addLayer()
		.build();
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
	GameObject objectA = null;
	GameObject objectB = null;

	for (GameObject object : world) {
	    if (object.getType().equals(typeA)) {
		objectA = object;
	    } else if (object.getType().equals(typeB)) {
		objectB = object;
	    }
	}
	assertThat(objectA).isNotNull();
	assertThat(objectB).isNotNull();
	assertThat(objectA.getWidth()).isEqualTo(5f);
	assertThat(objectA.getHeight()).isEqualTo(5f);
	assertThat(objectB.getWidth()).isEqualTo(10f);
	assertThat(objectB.getHeight()).isEqualTo(10f);
	assertThat(world.size()).isEqualTo(5);
	inOrder(renderManager).verify(renderManager, calls(3)).register(any(), any(GameObjectRenderer.class));
    }
    
    @Test
    public void load_withMapObjectsValidAPI() throws TiledMapException {
	final String type = "game_object";
	TiledMapListener listenerMock = mock(TiledMapListener.class);
	ArgumentCaptor<GameObject> gameObjectCaptor = ArgumentCaptor.forClass(GameObject.class);
	ArgumentCaptor<TiledMapAPI> apiCaptor = ArgumentCaptor.forClass(TiledMapAPI.class);
	Mockito.doNothing().when(listenerMock).onLoad(gameObjectCaptor.capture(), apiCaptor.capture());
	TiledMap map = new MockTiledMapBuilder(2, 2, 1)
		.addLayer()
		.addLayer()
		.addLayer(new MockObjectLayerBuilder()
			.addObject(0, 0, type, false)
			.addObject(1, 1, type)
			.build())
		.addLayer()
		.build();
	tiledMapManager.addListener(listenerMock);
	tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
	assertThat(gameObjectCaptor.getAllValues()).hasSize(2);
	assertThat(apiCaptor.getValue()).isNotNull();
	assertThat(apiCaptor.getValue().getNumberOfColumns()).isEqualTo(2);
	assertThat(apiCaptor.getValue().getNumberOfRows()).isEqualTo(2);
	assertThat(apiCaptor.getValue().highestZIndexAt(0, 0)).isGreaterThan(2);
	assertThat(apiCaptor.getValue().isCollision(0, 0, 1)).isFalse();
	assertThat(apiCaptor.getValue().isCollision(1, 1, 1)).isTrue();
    }
    

    @Test
    public void load_withSimple3x3Map_validCollisions() throws TiledMapException {
	final TiledMapAPI api = tiledMapManager.getAPI();
	// Load game world
	tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
	final GameObject objectA = api.getGameObjectAt(0, 1, 0);
	final GameObject objectB = api.getGameObjectAt(1, 1, 0);
	final GameObject objectC = api.getGameObjectAt(0, 0, 1);

	// Update the world
	world.update(0f);

	// Validate state
	assertThat(objectA).isNotNull();
	assertThat(objectB).isNotNull();
	assertThat(objectC).isNotNull();

	// Validate collisions
	assertThat(api.isCollision(0, 0, 0)).isFalse();
	assertThat(api.isCollision(1, 0, 0)).isFalse();
	assertThat(api.isCollision(0, 1, 0)).isTrue();
	assertThat(api.isCollision(1, 1, 0)).isTrue();
	assertThat(api.isCollision(0, 0, 1)).isTrue();
	assertThat(api.isCollision(1, 0, 1)).isFalse();
	assertThat(api.isCollision(0, 1, 1)).isFalse();
	assertThat(api.isCollision(1, 1, 1)).isFalse();
    }
    
    @Test
    public void load_withSimple3x3Map_validUpdatingAfterMovement() throws TiledMapException {
	final TiledMapAPI api = tiledMapManager.getAPI();
	tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
	final GameObject objectA = api.getGameObjectAt(0, 1, 0);
	objectA.setPosition(1, 0);
	world.update(0f);
	assertThat(api.isCollision(1, 0, 0)).isTrue();
	assertThat(api.isCollision(0, 1, 0)).isFalse();
    }

    @Test
    public void load_withSimple3x3Map_validUpdatingAfterChangingLayers() throws TiledMapException {
	final TiledMapAPI api = tiledMapManager.getAPI();
	tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
	final GameObject objectA = api.getGameObjectAt(0, 1, 0);
	objectA.setPosition(1, 0);
	world.update(0f);
	api.setLayerIndex(objectA, 1);
	world.update(0f);

	assertThat(api.isCollision(1, 0, 1)).isTrue();
	assertThat(api.isCollision(1, 0, 0)).isFalse();
	assertThat(api.isCollision(0, 1, 0)).isFalse();
	assertThat(api.isCollision(0, 1, 0)).isFalse();
    }

    @Test(expected = TiledMapException.class)
    public void load_withSimple3x3Map_invalidUpdatingAfterChangingLayers() throws TiledMapException {
	final TiledMapAPI api = tiledMapManager.getAPI();
	tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
	final GameObject objectA = api.getGameObjectAt(0, 1, 0);
	api.setLayerIndex(objectA, 2);
    }

    /**
     * Creates collisions on different layers
     * <p>
     * setup:
     * 
     * <pre>
     * x x x x
     * x c   x  
     * x a b x
     * x x x x
     * </pre>
     */
    private TiledMap createSimple3x3Map() {
	// Initialization
	final String typeA = "a";
	final String typeB = "b";
	final String typeC = "c";
	return new MockTiledMapBuilder(2, 2, 1)
		.addLayer(new MockTiledTileLayerBuilder()
			.addCell(0, 0)
			.addCell(0, 1)
			.addCell(1, 0)
			.addCell(1, 1)
			.build())
		.addLayer(new MockObjectLayerBuilder()
			.addObject(0, 1, typeA)
			.addObject(1, 1, typeB)
			.build())
		.addLayer(new MockTiledTileLayerBuilder()
			.addCell(0, 0)
			.addCell(0, 1)
			.addCell(1, 0)
			.addCell(1, 1)
			.build())
		.addLayer(new MockObjectLayerBuilder()
			.addObject(0, 0, typeC)
			.build())
		.build();
    }
}
