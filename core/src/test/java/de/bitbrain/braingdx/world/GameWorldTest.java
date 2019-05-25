package de.bitbrain.braingdx.world;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.bitbrain.braingdx.util.GdxUtils;
import de.bitbrain.braingdx.util.Mutator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class GameWorldTest {

   @Mock
   private OrthographicCamera camera;

   private GameWorld world;

   @Before
   public void beforeTest() {
      GdxUtils.mockApplicationContext();
      world = new GameWorld(camera);
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
      List<GameObject> sorted = world.getObjects();
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
      List<GameObject> sorted = world.getObjects(new Comparator<GameObject>() {

         @Override
         public int compare(GameObject o1, GameObject o2) {
            return (int) (o1.getLeft() - o2.getLeft());
         }
      });
      assertThat(sorted).containsExactly(object1, object3, object2);
   }
}
