package de.bitbrain.braingdx.util.math;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BigDecimalVector2Test {

   @Test
   public void testAngle_90() {
      BigDecimalVector2 vector = new BigDecimalVector2();
      vector.set(0f, 1f);
      assertThat(vector.angle()).isEqualTo(90f);
   }

   @Test
   public void testAngle_0() {
      BigDecimalVector2 vector = new BigDecimalVector2();
      vector.set(1f, 0f);
      assertThat(vector.angle()).isEqualTo(0f);
   }

   @Test
   public void testAngle_180() {
      BigDecimalVector2 vector = new BigDecimalVector2();
      vector.set(-1f, 0f);
      assertThat(vector.angle()).isEqualTo(180f);
   }

   @Test
   public void testAngle_275() {
      BigDecimalVector2 vector = new BigDecimalVector2();
      vector.set(-0f, -1f);
      assertThat(vector.angle()).isEqualTo(270f);
   }

   @Test
   public void testLength() {
      BigDecimalVector2 vector = new BigDecimalVector2();
      vector.set(0f, 10f);
      assertThat(vector.len().floatValue()).isEqualTo(10f);
   }
}