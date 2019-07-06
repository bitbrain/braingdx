package de.bitbrain.braingdx.behavior.movement;

import org.junit.Test;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThat;

public class OrientationTest {

   @Test
   public void testDirections() {
      for (Orientation orientation : Orientation.values()) {
         assertThat(abs(orientation.getXFactor() + orientation.getYFactor())).isEqualTo(1);
      }
   }
}