package de.bitbrain.braingdx.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Produces randomized strings depending on the pool and text provided.
 *
 * @since 0.5.9
 */
public class StringRandomizer {

   private final String text;
   private final int nativeLength;
   private final String pool;
   private final Random random;

   private float factor = 0.5f;

   /**
    * Initialises this object with a static seed.
    *
    * @param text the text to randomize
    * @param pool the pool to retrieve masks from
    * @param seed a static seed to generate randomness from
    */
   public StringRandomizer(String text, String pool, String seed) {
      this(text, pool, new Random(seed.hashCode()));
   }

   /**
    * Initialises this object with a dynamic seed.
    *
    * @param text the text to randomize
    * @param pool the pool to retrieve masks from
    */
   public StringRandomizer(String text, String pool) {
      this(text, pool, new Random());
   }

   private StringRandomizer(String text, String pool, Random random) {
      this.text = text;
      this.pool = pool;
      this.random = random;
      this.nativeLength = computeNativeLength(text);
   }

   public void setFactor(float factor) {
      this.factor = max(min(factor, 1f), 0);
   }

   public float getFactor() {
      return this.factor;
   }

   /**
    * Returns a randomized string from the original string provided.
    */
   public String randomize() {
      StringBuilder builder = new StringBuilder(text);
      int charactersToRandomize = (int) (factor * nativeLength);
      Set<Integer> visitedIndices = new HashSet<Integer>();
      for (int i = 0; i < charactersToRandomize; ++i) {
         String replacement = getRandomCharacter(pool);
         replaceRandomCharacter(builder, replacement, visitedIndices);
      }
      return builder.toString();
   }

   /* replaces a random character from the builder with a given character.
    * if the builder only contains whitespaces or is blank, nothing gets replaced.
    * only non-whitespace characters are eligible for replacement.
    */
   private void replaceRandomCharacter(StringBuilder builder, String character, Set<Integer> visitedIndices) {
      int index = getRandomTextIndex();
      while (visitedIndices.contains(index)) {
         index = getRandomTextIndex();
      }
      visitedIndices.add(index);
      builder.replace(index, index + 1, character);
   }

   /* returns a random text index which is not blank */
   private int getRandomTextIndex() {
      if (text.trim().isEmpty()) {
         return -1;
      }
      int maxRetries = text.length();
      while (maxRetries-- > 0) {
         int randomIndex = getRandomIndex(text);
         if (text.charAt(randomIndex) != ' ') {
            return randomIndex;
         }
      }
      return -1;
   }

   /* Computes the length of a string excluding whitespaces */
   private int computeNativeLength(String text) {
      int length = 0;
      for (int i = 0; i < text.length(); ++i) {
         if (text.charAt(i) != ' ') {
            length++;
         }
      }
      return length;
   }

   /* returns a random index on the string provided */
   private int getRandomIndex(String string) {
      return (int) (random.nextFloat() * string.length());
   }

   /* returns a random character of the string provided */
   private String getRandomCharacter(String string) {
      return string.charAt(getRandomIndex(string)) + "";
   }
}
