package de.bitbrain.braingdx.graphics.pipeline;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.lighting.LightingManagerRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.StageRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.WorldRenderLayer;
import de.bitbrain.braingdx.graphics.shader.ShaderConfig;
import de.bitbrain.braingdx.world.GameWorld;

public class CombinedRenderPipelineFactory implements RenderPipelineFactory {

    private final ShaderConfig config;

    private final GameWorld world;

    private final LightingManager lightingManager;

    private final Stage stage;

    public CombinedRenderPipelineFactory(ShaderConfig config, GameWorld world, LightingManager lightingManager,
	    Stage stage) {
	this.config = config;
	this.world = world;
	this.lightingManager = lightingManager;
	this.stage = stage;
    }

    @Override
    public RenderPipeline create() {
	RenderPipeline pipeline = new CombinedRenderPipeline(config);
	pipeline.add(RenderPipeIds.BACKGROUND, new AbstractRenderLayer() {
	    @Override
	    public void render(Batch batch, float delta) {
	    }
	});
	pipeline.add(RenderPipeIds.FOREGROUND, new AbstractRenderLayer() {
	    @Override
	    public void render(Batch batch, float delta) {
		// noOp
	    }
	});
	pipeline.add(RenderPipeIds.WORLD, new WorldRenderLayer(world));
	pipeline.add(RenderPipeIds.LIGHTING, new LightingManagerRenderLayer(lightingManager));
	pipeline.add(RenderPipeIds.UI, new StageRenderLayer(stage));
	return pipeline;
    }

}
