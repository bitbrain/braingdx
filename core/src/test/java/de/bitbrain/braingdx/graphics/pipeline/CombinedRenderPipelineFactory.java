package de.bitbrain.braingdx.graphics.pipeline;

public class CombinedRenderPipelineFactory implements RenderPipelineFactory {

    @Override
    public RenderPipeline create() {
	return new CombinedRenderPipeline();
    }

}
