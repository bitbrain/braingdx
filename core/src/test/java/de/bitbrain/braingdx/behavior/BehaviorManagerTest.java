package de.bitbrain.braingdx.behavior;

import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.util.GdxUtils;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.braingdx.world.SimpleWorldBounds;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

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
      world.setBounds(new SimpleWorldBounds(5000, 5000));
      manager = new BehaviorManager(world);
      world.addListener(new BehaviorManagerAdapter(manager));
      GdxUtils.mockApplicationContext();
   }

   @Test
   public void testUpdateWithLocalBehavior() {
      UpdateableBehavior mockBehavior = mock(UpdateableBehavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.update(1f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(1f);
      world.remove(mockObject);
      manager.update(2f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(2f);
   }

   @Test
   public void testUpdateWithGlobalBehavior() {
      UpdateableBehavior mockBehavior = mock(UpdateableBehavior.class);
      manager.apply(mockBehavior);
      manager.update(1f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(1f);
      manager.remove(mockBehavior);
      manager.update(2f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(2f);
   }

   @Test
   public void testApplyLocalBehavior() {
      Behavior mockBehavior = mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.updateLocally(mockObject, 0f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onAttach(mockObject);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObject, 0f);
   }

   @Test
   public void testApplyGlobalBehavior() {
      Behavior mockBehavior = mock(Behavior.class);
      GameObject mockObjectA = world.addObject();
      GameObject mockObjectB = world.addObject();
      manager.apply(mockBehavior);
      manager.updateGlobally(mockObjectA, 0f);
      manager.updateGlobally(mockObjectB, 0f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObjectA, 0f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObjectB, 0f);
   }

   @Test
   public void testRemoveBehavior() {
      Behavior mockBehavior = mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.remove(mockObject, mockBehavior);
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }

   @Test
   public void testRemoveBehavior_GameObjectOnly() {
      Behavior mockBehavior = mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.remove(mockObject);
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onAttach(mockObject);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }

   @Test
   public void testClear() {
      Behavior mockBehavior = mock(Behavior.class);
      GameObject mockObject = world.addObject();
      manager.apply(mockBehavior, mockObject);
      manager.clear();
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }

   @Test
   public void testStatusChange_Updateable_ToDisabled_AndEnabled() {
      GameCamera camera = mock(GameCamera.class);
      when(camera.getScaledCameraWidth()).thenReturn(200f);
      when(camera.getScaledCameraHeight()).thenReturn(200f);
      world.setCamera(camera);
      Behavior mockBehavior = mock(Behavior.class);
      for (int i = 0; i < 60; ++i) {
         GameObject mockObject = world.addObject();
         mockObject.setPosition(i * 10, i * 10);
         mockObject.setDimensions(5, 5);
         manager.apply(mockBehavior, mockObject);
      }
      world.update(1f);
      when(camera.getLeft()).thenReturn(1000f);
      when(camera.getTop()).thenReturn(1000f);
      world.update(1f);
      world.update(1f);
      for (GameObject o : world.getObjects()) {
         inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onStatusChange(o, false);
         o.setPosition(1000f, 1000f);
      }
      world.update(1f);
      world.update(1f);
      for (GameObject o : world.getObjects()) {
         inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onStatusChange(o, true);
      }
   }
}
