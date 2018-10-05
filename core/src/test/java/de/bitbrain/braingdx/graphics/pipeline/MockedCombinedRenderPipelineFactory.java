package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.viewport.Viewport;
import de.bitbrain.braingdx.graphics.FrameBufferFactory;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.graphics.postprocessing.PostProcessor;
import de.bitbrain.braingdx.util.ViewportFactory;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockedCombinedRenderPipelineFactory implements RenderPipelineFactory {

   @Override
   public RenderPipeline create() {
      ShaderConfig config = mock(ShaderConfig.class);
      PostProcessor processorMock = mock(PostProcessor.class);
      FrameBufferFactory factory = mock(FrameBufferFactory.class);
      FrameBuffer buffer = mock(FrameBuffer.class);
      Texture mockTexture = mock(Texture.class);
      ViewportFactory viewportFactory = mock(ViewportFactory.class);
      Viewport viewport = mock(Viewport.class);
      when(factory.create(Mockito.anyInt(), Mockito.anyInt())).thenReturn(buffer);
      Mockito.doAnswer(new Answer<Object>() {
         @Override
         public Object answer(InvocationOnMock invocation) throws Throwable {
            return null;
         }
      }).when(viewport).update(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyBoolean());
      when(viewportFactory.create(Mockito.anyInt(), Mockito.anyInt())).thenReturn(viewport);
      when(buffer.getColorBufferTexture()).thenReturn(mockTexture);
      return new CombinedRenderPipeline(config, processorMock, factory, mock(SpriteBatch.class),
            mock(OrthographicCamera.class), viewportFactory);
   }

}
