package de.bitbrain.braingdx.world;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.badlogic.gdx.graphics.OrthographicCamera;

@RunWith(MockitoJUnitRunner.class)
public class GameWorldTest {

   @Mock
   private OrthographicCamera camera;

   private GameWorld world;

   @Before
   public void beforeTest() {
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
}
