package de.bitbrain.braingdx.tmx.events;

import box2dLight.Light;
import com.badlogic.gdx.graphics.Color;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.tmx.TiledMapEvents;
import de.bitbrain.braingdx.world.GameObject;

public class TmxLightingConfigurer implements GameEventListener<TiledMapEvents.OnLoadGameObjectEvent> {

   private static final String LIGHTING_COLOR = "lighting.color";
   private static final String LIGHTING_RANGE = "lighting.range";

   private static final Color DEFAULT_LIGHTING_COLOR = Color.WHITE;
   private static final float DEFAULT_LIGHTING_RANGE = 256f;

   private final LightingManager lightingManager;

   public TmxLightingConfigurer(LightingManager lightingManager) {
      this.lightingManager = lightingManager;
   }

   @Override
   public void onEvent(TiledMapEvents.OnLoadGameObjectEvent event) {
      GameObject object = event.getObject();
      if (object.hasAttribute(LIGHTING_COLOR) || object.hasAttribute(LIGHTING_RANGE)) {
         final Color color = object.getAttribute(LIGHTING_COLOR, DEFAULT_LIGHTING_COLOR);
         final float range = object.getAttribute(LIGHTING_RANGE, DEFAULT_LIGHTING_RANGE);
         Light light = lightingManager.createPointLight(range, color);
         lightingManager.attach(light, object, true);
      }
   }
}
