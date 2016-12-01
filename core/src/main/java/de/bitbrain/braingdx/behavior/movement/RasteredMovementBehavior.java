package de.bitbrain.braingdx.behavior.movement;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;

public class RasteredMovementBehavior extends BehaviorAdapter implements Movement<Orientation> {

    public static final int DEFAULT_RASTER_SIZE = 32;
    public static final float DEFAULT_INTERVAL = 1f;
    public static final Orientation DEFAULT_DIRECTION = Orientation.DOWN;

    private Orientation direction = DEFAULT_DIRECTION;
    private int rasterSize = DEFAULT_RASTER_SIZE;
    private float interval = DEFAULT_INTERVAL;

    private final TweenManager tweenManager = SharedTweenManager.getInstance();

    private boolean moveRequest = false;
    private boolean moving = false;

    public RasteredMovementBehavior rasterSize(int size) {
	this.rasterSize = Math.max(size, 1);
	return this;
    }

    public RasteredMovementBehavior interval(float interval) {
	this.interval = interval;
	return this;
    }

    public boolean isMoving() {
	return moving;
    }

    @Override
    public void move(Orientation direction) {
	if (direction != null) {
	    this.direction = direction;
	    moveRequest = true;
	}
    }

    @Override
    public void onAttach(GameObject source) {
	source.setAttribute(Orientation.class, direction);
    }

    @Override
    public void onDetach(GameObject source) {
	tweenManager.killTarget(source, GameObjectTween.OFFSET_X);
	tweenManager.killTarget(source, GameObjectTween.OFFSET_Y);
    }

    @Override
    public void update(GameObject source, float delta) {
	if (moveRequest && !moving) {
	    source.setAttribute(Orientation.class, direction);
	    float movementX = rasterSize * direction.getXFactor();
	    float movementY = rasterSize * direction.getYFactor();
	    source.setPosition(source.getLeft() + movementX, source.getTop() + movementY);
	    source.setOffset(-movementX, -movementY);
	    Tween.to(source, GameObjectTween.OFFSET_X, interval)
	         .target(0f)
	         .ease(TweenEquations.easeNone)
	         .start(tweenManager);
	    Tween.to(source, GameObjectTween.OFFSET_Y, interval)
	         .target(0f)
	         .ease(TweenEquations.easeNone)
		    .setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {
			@Override
			public void onEvent(int arg0, BaseTween<?> arg1) {
			    moving = false;
			}
		    })
	         .start(tweenManager);
	    moveRequest = false;
	    moving = true;
	}
    }

}
