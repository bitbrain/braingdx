package de.bitbrain.braingdx.graphics.lighting;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerImpl.LightFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LightingManagerTest {

   @Mock
   private RayHandler rayHandler;

   @Mock
   private LightFactory lightFactory;

   @Mock
   PointLight pointLightMock;

   @InjectMocks
   private LightingManagerImpl lightingManager;

   @Before
   public void beforeTest() {
      when(lightFactory.newPointLight(any(RayHandler.class), anyInt(), any(Color.class), anyFloat(), anyFloat(), anyFloat())).thenReturn(pointLightMock);
      Application mockApp = mock(Application.class);
      Gdx.app = mockApp;
      doAnswer(new Answer<Void>() {
         @Override
         public Void answer(InvocationOnMock invocation) {
            ((Runnable)invocation.getArguments()[0]).run();
            return null;
         }
      }).when(mockApp).postRunnable(any(Runnable.class));
   }

   @Test
   public void testRemoveLight_Point() {
      lightingManager.addPointLight("pointlight", new Vector2(), 0f, Color.WHITE);
      lightingManager.removePointLight("pointlight");
      verify(pointLightMock, times(1)).remove();
   }

   @Test
   public void testClear() {
      lightingManager.addPointLight("", new Vector2(), 0f, Color.WHITE);
      lightingManager.clear();
      lightingManager.removePointLight("pointlight");
      verify(pointLightMock, never()).remove();
   }

}
