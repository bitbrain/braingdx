package de.bitbrain.braingdx.tmx;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class IsometricPositionTranslatorTest {

   private static final float CELL_WIDTH = 100f;
   private static final float CELL_HEIGHT = 80f;

   @Mock
   private State state;

   private IsometricPositionTranslator impl;

   @Before
   public void setup() {
      when(state.getCellWidth()).thenReturn(CELL_WIDTH);
      when(state.getCellHeight()).thenReturn(CELL_HEIGHT);
      impl = new IsometricPositionTranslator(state);
   }

   @Test
   public void testToWorldX() {
   }

   @Test
   public void testToWorldY() {
   }

   @Test
   public void testToWorldX1() {
   }

   @Test
   public void testToWorldY1() {
   }

   @Test
   public void testToMapX() {
   }

   @Test
   public void testToMapY() {
   }

   @Test
   public void testToIndexX() {
   }

   @Test
   public void testToIndexY() {
   }
}