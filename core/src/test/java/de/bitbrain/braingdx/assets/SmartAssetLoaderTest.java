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

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import de.bitbrain.braingdx.assets.SmartAssetLoader.SmartAssetLoaderConfiguration;

/**
 * Test for {@link SmartAssetLoader}.
 */
public class SmartAssetLoaderTest {
	
	private Map<String, Class<?>> assets;
	
	@Before
	public void beforeTest() {		
		assets = new HashMap<String, Class<?>>();
		Gdx.app = Mockito.mock(Application.class);
	}
	
	@Test
	public void testDefaultTypes() {
		SmartAssetLoader loader = new SmartAssetLoader(SampleValidAssets.class);
		loader.put(assets);
		Assertions.assertThat(assets).hasSize(12);
	}
	
	@Test
	public void testDefaultTypesWithAsdfTypes() {
		SmartAssetLoaderConfiguration config = SmartAssetLoader.defaultConfiguration();
		config.getClassMapping().put("AsdfType", Object.class);
		SmartAssetLoader loader = new SmartAssetLoader(CustomSampleValidAssets.class, config);
		loader.put(assets);
		Assertions.assertThat(assets).hasSize(2);
	}
}
