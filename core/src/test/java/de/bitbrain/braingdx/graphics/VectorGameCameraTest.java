package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VectorGameCameraTest {

   private static final float WORLD_WIDTH = 500;
   private static final float WORLD_HEIGHT = 800;

   @Mock
   private GameWorld gameWorld;

   @Mock
   private OrthographicCamera orthographicCamera;

   @Mock
   private GameWorld.WorldBounds bounds;

   @InjectMocks
   private VectorGameCamera camera;

   @Before
   public void setup() {
      when(gameWorld.getBounds()).thenReturn(bounds);
      setBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
   }

   @Test
   public void testInBounds() {

   }

   private void setBounds(float x, float y, float width, float height) {
      when(bounds.getWorldOffsetX()).thenReturn(x);
      when(bounds.getWorldOffsetY()).thenReturn(y);
      when(bounds.getWorldWidth()).thenReturn(width);
      when(bounds.getWorldHeight()).thenReturn(height);
   }
}