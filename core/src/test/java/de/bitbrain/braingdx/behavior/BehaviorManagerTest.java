package de.bitbrain.braingdx.behavior;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.bitbrain.braingdx.world.GameObject;

@RunWith(MockitoJUnitRunner.class)
public class BehaviorManagerTest {

    private BehaviorManager manager;

    @Before
    public void beforeTest() {
	manager = new BehaviorManager();
    }

    @Test
    public void testApplyLocalBehavior() {
	Behavior mockBehavior = Mockito.mock(Behavior.class);
	GameObject mockObject = Mockito.mock(GameObject.class);
	manager.apply(mockBehavior, mockObject);
	manager.updateLocally(mockObject, 0f);
	Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onAttach(mockObject);
	Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObject, 0f);
    }

    @Test
    public void testApplyGlobalBehavior() {
	Behavior mockBehavior = Mockito.mock(Behavior.class);
	GameObject mockObjectA = Mockito.mock(GameObject.class);
	GameObject mockObjectB = Mockito.mock(GameObject.class);
	manager.apply(mockBehavior);
	manager.updateGlobally(mockObjectA, 0f);
	manager.updateGlobally(mockObjectB, 0f);
	Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObjectA, 0f);
	Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).update(mockObjectB, 0f);
    }

    @Test
    public void testRemoveBehavior() {
	Behavior mockBehavior = Mockito.mock(Behavior.class);
	GameObject mockObject = Mockito.mock(GameObject.class);
	manager.apply(mockBehavior, mockObject);
	manager.remove(mockObject);
	manager.updateLocally(mockObject, 0f);
	manager.updateGlobally(mockObject, 0f);
	Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.calls(1)).onDetach(mockObject);
	Mockito.inOrder(mockBehavior).verify(mockBehavior, Mockito.never()).update(mockObject, 0f);
    }
}
