package de.bitbrain.braingdx.graphics.shader;

import de.bitbrain.braingdx.util.ClassPathResolver;
import de.bitbrain.braingdx.util.PathResolver;

public class ShaderConfig {

   public PathResolver pathResolver = new ClassPathResolver();

   public String basePath = "postprocessing/shaders/";

}
