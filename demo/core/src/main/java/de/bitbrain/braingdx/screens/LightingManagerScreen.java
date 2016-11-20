package de.bitbrain.braingdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import de.bitbrain.braingdx.apps.LightingManagerTest;
import de.bitbrain.braingdx.assets.Assets;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.RandomMovementBehavior;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Fxaa;
import de.bitbrain.braingdx.ui.Styles;
import de.bitbrain.braingdx.world.GameObject;

public class LightingManagerScreen extends AbstractScreen<LightingManagerTest> {

    private static final int OBJECTS = 5;

    private static final int TYPE = 1;

    public LightingManagerScreen(LightingManagerTest game) {
	super(game);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
	prepareResources();
	setupShaders();
	addRandomObjects();
	createButtonUI(stage);
    }

    private void prepareResources() {
	Styles.init();
	final Texture background = SharedAssetManager.getInstance().get(Assets.WALL, Texture.class);
	getRenderPipeline().add(PIPE_BACKGROUND, new RenderLayer() {

	    @Override
	    public void render(Batch batch, float delta) {
		batch.begin();
		batch.draw(background, 0f, 0f);
		batch.end();
	    }

	    @Override
	    public void beforeRender() {
		// TODO Auto-generated method stub

	    }

	});
	getLightingManager().setAmbientLight(new Color(0.1f, 0f, 0.2f, 0.25f));
	Texture texture = SharedAssetManager.getInstance().get(Assets.SOLDIER);
	getRenderManager().register(TYPE, new SpriteRenderer(texture));
    }

    private void setupShaders() {
	RenderPipe uiPipe = getRenderPipeline().getPipe(PIPE_UI);
	Bloom uiBloom = new Bloom(Math.round(Gdx.graphics.getWidth() / 1f), Math.round(Gdx.graphics.getHeight() / 1f));
	uiBloom.setBlurAmount(10f);
	uiBloom.setBloomIntesity(2.1f);
	uiBloom.setBlurPasses(6);
	uiPipe.addEffects(uiBloom);
	Fxaa aliasing = new Fxaa(Gdx.graphics.getWidth(),
		Gdx.graphics.getHeight());
	uiPipe.addEffects(aliasing);
    }

    private void addRandomObjects() {
	for (int i = 0; i < OBJECTS; ++i) {
	    GameObject object = getGameWorld().addObject();
	    object.setDimensions(230, 230);
	    object.setPosition((int) (Gdx.graphics.getWidth() * Math.random()),
		    (int) (Gdx.graphics.getHeight() * Math.random()));
	    object.setType(TYPE);
	    Color randomColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1f);
	    getBehaviorManager().apply(new PointLightBehavior(randomColor, 500f, getLightingManager()), object);
	    getBehaviorManager().apply(new RandomMovementBehavior(), object);
	    Color objectColor = randomColor.cpy();
	    objectColor.a = 1f;
	    // object.setColor(objectColor);
	}
    }

    private void createButtonUI(Stage stage) {
	Table group = new Table();
	group.setFillParent(true);
	for (String pipeId : getRenderPipeline().getPipeIds()) {
	    final RenderPipe renderPipe = getRenderPipeline().getPipe(pipeId);
	    final TextButton textButton = new TextButton("Disable " + pipeId, Styles.BUTTON_DEFAULT_ACTIVE);
	    textButton.getColor().a = 0.7f;
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
	    group.left().top().add(textButton).width(400f).padBottom(10f).row();
	}
	stage.addActor(group);
    }
}
