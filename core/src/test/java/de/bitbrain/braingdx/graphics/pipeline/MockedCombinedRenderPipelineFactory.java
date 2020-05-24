package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.graphics.BatchResolver;
import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedCombinedRenderPipelineFactory implements RenderPipelineFactory {

   @Override
   public InternalRenderPipeline create() {
      ShaderConfig config = mock(ShaderConfig.class);
      PostProcessor processorMock = mock(PostProcessor.class);
      FrameBufferFactory factory = mock(FrameBufferFactory.class);
      FrameBuffer buffer = mock(FrameBuffer.class);
      Texture mockTexture = mock(Texture.class);
      when(factory.create(Mockito.anyInt(), Mockito.anyInt())).thenReturn(buffer);
      when(buffer.getColorBufferTexture()).thenReturn(mockTexture);
      BatchResolver[] batchResolvers = new BatchResolver[]{
        mock(BatchResolver.class)
      };
      return new CombinedRenderPipeline(
            config,
            processorMock,
            factory,
            mock(SpriteBatch.class),
            mock(OrthographicCamera.class),
            batchResolvers
      );
   }

}
