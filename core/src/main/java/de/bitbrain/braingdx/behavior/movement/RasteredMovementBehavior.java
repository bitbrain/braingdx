package de.bitbrain.braingdx.behavior.movement;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

public class RasteredMovementBehavior extends BehaviorAdapter implements Movement<Orientation> {

    public static final int DEFAULT_RASTER_SIZE = 32;
    public static final float DEFAULT_INTERVAL = 1f;
    public static final Orientation DEFAULT_DIRECTION = Orientation.DOWN;

    private int rasterSize = DEFAULT_RASTER_SIZE;
    private float interval = DEFAULT_INTERVAL;

    private boolean moving = false;
    private boolean wasMoving = false;
    private GameObject source;

    private final TweenManager tweenManager = SharedTweenManager.getInstance();
    private final DeltaTimer timer = new DeltaTimer(DEFAULT_INTERVAL);
    private final MovementController<Orientation> controller;

    public RasteredMovementBehavior(MovementController<Orientation> controller) {
	this.controller = controller;
    }

    public RasteredMovementBehavior rasterSize(int size) {
	this.rasterSize = Math.max(size, 1);
	return this;
    }

    public RasteredMovementBehavior interval(float interval) {
	this.interval = interval;
	timer.update(interval);
	return this;
    }

    public boolean isMoving() {
	return moving;
    }

    @Override
    public void move(Orientation direction) {
	if (isReadyToMove() && source != null) {
	    moving = true;
	    timer.reset();
	    source.setAttribute(Orientation.class, direction);
	    float moveX = direction.getXFactor() * rasterSize;
	    float moveY = direction.getYFactor() * rasterSize;
	    source.move(moveX, moveY);
	    source.setOffset(-moveX, -moveY);
	    Tween.to(source, GameObjectTween.OFFSET_X, interval)
	         .target(0f)
	         .ease(TweenEquations.easeNone)
	         .start(tweenManager);
	    Tween.to(source, GameObjectTween.OFFSET_Y, interval)
	         .target(0f)
	         .ease(TweenEquations.easeNone)
	         .start(tweenManager);
	}
    }

    @Override
    public void onAttach(GameObject source) {
	this.source = source;
	source.setAttribute(Orientation.class, DEFAULT_DIRECTION);
    }

    @Override
    public void onDetach(GameObject source) {
	tweenManager.killTarget(source, GameObjectTween.OFFSET_X);
	tweenManager.killTarget(source, GameObjectTween.OFFSET_Y);
    }

    @Override
    public void update(GameObject source, float delta) {
	this.source = source;
	timer.update(delta);
	if (wasMoving && moving && isReadyToMove()) {
	    moving = false;
	}
	wasMoving = moving;
	controller.update(this, delta);
	source.setAttribute(Movement.class, this);
    }

    private boolean isReadyToMove() {
	return timer.reached(interval);
    }

}
