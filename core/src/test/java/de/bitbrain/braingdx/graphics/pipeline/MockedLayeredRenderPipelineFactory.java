package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.postprocessing.PostProcessor;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedLayeredRenderPipelineFactory implements RenderPipelineFactory {

   @Override
   public RenderPipeline create() {
      ShaderConfig config = mock(ShaderConfig.class);
      PostProcessor processorMock = mock(PostProcessor.class);
      FrameBufferFactory factory = mock(FrameBufferFactory.class);
      FrameBuffer buffer = mock(FrameBuffer.class);
      when(factory.create(Mockito.anyInt(), Mockito.anyInt())).thenReturn(buffer);
      return new LayeredRenderPipeline(config, processorMock, factory);
   }

}
