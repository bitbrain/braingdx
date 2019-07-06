package de.bitbrain.braingdx.behavior.movement;

import de.bitbrain.braingdx.world.GameObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RandomVelocityMovementBehaviorTest {

   @Captor
   private ArgumentCaptor<Float> xCaptor;

   @Captor
   private ArgumentCaptor<Float> yCaptor;

   @Test
   public void testRandomVelocity() {
      GameObject obj = mock(GameObject.class);
      RandomVelocityMovementBehavior behavior = new RandomVelocityMovementBehavior();
      behavior.update(obj, 1f);
      verify(obj).move(xCaptor.capture(), yCaptor.capture());
      assertNotSame(xCaptor.getValue(), yCaptor.getValue());
   }

}