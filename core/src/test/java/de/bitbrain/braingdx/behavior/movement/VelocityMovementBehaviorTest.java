package de.bitbrain.braingdx.behavior.movement;

import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.world.GameObject;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VelocityMovementBehaviorTest {

   @Test
   public void testIsMoving() {
      VelocityMovementBehavior behavior = new VelocityMovementBehavior();
      assertThat(behavior.isMoving()).isFalse();
      behavior.move(new Vector2(1.5f, 1.1f));
      assertThat(behavior.isMoving()).isTrue();
   }

   @Test
   public void testVelocity() {
      GameObject obj = new GameObject();
      VelocityMovementBehavior behavior = new VelocityMovementBehavior();
      behavior.move(new Vector2(1.5f, 1.1f));
      behavior.update(obj, 1f);
      assertThat(obj.getLeft()).isEqualTo(1.5f);
      assertThat(obj.getTop()).isEqualTo(1.1f);
      behavior.update(obj, 1f);
      assertThat(obj.getLeft()).isEqualTo(3f);
      assertThat(obj.getTop()).isEqualTo(2.2f);
   }
}