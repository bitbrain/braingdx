/* Copyright 2017 Miguel Gonzalez Sanchez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    public static final TiledCollisionResolver EMPTY_COLLISION_RESOLVER = new TiledCollisionResolver() {

	@Override
	public boolean isCollision(GameObject object, int tileOffsetX, int tileOffsetY) {
	    return false;
	}

	@Override
	public boolean isCollision(float x, float y, int layer) {
	    return false;
	}

	@Override
	public boolean isCollision(int tileX, int tileY, int layer) {
	    return false;
	}
    };

    private int rasterSize = DEFAULT_RASTER_SIZE;
    private float interval = DEFAULT_INTERVAL;

    private boolean moving = false;
    private boolean wasMoving = false;
    private GameObject source;

    private final TweenManager tweenManager = SharedTweenManager.getInstance();
    private final DeltaTimer timer = new DeltaTimer(DEFAULT_INTERVAL);
    private final MovementController<Orientation> controller;
    private final TiledCollisionResolver collisionResolver;

    public RasteredMovementBehavior(MovementController<Orientation> controller,
	    TiledCollisionResolver collisionResolver) {
	this.controller = controller;
	this.collisionResolver = collisionResolver;
    }

    public RasteredMovementBehavior(MovementController<Orientation> controller) {
	this(controller, EMPTY_COLLISION_RESOLVER);
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
	    source.setAttribute(Orientation.class, direction);
	    if (canMove(direction)) {
        	float moveX = direction.getXFactor() * rasterSize;
        	float moveY = direction.getYFactor() * rasterSize;
        	moving = true;
        	timer.reset();
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

    private boolean canMove(Orientation orientation) {
	return !collisionResolver.isCollision(source, orientation.getXFactor(), orientation.getYFactor());
    }

    private boolean isReadyToMove() {
	return timer.reached(interval);
    }

}
