package de.bitbrain.braingdx.graphics.lighting;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.graphics.lighting.LightingManager.LightFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LightingManagerTest {

   @Mock
   private RayHandler rayHandler;

   @Mock
   private OrthographicCamera camera;

   @Mock
   private LightFactory lightFactory;

   @InjectMocks
   private LightingManager lightingManager;

   @Before
   public void beforeTest() {
      PointLight pointLightMock = mock(PointLight.class);
      when(lightFactory.newPointLight(any(RayHandler.class), anyInt(), any(Color.class), anyFloat(), anyFloat(), anyFloat())).thenReturn(pointLightMock);
   }

   @Test
   public void testRemoveLight_Point() {
      lightingManager.addPointLight("pointlight", new Vector2(), 0f, Color.WHITE);
      assertTrue(lightingManager.removePointLight("pointlight"));
   }

   @Test
   public void testClear() {
      lightingManager.addPointLight("", new Vector2(), 0f, Color.WHITE);
      lightingManager.clear();
      assertFalse(lightingManager.removePointLight("pointlight"));
   }

}
