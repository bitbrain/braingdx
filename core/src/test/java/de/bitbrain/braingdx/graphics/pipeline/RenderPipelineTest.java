package de.bitbrain.braingdx.graphics.pipeline;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;

@RunWith(Parameterized.class)
public class RenderPipelineTest {

   @Parameter
   public RenderPipelineFactory pipelineFactory;

   private RenderPipeline pipeline;

   @Parameters
   public static Collection<RenderPipelineFactory[]> getParams() {
      List<RenderPipelineFactory[]> params = new ArrayList<RenderPipelineFactory[]>();
      Gdx.app = mock(Application.class);
      Gdx.gl = mock(GL20.class);
      when(Gdx.app.getType()).thenReturn(ApplicationType.Desktop);
      params.add(new RenderPipelineFactory[] { new MockedLayeredRenderPipelineFactory() });
      params.add(new RenderPipelineFactory[] { new MockedCombinedRenderPipelineFactory() });
      return params;
   }

   @Before
   public void beforeTest() {
      pipeline = pipelineFactory.create();
   }

   @Test
   public void testAddLayer() {
      final String id = "my-id";
      pipeline.set(id, mock(RenderLayer.class));
      assertThat(pipeline.getPipe(id)).isNotNull();
   }

   @Test
   public void testRender() {
      RenderLayer layerA = mock(RenderLayer.class);
      RenderLayer layerB = mock(RenderLayer.class);
      RenderLayer layerC = mock(RenderLayer.class);
      Batch batch = mock(Batch.class);
      pipeline.set("my-id-a", layerA);
      pipeline.set("my-id-b", layerB);
      pipeline.set("my-id-c", layerC);
      pipeline.resize(0, 0);
      pipeline.render(batch, 0f);
      InOrder order = Mockito.inOrder(layerA, layerB, layerC);
      order.verify(layerA, Mockito.calls(1)).render(batch, 0f);
      order.verify(layerB, Mockito.calls(1)).render(batch, 0f);
      order.verify(layerC, Mockito.calls(1)).render(batch, 0f);
   }

   @Test
   public void testGetPipeIds() {
      pipeline.set("a", mock(RenderLayer.class));
      pipeline.set("b", mock(RenderLayer.class));
      pipeline.set("c", mock(RenderLayer.class));
      assertEquals(pipeline.getPipeIds().size(), 3);
   }

   @Test
   public void testGetPipeIdsDuplicates() {
      pipeline.set("a", mock(RenderLayer.class));
      pipeline.set("b", mock(RenderLayer.class));
      pipeline.set("b", mock(RenderLayer.class));
      pipeline.set("c", mock(RenderLayer.class));
      pipeline.set("c", mock(RenderLayer.class));
      assertEquals(pipeline.getPipeIds().size(), 3);

   }
}
