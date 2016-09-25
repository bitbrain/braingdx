package de.bitbrain.braingdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.bitfire.postprocessing.effects.Bloom;

import de.bitbrain.braingdx.AbstractScreen;
import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.apps.LightingManagerTest;
import de.bitbrain.braingdx.behavior.RandomMovementBehavior;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.ui.Styles;

public class LightingManagerScreen extends AbstractScreen<LightingManagerTest> {

    private static final int OBJECTS = 205;

    private static final int TYPE = 1;

    public LightingManagerScreen(LightingManagerTest game) {
	super(game);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
	super.onCreateStage(stage, width, height);

	// Basic setup
	Styles.init();
	setBackgroundColor(Color.GRAY);
	lightingManager.setAmbientLight(new Color(0.1f, 0f, 0.2f, 0.1f));
	Texture texture = GraphicsFactory.createTexture(30, 30, Color.DARK_GRAY);
	world.registerRenderer(TYPE, new SpriteRenderer(texture));

	// Shading
	RenderPipe pipe = renderPipeline.getPipe(PIPE_WORLD);
	Bloom bloom = new Bloom(Math.round(Gdx.graphics.getWidth()), Math.round(Gdx.graphics.getHeight()));
	bloom.setBlurAmount(20f);
	bloom.setBloomIntesity(3.1f);
	bloom.setBlurPasses(8);
	pipe.addEffects(bloom);
	pipe = renderPipeline.getPipe(PIPE_WORLD);

	// Objects
	for (int i = 0; i < OBJECTS; ++i) {
	    GameObject object = world.addObject();
	    object.setDimensions(30, 30);
	    object.setPosition((int) (Gdx.graphics.getWidth() * Math.random()),
		    (int) (Gdx.graphics.getHeight() * Math.random()));
	    object.setType(TYPE);
	    Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 0.27f);
	    world.applyBehavior(new PointLightBehavior(randomColor, 300f, lightingManager), object);
	    world.applyBehavior(new RandomMovementBehavior(), object);
	}

	// UI
	VerticalGroup group = new VerticalGroup();
	group.setFillParent(true);
	group.setWidth(200f);
	for (String pipeId : renderPipeline.getPipeIds()) {
	    final RenderPipe renderPipe = renderPipeline.getPipe(pipeId);
	    final TextButton textButton = new TextButton("Disable " + pipeId, Styles.BUTTON_DEFAULT_ACTIVE);
	    textButton.addListener(new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {
		    if (renderPipe.isEnabled()) {
			textButton.setStyle(Styles.BUTTON_DEFAULT_INACTIVE);
			renderPipe.setEnabled(false);
		    } else {
			textButton.setStyle(Styles.BUTTON_DEFAULT_ACTIVE);
			renderPipe.setEnabled(true);
		    }
		}
	    });
	    group.addActor(textButton);
	}
	stage.addActor(group);
    }
}
