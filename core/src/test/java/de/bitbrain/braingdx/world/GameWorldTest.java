package de.bitbrain.braingdx.world;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.math.QuadTree;
import de.bitbrain.braingdx.util.GdxUtils;
import de.bitbrain.braingdx.util.Mutator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameWorldTest {

   private GameWorld world;

   @Before
   public void beforeTest() {
      GdxUtils.mockApplicationContext();
      world = new GameWorld();
      GameCamera camera = mock(GameCamera.class);
      world.setCamera(camera);
      setViewport(camera, 0, 0, 100, 100);
   }

   @Test
   public void testSize() {
      final int size = 5;
      for (int i = 0; i < size; ++i) {
         world.addObject();
      }
      assertThat(world.size()).isEqualTo(size);
   }

   @Test
   public void testAddObject() {
      GameObject object = world.addObject();
      assertThat(object).isNotNull();
      assertThat(world.size()).isEqualTo(1);
      assertThat(object.getId()).isNotNull().isNotEmpty();
   }

   @Test
   public void testAddObjectWithCustomId() {
      String customId = "customId";
      GameObject object = world.addObject(new GameObjectIdMutator(customId));
      assertThat(object).isNotNull();
      assertThat(world.size()).isEqualTo(1);
      assertThat(object.getId()).isEqualTo(customId);
   }

   @Test
   public void testRemoveObject() {
      GameObject object = world.addObject();
      world.remove(object);
      world.update(0f);
      assertThat(world.size()).isEqualTo(0);
   }

   @Test
   public void testRemoveObjectWithCustomId() {
      GameObject object = world.addObject(new GameObjectIdMutator("custom-id"));
      world.remove(object);
      world.update(0f);
      assertThat(world.size()).isEqualTo(0);
   }

   @Test
   public void testUpdateWithListener() {
      GameObject object = world.addObject();
      FakeIdSupplier fakeIdSupplier = new FakeIdSupplier();
      world.addListener(fakeIdSupplier);
      world.update(0f);
      assertThat(object.getId()).isEqualTo(fakeIdSupplier.getCurrentId());
   }

   @Test
   public void testGetObjects() {
      GameObject object1 = world.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setLeft(10);
         }
      });
      GameObject object2 = world.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setLeft(30);
         }
      });
      GameObject object3 = world.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setLeft(20);
         }
      });
      Array<GameObject> sorted = world.getObjects();
      assertThat(sorted).containsExactly(object1, object2, object3);
   }

   @Test
   public void testGetObjectsSorted() {
      GameObject object1 = world.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setLeft(10);
         }
      });
      GameObject object2 = world.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setLeft(30);
         }
      });
      GameObject object3 = world.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setLeft(20);
         }
      });
      Array<GameObject> sorted = world.getObjects(new Comparator<GameObject>() {

         @Override
         public int compare(GameObject o1, GameObject o2) {
            return (int) (o1.getLeft() - o2.getLeft());
         }
      });
      assertThat(sorted).containsExactly(object1, object3, object2);
   }

   @Test
   public void testAddToGroup() {
      GameObject object1 = world.addObject("asdf");
      GameObject object2 = world.addObject("asdf");
      assertThat(world.getGroup("asdf")).containsExactly(object1, object2);
   }

   @Test
   public void testRemoveFromGroup() {
      GameObject object1 = world.addObject("asdf");
      GameObject object2 = world.addObject("asdf");
      world.remove(object1);
      assertThat(world.getGroup("asdf")).containsExactly(object2);
   }

   @Test
   public void testClearGroup() {
      world.addObject("asdf");
      world.addObject("asdf");
      GameObject object3 = world.addObject("asdf2");
      GameObject object4 = world.addObject("asdf2");
      world.clearGroup("asdf");
      assertThat(world.getGroup("asdf")).isEmpty();
      assertThat(world.getObjects()).containsExactlyInAnyOrder(object3, object4);
   }

   @Test
   public void shouldReturnUpdateable() {
      GameObject object = world.addObject("asdf2");
      object.setDimensions(2, 2);
      GameCamera gameCamera = mock(GameCamera.class);
      setViewport(gameCamera, 0, 0, 10, 10);
      world.setCamera(gameCamera);
      world.setBounds(new SimpleWorldBounds(100, 100));
      world.update(1f);
      assertThat(world.getObjects(null, true)).containsExactly(object);
   }

   @Test
   public void shouldNotReturnUpdateable() {
      world = new GameWorld(10);
      for (int i = 0; i < 60; ++i) {
         GameObject farAway = world.addObject("asdf2");
         farAway.setPosition(500 + i * 2f, 500 + i * 2f);
         farAway.setDimensions(2, 2);
      }
      GameCamera gameCamera = mock(GameCamera.class);
      setViewport(gameCamera, 10, 10, 50, 50);
      world.setCamera(gameCamera);
      world.setBounds(new SimpleWorldBounds(1000, 1000));
      world.update(1f);
      assertThat(world.getObjects(null, true).size).isEqualTo(1);
      assertThat(world.getObjects(null, false).size).isEqualTo(60);
   }

   private void setViewport(GameCamera cameraMock, float x, float y, float width, float height) {
      when(cameraMock.getLeft()).thenReturn(x);
      when(cameraMock.getTop()).thenReturn(y);
      when(cameraMock.getScaledCameraHeight()).thenReturn(x);
      when(cameraMock.getScaledCameraHeight()).thenReturn(y);
   }
}
