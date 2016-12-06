package de.bitbrain.braingdx.apps.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.bitbrain.braingdx.apps.Assets;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.movement.Orientation;
import de.bitbrain.braingdx.behavior.movement.RasteredMovementBehavior;
import de.bitbrain.braingdx.graphics.animation.SpriteSheet;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation;
import de.bitbrain.braingdx.graphics.animation.SpriteSheetAnimation.Direction;
import de.bitbrain.braingdx.graphics.animation.types.AnimationTypes;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.RenderPipe;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.pipeline.layers.TextureRenderLayer;
import de.bitbrain.braingdx.graphics.renderer.SpriteSheetAnimationRenderer;
import de.bitbrain.braingdx.postprocessing.effects.Bloom;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.world.GameObject;

public class RPGScreen extends AbstractScreen<RPGTest> {

    private static final int SOLDIER = 1;

    private RasteredMovementBehavior behavior;

    private SpriteSheetAnimation animation;

    public RPGScreen(RPGTest rpgTest) {
	super(rpgTest);
    }

    @Override
    protected void onCreateStage(Stage stage, int width, int height) {
	prepareResources();
	behavior = new RasteredMovementBehavior().interval(0.5f).rasterSize(64);
	addSoldier(0f, 0f, 64);
	setupShaders();
    }

    @Override
    protected void onUpdate(float delta) {
	if (Gdx.input.isKeyPressed(Keys.W)) {
	    animation.type(AnimationTypes.FORWARD_YOYO);
	    behavior.move(Orientation.UP);
	} else if (Gdx.input.isKeyPressed(Keys.A)) {
	    animation.type(AnimationTypes.FORWARD_YOYO);
	    behavior.move(Orientation.LEFT);
	} else if (Gdx.input.isKeyPressed(Keys.S)) {
	    animation.type(AnimationTypes.FORWARD_YOYO);
	    behavior.move(Orientation.DOWN);
	} else if (Gdx.input.isKeyPressed(Keys.D)) {
	    animation.type(AnimationTypes.FORWARD_YOYO);
	    behavior.move(Orientation.RIGHT);
	} else if (!behavior.isMoving()) {
	    animation.type(AnimationTypes.RESET);
	}
    }

    private void prepareResources() {
	final Texture background = SharedAssetManager.getInstance().get(Assets.RPG.BACKGROUND, Texture.class);
	getRenderPipeline().add(RenderPipeIds.BACKGROUND, new TextureRenderLayer(background));
	getLightingManager().setAmbientLight(new Color(0.06f, 0f, 0.1f, 0.1f));
	Texture texture = SharedAssetManager.getInstance().get(Assets.RPG.CHARACTER_TILESET);
	SpriteSheet sheet = new SpriteSheet(texture, 12, 8);
	animation = new SpriteSheetAnimation(sheet)
	         .origin(3, 0)
	         .interval(0.2f)
	         .direction(Direction.HORIZONTAL)
		 .type(AnimationTypes.RESET)
	         .base(1)
	         .frames(3);
	getRenderManager().register(SOLDIER, 
	   new SpriteSheetAnimationRenderer(animation)
	      .map(Orientation.DOWN, 0)
	      .map(Orientation.LEFT, 1)
	      .map(Orientation.RIGHT, 2)
	      .map(Orientation.UP, 3)
	);
    }

    private void setupShaders() {
	RenderPipe worldPipe = getRenderPipeline().getPipe(RenderPipeIds.WORLD);
	Bloom bloom = new Bloom(Math.round(Gdx.graphics.getWidth() / 1.5f),
		Math.round(Gdx.graphics.getHeight() / 1.5f));
	bloom.setBlurAmount(25f);
	bloom.setBloomIntesity(1.4f);
	bloom.setBlurPasses(7);
	worldPipe.addEffects(bloom);
    }

    private void addSoldier(float x, float y, int size) {
	GameObject object = getGameWorld().addObject();
	object.setPosition(x, y);
	object.setType(SOLDIER);
	object.setDimensions(size, size);
	getBehaviorManager().apply(new PointLightBehavior(Color.valueOf("ff5544ff"), 700f, getLightingManager()),
		object);
	getBehaviorManager().apply(behavior, object);
	getGameCamera().setTarget(object);
	getGameCamera().setSpeed(1f);
	getGameCamera().setZoomScale(0.001f);
    }
}