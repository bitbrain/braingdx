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

package de.bitbrain.braingdx.graphics.lighting;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import box2dLight.PointLight;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Lighting behavior to attach lights to game objects
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class PointLightBehavior extends BehaviorAdapter {

   private static int LIGHT_COUNT = 0;

   private PointLight light;

   private String lightId;

   private LightingManager lightingManager;

   private Color color;

   private float distance;

   public PointLightBehavior(Color color, float distance, LightingManager lightingManager) {
      this.color = color;
      this.distance = distance;
      this.lightingManager = lightingManager;
      createLight();
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      light.setPosition(source.getLeft() + source.getOffset().x + source.getWidth() / 2f,
            source.getTop() + source.getOffset().y + source.getHeight() / 2f);
   }

   @Override
   public void onDetach(GameObject source) {
      lightingManager.removePointLight(lightId);
   }

   private void createLight() {
      lightId = generateId();
      light = lightingManager.addPointLight(lightId, Vector2.Zero, distance, color);
   }

   private String generateId() {
      return getClass().getCanonicalName() + "_" + LIGHT_COUNT++;
   }

}
