package de.bitbrain.braingdx.apps.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.AnimationRenderer;
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
	addSoldier(100, 100, 64);
	setupShaders();
    }

    private void prepareResources() {
	getLightingManager().setAmbientLight(new Color(0.05f, 0f, 0.5f, 0.15f));
	Texture texture = SharedAssetManager.getInstance().get(Assets.RPG.CHARACTER_TILESET);
	SpriteSheet sheet = new SpriteSheet(texture, 12, 8);
	getRenderManager().register(SOLDIER, new AnimationRenderer(
	   new SpriteSheetAnimation(sheet)
	      .offset(3, 0)
	      .interval(0.2f)
	      .type(AnimationTypes.FORWARD_YOYO)
	      .frames(3)
	));
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
	object.setDimensions(48, 64);
	getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("ff5522ff"), 700f, getLightingManager()),
		object);
    }
}