package de.bitbrain.braingdx.assets;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class EmptyGameAssetLoaderTest {

   @Test
   public void testPutEmpty() {
      EmptyGameAssetLoader loader = new EmptyGameAssetLoader();
      Map<String, Class<?>> map = new HashMap<String, Class<?>>();
      loader.put(map);
      assertThat(map).isEmpty();
   }
}