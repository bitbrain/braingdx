package de.bitbrain.braingdx.assets.loader;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParticleLoaderTest {

   private ParticleLoader loader = new ParticleLoader(mock(FileHandleResolver.class));

   @Test
   public void emptyDependencies() {
      assertThat(loader.getDependencies(null, null, null)).isNull();
   }

   @Test(expected = GdxRuntimeException.class)
   public void testLoad() {
      Gdx.files = mock(Files.class);
      FileHandle handle = mock(FileHandle.class);
      InputStream stream = mock(InputStream.class);
      when(handle.read()).thenReturn(stream);
      assertThat(
            loader.load(new AssetManager(),
                  "asdf",
                  handle,
                  mock(ParticleLoader.ParticleParameter.class))
      ).isNotNull();
   }
}