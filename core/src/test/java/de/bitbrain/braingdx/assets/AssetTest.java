package de.bitbrain.braingdx.assets;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class AssetTest {

   @Test(expected = RuntimeException.class)
   public void testReload_GdxNotLoaded() {
      Gdx.files = null;
      Asset.get("", Texture.class);
      Asset.reload();
   }


   @Test
   public void testReload() {
      Gdx.files = mock(Files.class);
      Asset.reload();
   }

   @Test(expected = GdxRuntimeException.class)
   public void testGet() {
      Gdx.files = mock(Files.class);
      Asset.get("", Texture.class);
   }

}