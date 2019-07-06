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
import com.badlogic.gdx.maps.tiled.TiledMap;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.behavior.BehaviorManagerAdapter;
import de.bitbrain.braingdx.event.*;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager.GameObjectRenderer;
import de.bitbrain.braingdx.utils.GdxUtils;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.inOrder;

/**
 * Tests and verifies the integrity of the {@link TiledMapManager} implementation used within
 * braingdx.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class TiledMapManagerTest {

   @Mock
   private GameObjectRenderManager renderManager;
   @Mock
   private OrthographicCamera camera;
   private TiledMapManagerImpl tiledMapManager;
   private GameEventManager gameEventManager;
   private GameWorld world;

   @Before
   public void beforeTest() {
      world = new GameWorld();
      gameEventManager = new GameEventManagerImpl();
      GdxUtils.mockApplicationContext();
      BehaviorManager behaviorManager = new BehaviorManager(world);
      world.addListener(new BehaviorManagerAdapter(behaviorManager));
      GameEventRouter tiledMapEventRouter = new GameEventRouter(
            gameEventManager,
            world,
            new TiledMapInfoExtractor()
      );
      behaviorManager.apply(tiledMapEventRouter);
      TiledMapContextFactory contextFactory = new TiledMapContextFactory(
            renderManager,
            world,
            gameEventManager,
            tiledMapEventRouter,
            behaviorManager
      );
      tiledMapManager = new TiledMapManagerImpl(world, gameEventManager, contextFactory) {
         @Override
         protected Map<TiledMapType, MapLayerRendererFactory> createRendererFactories() {
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
      tiledMapManager.load(null, null, (TiledMapConfig)null);
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
      assertThat(world.size()).isEqualTo(2); // 1 + 1 debug layer
      inOrder(renderManager).verify(renderManager, calls(1)).register(any(), any(GameObjectRenderer.class));
   }

   @Test
   public void load_withMapObjects() throws TiledMapException {
      final String typeA = "typeA";
      final String typeB = "typeB";
      TiledMap map = new MockTiledMapBuilder(5, 5, 5).addLayer().addLayer()
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, 2, typeA).addObject(0, 0, 6, typeB).build())
            .addLayer().build();
      tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      GameObject objectA = null;
      GameObject objectB = null;

      for (GameObject object : world.getObjects()) {
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
      assertThat(world.size()).isEqualTo(6); // 5 + 1 debug layer
      inOrder(renderManager).verify(renderManager, calls(3)).register(any(), any(GameObjectRenderer.class));
   }

   @Test
   public void load_withMapObjectsValidAPI() throws TiledMapException {
      final String type = "game_object";
      final AtomicBoolean failed = new AtomicBoolean(true);
      final AtomicBoolean firstObject = new AtomicBoolean(true);
      gameEventManager.register(new GameEventListener<TiledMapEvents.OnLoadGameObjectEvent>() {
         @Override
         public void onEvent(TiledMapEvents.OnLoadGameObjectEvent event) {
            if (firstObject.get()) {
               assertThat(event.getTiledMapContext().getNumberOfColumns()).isEqualTo(2);
               assertThat(event.getTiledMapContext().getNumberOfRows()).isEqualTo(2);
               assertThat(event.getTiledMapContext().highestZIndexAt(0, 0)).isGreaterThan(2);
               assertThat(event.getTiledMapContext().isCollision(0, 0, 1)).isFalse();
               assertThat(event.getTiledMapContext().isCollision(1, 1, 1)).isFalse();
               failed.set(false);
               firstObject.set(false);
            } else {
               assertThat(event.getTiledMapContext().getNumberOfColumns()).isEqualTo(2);
               assertThat(event.getTiledMapContext().getNumberOfRows()).isEqualTo(2);
               assertThat(event.getTiledMapContext().highestZIndexAt(0, 0)).isGreaterThan(2);
               assertThat(event.getTiledMapContext().isCollision(0, 0, 1)).isFalse();
               assertThat(event.getTiledMapContext().isCollision(1, 1, 1)).isTrue();
            }
         }
      }, TiledMapEvents.OnLoadGameObjectEvent.class);
      TiledMap map = new MockTiledMapBuilder(2, 2, 1).addLayer().addLayer()
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, type, false).addObject(1, 1, type).build())
            .addLayer().build();
      tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      assertFalse(failed.get());
   }

   @Test
   public void load_withSimple3x3Map_validCollisions() throws TiledMapException {
      TiledMapContext context = tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
      final GameObject objectA = context.getGameObjectAt(0, 1, 0);
      final GameObject objectB = context.getGameObjectAt(1, 1, 0);
      final GameObject objectC = context.getGameObjectAt(0, 0, 1);

      // Update the world
      world.update(0f);

      // Validate state
      assertThat(objectA).isNotNull();
      assertThat(objectB).isNotNull();
      assertThat(objectC).isNotNull();

      // Validate collisions
      assertThat(context.isCollision(0, 0, 0)).isFalse();
      assertThat(context.isCollision(1, 0, 0)).isFalse();
      assertThat(context.isCollision(0, 1, 0)).isTrue();
      assertThat(context.isCollision(1, 1, 0)).isTrue();
      assertThat(context.isCollision(0, 0, 1)).isTrue();
      assertThat(context.isCollision(1, 0, 1)).isFalse();
      assertThat(context.isCollision(0, 1, 1)).isTrue();
      assertThat(context.isCollision(1, 1, 1)).isTrue();
   }

   @Test
   public void load_withSimple3x3Map_validCollisionsWithCollisionLayer() throws TiledMapException {
      TiledMapContext context = tiledMapManager.load(createSimple3x3Map(true), camera, TiledMapType.ORTHOGONAL);
      final GameObject objectA = context.getGameObjectAt(0, 1, 0);
      final GameObject objectB = context.getGameObjectAt(1, 1, 0);
      final GameObject objectC = context.getGameObjectAt(0, 0, 1);

      // Update the world
      world.update(0f);

      // Validate state
      assertThat(objectA).isNotNull();
      assertThat(objectB).isNotNull();
      assertThat(objectC).isNotNull();

      // Validate collisions
      assertThat(context.isCollision(0, 0, 0)).isFalse();
      assertThat(context.isCollision(1, 0, 0)).isFalse();
      assertThat(context.isCollision(0, 1, 0)).isTrue();
      assertThat(context.isCollision(1, 1, 0)).isTrue();
      assertThat(context.isCollision(0, 0, 1)).isTrue();
      assertThat(context.isCollision(1, 0, 1)).isFalse();
      assertThat(context.isCollision(0, 1, 1)).isFalse();
      assertThat(context.isCollision(1, 1, 1)).isFalse();
   }

   @Test
   public void load_withSimple3x3Map_validUpdatingAfterMovement() throws TiledMapException {
      TiledMapContext context = tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
      final GameObject objectA = context.getGameObjectAt(0, 1, 0);
      objectA.setPosition(1, 0);
      world.update(0f);
      assertThat(context.isCollision(1, 0, 0)).isTrue();
      assertThat(context.isCollision(0, 1, 0)).isFalse();
   }

   @Test
   public void load_withSimple3x3Map_validUpdatingAfterChangingLayers() throws TiledMapException {
      TiledMapContext context = tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
      final GameObject objectA = context.getGameObjectAt(0, 1, 0);
      objectA.setPosition(1, 0);
      world.update(0f);
      context.setLayerIndex(objectA, 1);
      world.update(0f);
      assertThat(context.isCollision(1, 0, 1)).isTrue();
      assertThat(context.isCollision(1, 0, 0)).isFalse();
      assertThat(context.isCollision(0, 1, 0)).isFalse();
   }

   @Test(expected = TiledMapException.class)
   public void load_withSimple3x3Map_invalidUpdatingAfterChangingLayers() throws TiledMapException {
      TiledMapContext context = tiledMapManager.load(createSimple3x3Map(), camera, TiledMapType.ORTHOGONAL);
      final GameObject objectA = context.getGameObjectAt(0, 1, 0);
      context.setLayerIndex(objectA, 6);
   }

   @Test
   public void load_withSimple3x3Map_publishCustomEventOnCollision() throws TiledMapException {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, "event").addObject(1, 1, "player").build())
            .build();

      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      final AtomicBoolean called = new AtomicBoolean();
      final GameEventListener<TestEvent> gameEventEventListener = new GameEventListener<TestEvent>() {
         @Override
         public void onEvent(TestEvent event) {
            called.set(true);
         }
      };
      gameEventManager.register(gameEventEventListener, TestEvent.class);
      context.setEventFactory(new GameEventFactory() {
         @Override
         public GameEvent create(GameObject eventObject, GameObject producerObject) {
            return new TestEvent();
         }

         @Override
         public Object[] identifiers() {
            return new Object[]{"event"};
         }
      });
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player")) {
            // move player to event
            o.setPosition(0, 0);
         }
      }
      world.update(0f);
      assertThat(called.get()).isTrue();
   }

   @Test
   public void load_withSimple3x3Map_publishOnProducerCollisionOnly() throws TiledMapException {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder()
                  .addObject(0, 0, "event", "player", true)
                  .addObject(1, 1, "player")
                  .addObject(1, 1, "another_player").build())
            .build();

      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      final AtomicInteger calls = new AtomicInteger();
      final GameEventListener<TestEvent> gameEventEventListener = new GameEventListener<TestEvent>() {
         @Override
         public void onEvent(TestEvent event) {
            calls.addAndGet(1);
         }
      };
      gameEventManager.register(gameEventEventListener, TestEvent.class);
      context.setEventFactory(new GameEventFactory() {
         @Override
         public GameEvent create(GameObject eventObject, GameObject producerObject) {
            return new TestEvent();
         }

         @Override
         public Object[] identifiers() {
            return new Object[]{"event"};
         }
      });
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player") || o.getType().equals("another_player")) {
            // move player to event
            o.setPosition(0, 0);
         }
      }
      world.update(0f);
      assertThat(calls.get()).isEqualTo(1);
   }

   @Test
   public void load_withSimple3x3Map_republishOnSticky() throws TiledMapException {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder()
                  .addObject(0, 0, "event", null, true)
                  .addObject(1, 1, "player")
                  .addObject(1, 1, "another_player").build())
            .build();

      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      final AtomicInteger calls = new AtomicInteger();
      final GameEventListener<TestEvent> gameEventEventListener = new GameEventListener<TestEvent>() {
         @Override
         public void onEvent(TestEvent event) {
            calls.addAndGet(1);
         }
      };
      gameEventManager.register(gameEventEventListener, TestEvent.class);
      context.setEventFactory(new GameEventFactory() {
         @Override
         public GameEvent create(GameObject eventObject, GameObject producerObject) {
            return new TestEvent();
         }

         @Override
         public Object[] identifiers() {
            return new Object[]{"event"};
         }
      });
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player") || o.getType().equals("another_player")) {
            // move player to event
            o.setPosition(0, 0);
         }
      }
      world.update(0f);
      assertThat(calls.get()).isEqualTo(2);
   }

   @Test
   public void load_withSimple3x3Map_removeLastCollisionOnSimpleMove() {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, "player").build())
            .build();
      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      world.update(0f);
      assertThat(context.isCollision(0, 0, 0)).isTrue();
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player")) {
            o.setPosition(1, 0);
         }
      }
      world.update(0f);
      assertThat(context.isCollision(0, 0, 0)).isFalse();
   }

   @Test
   public void load_withSimple3x3Map_removeLastCollisionOnMultipleMoves() {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, "player").build())
            .build();
      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      world.update(0f);
      assertThat(context.isCollision(0, 0, 0)).isTrue();
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player")) {
            o.setPosition(1, 0);
            o.setPosition(0, 1);
         }
      }
      world.update(0f);
      assertThat(context.isCollision(0, 0, 0)).isFalse();
      assertThat(context.isCollision(0, 1, 0)).isTrue();
   }

   @Test
   public void load_withSimple3x3Map_shouldNotOverrideExistingCollision() {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder()
                  .addCell(0, 0)
                  .addCell(0, 1, true)
                  .addCell(1, 0)
                  .addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, "player").build())
            .build();
      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      world.update(0f);
      // Check for normal collision setup
      assertThat(context.isCollision(0, 0, 0)).isTrue();
      assertThat(context.isCollision(0, 1, 0)).isTrue();
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player")) {
            o.setPosition(0, 1);
         }
      }
      world.update(0f);
      // Verify collision has moved
      assertThat(context.isCollision(0, 0, 0)).isFalse();
      assertThat(context.isCollision(0, 1, 0)).isTrue();
      for (GameObject o : world.getObjects()) {
         if (o.getType().equals("player")) {
            o.setPosition(0, 0);
         }
      }
      world.update(0f);
      // Verify that everything is back to normal
      assertThat(context.isCollision(0, 0, 0)).isTrue();
      assertThat(context.isCollision(0, 1, 0)).isTrue();
   }

   @Test
   public void load_withSimple3x3Map_shouldOnlyUnloadRelevantObjects() {
      TiledMap map = new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder()
                  .addCell(0, 0)
                  .addCell(0, 1, true)
                  .addCell(1, 0)
                  .addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, "player").build())
            .build();
      TiledMapContext context = tiledMapManager.load(map, camera, TiledMapType.ORTHOGONAL);
      world.update(0f);
      GameObject remainer = world.addObject();
      assertThat(world.size()).isEqualTo(4);
      context.dispose();
      assertThat(world.getObjects()).containsExactly(remainer);
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
   private TiledMap createSimple3x3Map(boolean collision) {
      // Initialization
      final String typeA = "a";
      final String typeB = "b";
      final String typeC = "c";
      return new MockTiledMapBuilder(2, 2, 1)
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 1, typeA).addObject(1, 1, typeB).build())
            .addLayer(new MockTiledTileLayerBuilder().addCell(0, 0).addCell(0, 1).addCell(1, 0).addCell(1, 1)
                  .collision(collision).build())
            .addLayer(new MockObjectLayerBuilder().addObject(0, 0, typeC).build()).build();
   }

   private TiledMap createSimple3x3Map() {
      return createSimple3x3Map(false);
   }

   private class TestEvent implements GameEvent {

   }
}
