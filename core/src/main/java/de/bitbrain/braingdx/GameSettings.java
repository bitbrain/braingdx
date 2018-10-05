package de.bitbrain.braingdx;

import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.graphics.GraphicsSettings;

public class GameSettings {

   public static final String ID = "braingdx.settings";

   private final GraphicsSettings graphicsSettings;

   public GameSettings(GameEventManager gameEventManager) {
      graphicsSettings = new GraphicsSettings(gameEventManager);
   }

   public GraphicsSettings getGraphics() {
      return graphicsSettings;
   }
}
