package de.bitbrain.braingdx.utils;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;

public class GdxUtils {

   public static void mockApplicationContext() {
      Gdx.app = mock(Application.class);
      Mockito.doAnswer(new Answer() {
         @Override
         public Object answer(InvocationOnMock invocation) {
            Runnable runnable = (Runnable) invocation.getArguments()[0];
            runnable.run();
            return null;
         }
      }).when(Gdx.app).postRunnable(Mockito.any(Runnable.class));
   }
}
