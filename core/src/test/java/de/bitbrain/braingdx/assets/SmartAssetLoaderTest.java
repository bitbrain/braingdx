/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bitbrain.braingdx.assets;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Test for {@link SmartAssetLoader}.
 */
public class SmartAssetLoaderTest {

   private Map<String, Class<?>> assets;

   @Before
   public void beforeTest() {
      assets = new HashMap<String, Class<?>>();
      Application application = mock(Application.class);
      doAnswer(new Answer<Void>() {
         @Override
         public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
            String tag = (String) invocationOnMock.getArguments()[0];
            String message = (String) invocationOnMock.getArguments()[1];
            throw new RuntimeException("Unexpected error log found: tag=" + tag + ", message=" + message);
         }
      }).when(application).error(anyString(), anyString());
      Gdx.app = application;

   }

   @Test
   public void testDefaultTypes() {
      SmartAssetLoader loader = new SmartAssetLoader(SampleValidAssets.class);
      loader.put(assets);
      assertThat(assets).hasSize(12);
   }

   @Test
   public void testProducesErrors_WithInvalidAssets() {
      Application application = mock(Application.class);
      doNothing().when(application).error(anyString(), anyString());
      Gdx.app = application;
      SmartAssetLoader loader = new SmartAssetLoader(SampleInvalidAssets.class);
      loader.put(assets);
      assertThat(assets).hasSize(10);
      verify(application, times(3)).error(anyString(), anyString());
   }
}
