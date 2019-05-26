package de.bitbrain.braingdx.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringRandomizerTest {

   @Test
   public void testSetFactor_UpperBound() {
      StringRandomizer randomizer = new StringRandomizer("", "");
      randomizer.setFactor(3f);
      assertThat(randomizer.getFactor()).isEqualTo(1f);
   }

   @Test
   public void testSetFactor_LowerBound() {
      StringRandomizer randomizer = new StringRandomizer("", "");
      randomizer.setFactor(-1f);
      assertThat(randomizer.getFactor()).isEqualTo(0f);
   }

   @Test
   public void testRandomize() {
      StringRandomizer randomizer = new StringRandomizer("asdf", "0");
      String randomized = randomizer.randomize();
      assertThat(randomized).isNotEqualTo("asdf");
   }

   @Test
   public void testRandomize_FixedSeed() {
      StringRandomizer randomizer = new StringRandomizer("asdfasdf", "01", "asdf");
      String randomized = randomizer.randomize();
      assertThat(randomized).isEqualTo("01df0sd0");
   }

   @Test
   public void testRandomize_Blank() {
      StringRandomizer randomizer = new StringRandomizer(" ", "0");
      String randomized = randomizer.randomize();
      assertThat(randomized).isEqualTo(" ");
   }
}