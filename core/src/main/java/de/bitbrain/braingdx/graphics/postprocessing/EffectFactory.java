package de.bitbrain.braingdx.graphics.postprocessing;

import de.bitbrain.braingdx.graphics.GraphicsSettings;

interface EffectFactory<T> {
   T create(T original, int newWidth, int newHeight, GraphicsSettings settings);
}
