package de.bitbrain.braingdx.util.math;

import org.junit.Test;

import java.math.BigDecimal;

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

   @Test
   public void testScaling() {
      BigDecimalVector2 vector = new BigDecimalVector2();
      vector.set(10f, 0f);
      vector.scl(new BigDecimal("0.5"));
      assertThat(vector.len().floatValue()).isEqualTo(5f);
      vector.scl(new BigDecimal("2"));
      assertThat(vector.len().floatValue()).isEqualTo(10f);
   }

   @Test
   public void testAddition() {
      BigDecimalVector2 vectorA = new BigDecimalVector2();
      BigDecimalVector2 vectorB = new BigDecimalVector2();

      vectorA.set(15f, 10f);
      vectorB.set(20f, 28f);

      vectorA.add(vectorB);

      assertThat(vectorA.x.floatValue()).isEqualTo(35f);
      assertThat(vectorA.y.floatValue()).isEqualTo(38f);
      assertThat(vectorB.x.floatValue()).isEqualTo(20f);
      assertThat(vectorB.y.floatValue()).isEqualTo(28f);
   }

   @Test
   public void testSubstraction() {
      BigDecimalVector2 vectorA = new BigDecimalVector2();
      BigDecimalVector2 vectorB = new BigDecimalVector2();

      vectorA.set(15f, 10f);
      vectorB.set(20f, 28f);

      vectorA.sub(vectorB);

      assertThat(vectorA.x.floatValue()).isEqualTo(-5f);
      assertThat(vectorA.y.floatValue()).isEqualTo(-18f);
      assertThat(vectorB.x.floatValue()).isEqualTo(20f);
      assertThat(vectorB.y.floatValue()).isEqualTo(28f);
   }
}