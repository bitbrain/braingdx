package de.bitbrain.braingdx.util;

import org.junit.Test;

import static de.bitbrain.braingdx.util.BitUtils.haveSameSign;
import static org.assertj.core.api.Assertions.assertThat;

public class BitUtilsTest {

   @Test
   public void testSameSign() {
      assertThat(haveSameSign(-1, -5)).isTrue();
   }

   @Test
   public void testOppositeSignA() {
      assertThat(haveSameSign(1, -5)).isFalse();
   }

   @Test
   public void testNotSameSignB() {
      assertThat(haveSameSign(-1, 5)).isFalse();
   }
}