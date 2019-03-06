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

import aurelienribon.tweenengine.*;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.tmx.TiledMapAPI;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

import java.util.ArrayList;
import java.util.List;

public class RasteredMovementBehavior extends BehaviorAdapter implements Movement<Orientation> {

   public static final int DEFAULT_RASTER_SIZE = 32;
   public static final float DEFAULT_INTERVAL = 1f;
   public static final Orientation DEFAULT_DIRECTION = Orientation.DOWN;
   private final TweenManager tweenManager = SharedTweenManager.getInstance();
   private final DeltaTimer timer = new DeltaTimer(DEFAULT_INTERVAL);
   private final TiledMapAPI api;
   private final List<RasteredMovementListener> listeners = new ArrayList<RasteredMovementListener>();
   private float rasterWidth = DEFAULT_RASTER_SIZE;
   private float rasterHeight = DEFAULT_RASTER_SIZE;
   private float interval = DEFAULT_INTERVAL;
   private boolean moving = false;
   private boolean wasMoving = false;
   private GameObject source;
   private TweenEquation ease = TweenEquations.easeNone;
   public RasteredMovementBehavior(TiledMapAPI api) {
      this.api = api;
   }

   public RasteredMovementBehavior rasterSize(float width, float height) {
      this.rasterWidth = Math.max(width, 1);
      this.rasterHeight = Math.max(height, 1);
      return this;
   }

   public RasteredMovementBehavior interval(float interval) {
      this.interval = interval;
      timer.update(interval);
      return this;
   }

   public RasteredMovementBehavior ease(TweenEquation ease) {
      this.ease = ease;
      return this;
   }

   public void addListener(RasteredMovementListener listener) {
      listeners.add(listener);
   }

   public boolean isMoving() {
      return moving;
   }

   @Override
   public void move(Orientation direction) {
      if (source.isActive() && isReadyToMove() && source != null) {
         source.setAttribute(Orientation.class, direction);
         if (canMove(direction)) {
            float moveX = direction.getXFactor() * rasterWidth;
            float moveY = direction.getYFactor() * rasterHeight;

            moving = true;

            for (RasteredMovementListener listener : listeners) {
               listener.moveBefore(source, moveX, moveY, interval);
            }
            timer.reset();
            source.move(moveX, moveY);
            source.setOffset(-moveX, -moveY);
            if (moveX != 0) {
               Tween.to(source, GameObjectTween.OFFSET_X, interval).target(0f).ease(ease)
                     .setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int arg0, BaseTween<?> arg1) {
                     for (RasteredMovementListener listener : listeners) {
                        listener.moveAfter(source);
                     }
                  }
               }).start(tweenManager);
            }
            if (moveY != 0) {
               Tween.to(source, GameObjectTween.OFFSET_Y, interval).target(0f).ease(ease)
                     .setCallbackTriggers(TweenCallback.COMPLETE).setCallback(new TweenCallback() {
                  @Override
                  public void onEvent(int arg0, BaseTween<?> arg1) {
                     for (RasteredMovementListener listener : listeners) {
                        listener.moveAfter(source);
                     }
                  }
               }).start(tweenManager);
            }
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
      source.setAttribute(Movement.class, this);
   }

   private boolean canMove(Orientation orientation) {
      // 1. Get the raw target to check
      int targetX = orientation.getXFactor();
      int targetY = orientation.getYFactor();
      int widthIndex = (int) Math.floor(source.getWidth() / api.getCellWidth());
      int heightIndex = (int) Math.floor(source.getHeight() / api.getCellHeight());

      // Offset target by width
      if (targetX > 0) {
         targetX += widthIndex - 1;
      }
      if (targetY > 0) {
         targetY += heightIndex - 1;
      }

      for (int i = 0; i < widthIndex; ++i) {
         int checkIndex = orientation.getYFactor() != 0 ? targetX + i : targetX;
         if (api.isCollision(source, checkIndex, targetY)) {
            return false;
         }
      }
      for (int i = 0; i < heightIndex; ++i) {
         int checkIndex = orientation.getXFactor() != 0 ? targetY + i : targetY;
         if (api.isCollision(source, targetX, checkIndex)) {
            return false;
         }
      }

      return true;
   }

   private boolean isReadyToMove() {
      return timer.reached(interval);
   }

   public interface RasteredMovementListener {
      void moveBefore(GameObject object, float moveX, float moveY, float duration);

      void moveAfter(GameObject object);
   }

}
