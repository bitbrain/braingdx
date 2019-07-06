package de.bitbrain.braingdx.assets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class SharedAssetManagerTest {

   @Test(expected = RuntimeException.class)
   public void testReload_GdxNotLoaded() {
      Gdx.files = null;
      SharedAssetManager.getInstance();
      SharedAssetManager.reload();
   }


   @Test
   public void testReload() {
      Gdx.files = mock(Files.class);
      SharedAssetManager.reload();
   }

   @Test
   public void testGetInstance() {
      Gdx.files = mock(Files.class);
      SharedAssetManager.getInstance();
   }

}