
package de.bitbrain.braingdx.postprocessing.effects;

import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;

public abstract class Antialiasing extends PostProcessorEffect {

   public abstract void setViewportSize(int width, int height);
}
