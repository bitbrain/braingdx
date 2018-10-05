package de.bitbrain.braingdx.graphics.postprocessing.effects;

import de.bitbrain.braingdx.graphics.postprocessing.PostProcessorEffect;

public abstract class Antialiasing extends PostProcessorEffect {

   public abstract void setViewportSize(int width, int height);
}
