package de.bitbrain.braingdx.graphics.pipeline;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.mockito.Mockito;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
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
	when(Gdx.app.getType()).thenReturn(ApplicationType.Desktop);
	params.add(new RenderPipelineFactory[] { new LayeredRenderPipelineFactory() });
	return params;
    }

    @Before
    public void beforeTest() {
	pipeline = pipelineFactory.create();
    }

    @Test
    public void testAddLayer() {
	final String id = "my-id";
	pipeline.add(id, mock(RenderLayer.class));
	assertThat(pipeline.getPipe(id)).isNotNull();
    }

    @Test
    public void testRender() {
	RenderLayer layer = mock(RenderLayer.class);
	Batch batch = mock(Batch.class);
	pipeline.add("my-id", layer);
	pipeline.resize(0, 0);
	pipeline.render(batch, 0f);
	Mockito.inOrder(layer).verify(layer, Mockito.calls(1)).render(batch, 0f);
    }

    @Test
    public void testGetPipeIds() {
	pipeline.add("a", mock(RenderLayer.class));
	pipeline.add("b", mock(RenderLayer.class));
	pipeline.add("c", mock(RenderLayer.class));
	assertThat(pipeline.getPipeIds()).hasSize(3);
    }

    @Test
    public void testGetPipeIdsDuplicates() {
	pipeline.add("a", mock(RenderLayer.class));
	pipeline.add("b", mock(RenderLayer.class));
	pipeline.add("b", mock(RenderLayer.class));
	pipeline.add("c", mock(RenderLayer.class));
	pipeline.add("c", mock(RenderLayer.class));
	assertThat(pipeline.getPipeIds()).hasSize(3);

    }
}
