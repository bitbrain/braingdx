package de.bitbrain.braingdx.debug;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class BrainGdxDebug {

   private static Label.LabelStyle debugLabelStyle;

   public static void setLabelStyle(Label.LabelStyle debugLabelStyle) {
      BrainGdxDebug.debugLabelStyle = debugLabelStyle;
   }

   public static Label.LabelStyle getDebugLabelStyle() {
      return debugLabelStyle;
   }
}
