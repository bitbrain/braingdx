package de.bitbrain.braingdx.behavior;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.bitbrain.braingdx.util.GdxUtils;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class BehaviorManagerTest {

   private class UpdateableBehavior extends BehaviorAdapter implements Updateable {

      @Override
      public void update(float delta) {

      }
   }

   private BehaviorManager manager;
   private GameWorld world;

   @Before
   public void beforeTest() {
      world = new GameWorld();
      manager = new BehaviorManager(world);
      world.addListener(new BehaviorManagerAdapter(manager));
      GdxUtils.mockApplicationContext();
   }

   @Test
   public void testUpdateWithLocalBehavior() {
      UpdateableBehavior mockBehavior = Mockito.mock(UpdateableBehavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.update(1f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(1f);
      world.remove(mockObject);
      manager.update(2f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(2f);
   }

   @Test
   public void testUpdateWithGlobalBehavior() {
      UpdateableBehavior mockBehavior = Mockito.mock(UpdateableBehavior.class);
      manager.apply(mockBehavior);
      manager.update(1f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(1f);
      manager.remove(mockBehavior);
      manager.update(2f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(2f);
   }

   @Test
   public void testApplyLocalBehavior() {
      Behavior mockBehavior = Mockito.mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.updateLocally(mockObject, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onAttach(mockObject);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObject, 0f);
   }

   @Test
   public void testApplyGlobalBehavior() {
      Behavior mockBehavior = Mockito.mock(Behavior.class);
      GameObject mockObjectA = world.addObject();
      GameObject mockObjectB = world.addObject();
      manager.apply(mockBehavior);
      manager.updateGlobally(mockObjectA, 0f);
      manager.updateGlobally(mockObjectB, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObjectA, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObjectB, 0f);
   }

   @Test
   public void testRemoveBehavior() {
      Behavior mockBehavior = Mockito.mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.remove(mockObject, mockBehavior);
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }

   @Test
   public void testRemoveBehavior_GameObjectOnly() {
      Behavior mockBehavior = Mockito.mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.remove(mockObject);
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onAttach(mockObject);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }

   @Test
   public void testClear() {
      Behavior mockBehavior = Mockito.mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.clear();
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }
}
