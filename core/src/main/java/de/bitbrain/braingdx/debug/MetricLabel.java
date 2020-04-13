package de.bitbrain.braingdx.debug;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class MetricLabel extends Label {

   private final DebugMetric debugMetric;

   public MetricLabel(DebugMetric debugMetric, LabelStyle labelStyle) {
      super("", labelStyle);
      this.debugMetric = debugMetric;
   }

   @Override
   public void act(float delta) {
      setText(debugMetric.getCurrentValue());
      super.act(delta);
   }
}
