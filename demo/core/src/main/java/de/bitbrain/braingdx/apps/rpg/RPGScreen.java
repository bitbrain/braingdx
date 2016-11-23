package de.bitbrain.braingdx.apps.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.SpriteRenderer;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.TextureRenderLayer;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.postprocessing.effects.Fxaa;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.world.GameObject;

public class RPGScreen extends AbstractScreen<RPGTest> {

    private static final int SOLDIER = 1;

    public RPGScreen(RPGTest rpgTest) {
	super(rpgTest);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
	prepareResources();
	addSoldier(100, 100, 350);
	setupShaders();
    }

    private void prepareResources() {
	getLightingManager().setAmbientLight(new Color(0.05f, 0f, 0.5f, 0.15f));
	final Texture background = SharedAssetManager.getInstance().get(Assets.WALL, Texture.class);
	getRenderPipeline().add(RenderPipeIds.BACKGROUND, new TextureRenderLayer(background));
	Texture texture = SharedAssetManager.getInstance().get(Assets.SOLDIER);
	getRenderManager().register(SOLDIER, new SpriteRenderer(texture));
    }

    private void setupShaders() {
	RenderPipe worldPipe = getRenderPipeline().getPipe(RenderPipeIds.WORLD);
	Bloom bloom = new Bloom(Math.round(Gdx.graphics.getWidth() / 1.5f),
		Math.round(Gdx.graphics.getHeight() / 1.5f));
	bloom.setBlurAmount(15f);
	bloom.setBloomIntesity(1.1f);
	bloom.setBlurPasses(5);
	worldPipe.addEffects(bloom);
	Fxaa aliasing = new Fxaa(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	worldPipe.addEffects(aliasing);
    }

    private void addSoldier(float x, float y, int size) {
	GameObject object = getGameWorld().addObject();
	object.setPosition(x, y);
	object.setType(SOLDIER);
	object.setDimensions(size, size);
	getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("885522"), 700f, getLightingManager()),
		object);
    }
}