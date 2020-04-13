package de.bitbrain.braingdx.debug;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import java.util.*;

public class DebugPanel extends Table {

   private final Map<String, DebugMetric> metrics = new HashMap<String, DebugMetric>();
   private final List<String> labelsToAdd = new ArrayList<String>();

   public DebugPanel() {
      setFillParent(true);
   }

   public void addMetric(String name, DebugMetric debugMetric) {
      metrics.put(name, debugMetric);
      labelsToAdd.add(name);
   }

   @Override
   public void act(float delta) {
      super.act(delta);
      if (BrainGdxDebug.getDebugLabelStyle() != null && !labelsToAdd.isEmpty()) {
         for (String label : labelsToAdd) {
            top().left().add(new Label(label, BrainGdxDebug.getDebugLabelStyle())).align(Align.left).padRight(50);
            left().add(new MetricLabel(metrics.get(label), BrainGdxDebug.getDebugLabelStyle())).align(Align.left).row();
         }
         if (metrics.size() != 0) {
            labelsToAdd.clear();
         }
      }
   }
}
