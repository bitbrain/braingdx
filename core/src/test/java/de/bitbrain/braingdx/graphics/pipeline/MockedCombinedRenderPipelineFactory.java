package de.bitbrain.braingdx.graphics.pipeline;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.postprocessing.PostProcessor;

public class MockedCombinedRenderPipelineFactory implements RenderPipelineFactory {

   @Override
   public RenderPipeline create() {
      ShaderConfig config = mock(ShaderConfig.class);
      PostProcessor processorMock = mock(PostProcessor.class);
      FrameBufferFactory factory = mock(FrameBufferFactory.class);
      FrameBuffer buffer = mock(FrameBuffer.class);
      Texture mockTexture = mock(Texture.class);
      when(factory.create(Mockito.anyInt(), Mockito.anyInt())).thenReturn(buffer);
      when(buffer.getColorBufferTexture()).thenReturn(mockTexture);
      return new CombinedRenderPipeline(config, processorMock, factory, mock(SpriteBatch.class),
            mock(OrthographicCamera.class));
   }

}
