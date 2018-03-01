package de.bitbrain.braingdx.behavior;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import static org.mockito.Mockito.mock;

import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

@RunWith(MockitoJUnitRunner.class)
public class BehaviorManagerTest {

   private BehaviorManager manager;
   private GameWorld world;

   @Before
   public void beforeTest() {
      world = new GameWorld(mock(OrthographicCamera.class));
      manager = new BehaviorManager(world);
      Gdx.app = mock(Application.class);
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
      manager.remove(mockObject);
      manager.updateLocally(mockObject, 0f);
      manager.updateGlobally(mockObject, 0f);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
      Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
   }
}
