package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import de.bitbrain.braingdx.util.GdxUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class RenderPipelineTest {

   @Parameter
   public RenderPipelineFactory pipelineFactory;

   private RenderPipeline pipeline;

   @Parameters
   public static Collection<RenderPipelineFactory[]> getParams() {
      List<RenderPipelineFactory[]> params = new ArrayList<RenderPipelineFactory[]>();
      GdxUtils.mockApplicationContext();
      Gdx.gl = mock(GL20.class);
      when(Gdx.app.getType()).thenReturn(ApplicationType.Desktop);
      //params.add(new RenderPipelineFactory[] { new MockedLayeredRenderPipelineFactory() });
      params.add(new RenderPipelineFactory[]{new MockedCombinedRenderPipelineFactory()});
      return params;
   }

   @Before
   public void beforeTest() {
      pipeline = pipelineFactory.create();
   }

   @Test
   public void testAddLayer() {
      final String id = "my-id";
      pipeline.put(id, mock(RenderLayer.class));
      assertThat(pipeline.getPipeIds()).contains("my-id");
   }

   @Test
   public void testAddLayerBefore() {
      final String id = "my-id";
      pipeline.put(id, mock(RenderLayer.class));
      pipeline.putBefore("my-id", "another-id", mock(RenderLayer.class));
      assertThat(pipeline.getPipeIds()).containsSequence("another-id", "my-id");
   }

   @Test
   public void testAddLayerAfter() {
      final String id = "my-id";
      pipeline.put(id, mock(RenderLayer.class));
      pipeline.putAfter("my-id", "another-id", mock(RenderLayer.class));
      assertThat(pipeline.getPipeIds()).containsSequence("my-id", "another-id");
   }

   @Test
   public void testRender() {
      RenderLayer layerA = mock(RenderLayer.class);
      RenderLayer layerB = mock(RenderLayer.class);
      RenderLayer layerC = mock(RenderLayer.class);
      Batch batch = mock(Batch.class);
      pipeline.put("my-id-a", layerA);
      pipeline.put("my-id-b", layerB);
      pipeline.put("my-id-c", layerC);
      pipeline.resize(0, 0);
      pipeline.render(batch, 0f);
      InOrder order = Mockito.inOrder(layerA, layerB, layerC);
      order.verify(layerA, Mockito.calls(1)).render(batch, 0f);
      order.verify(layerB, Mockito.calls(1)).render(batch, 0f);
      order.verify(layerC, Mockito.calls(1)).render(batch, 0f);
   }

   @Test
   public void testGetPipeIds() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.put("c", mock(RenderLayer.class));
      assertEquals(pipeline.getPipeIds().size(), 3);
   }

   @Test
   public void testGetPipeIdsDuplicates() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.put("c", mock(RenderLayer.class));
      pipeline.put("c", mock(RenderLayer.class));
      assertEquals(pipeline.getPipeIds().size(), 3);
   }

   @Test
   public void testMoveBefore_Swap() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.moveBefore("b", "a");
      assertThat(pipeline.getPipeIds()).containsExactly("b", "a");
   }

   @Test
   public void testMoveBefore_DifferentLayer() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.put("c", mock(RenderLayer.class));
      pipeline.moveBefore("c", "b");
      assertThat(pipeline.getPipeIds()).containsExactly("a", "c", "b");
   }

   @Test
   public void testMoveAfter_Swap() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.moveAfter("a", "b");
      assertThat(pipeline.getPipeIds()).containsExactly("b", "a");
   }

   @Test
   public void testMoveAfter_DifferentLayer() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.put("c", mock(RenderLayer.class));
      pipeline.moveAfter("c", "a");
      assertThat(pipeline.getPipeIds()).containsExactly("a", "c", "b");
   }

   @Test
   public void testRemove_Empty() {
      // No exception expected!
      pipeline.remove("doesNotExist");
   }

   @Test
   public void testRemove_Existing() {
      pipeline.put("a", mock(RenderLayer.class));
      pipeline.put("b", mock(RenderLayer.class));
      pipeline.put("c", mock(RenderLayer.class));
      pipeline.remove("b");
      assertThat(pipeline.getPipeIds()).containsExactly("a", "c");
      pipeline.remove("a");
      assertThat(pipeline.getPipeIds()).containsExactly("c");
   }

}
