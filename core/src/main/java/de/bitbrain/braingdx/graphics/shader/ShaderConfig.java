package de.bitbrain.braingdx.graphics.shader;

import com.bitfire.utils.ClassPathResolver;
import com.bitfire.utils.PathResolver;

public class ShaderConfig {
    
    public PathResolver pathResolver = new ClassPathResolver();

    public String basePath = "postprocessing/shaders/";

}
