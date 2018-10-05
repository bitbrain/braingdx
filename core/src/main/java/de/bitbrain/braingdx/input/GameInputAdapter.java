package de.bitbrain.braingdx.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * An extension of the {@link InputAdapter} which adds additional capabilities
 * required for a smooth game experience. It prevents input jitter and allows
 * to use swiping and touching simultaneously.
 *
 * @author Miguel Gonzalez Sanchez
 * @since 0.3.5
 */
public class GameInputAdapter extends InputAdapter {

   private static final int SWIPE_TOLERANCE = 2;

   public interface GameInputListener {
      /**
       * Is called whenever the user has initiated a strong enough swipe.
       *
       * @param deltaX the horizontal distance swiped
       * @param deltaY the vertical distance swiped
       */
      void onSwipe(int deltaX, int deltaY);

      /**
       * Is called whenever the user touches but does not swipe
       */
      void onTouch();

      /**
       * Is called whenever the user types
       *
       * @param key code defined by {@link com.badlogic.gdx.Input.Keys}
       */
      void onType(int key);
   }

   private final List<GameInputListener> listeners = new ArrayList<GameInputListener>();
   private boolean touched = false;
   private boolean dragged = false;
   private final Vector2 direction = new Vector2();

   public void addListener(GameInputListener listener) {
      listeners.add(listener);
   }

   @Override
   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
      if (touched) {
         for (GameInputListener l : listeners) {
            l.onTouch();
         }
      }
      touched = false;
      dragged = false;
      return false;
   }

   @Override
   public boolean keyDown(int keycode) {
      if (touched) {
         return false;
      }
      for (GameInputListener l : listeners) {
         l.onType(keycode);
      }
      touched = true;
      return true;
   }

   @Override
   public boolean keyUp(int keycode) {
      touched = false;
      return false;
   }

   @Override
   public boolean touchDragged(int screenX, int screenY, int pointer) {
      if (dragged) {
         return false;
      }
      int deltaX = Gdx.input.getDeltaX(pointer);
      int deltaY = Gdx.input.getDeltaY(pointer);

      int absDeltaX = Math.abs(deltaX);
      int absDeltaY = Math.abs(deltaY);

      direction.set(absDeltaX, absDeltaY);

      if (direction.len() >= SWIPE_TOLERANCE) {
         for (GameInputListener l : listeners) {
            l.onSwipe(deltaX, deltaY);
         }
      } else {
         touched = true;
         return false;
      }
      touched = false;
      dragged = true;
      return true;
   }
}
