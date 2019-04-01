package de.bitbrain.braingdx.ui;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a fully-fledged navigation menu which can be used to navigate over multiple buttons via controller or keyboard.
 *
 * @since 0.4.10
 */
public class NavigationMenu<T extends Button> extends Table implements Navigateable {

   public static class NavigationMenuStyle {
      public boolean checkMode = false;
      public float padding;
      public boolean vertical = true;
      public Sound hoverSound = null;
      public Sound enterSound = null;
   }

   private List<Button> buttons = new ArrayList<Button>();

   private int currentCheckIndex = -1;

   private final NavigationMenuStyle style;

   public NavigationMenu() {
      this(new NavigationMenuStyle());
   }

   public NavigationMenu(NavigationMenuStyle style) {
      this.style = style;

      setTouchable(Touchable.childrenOnly);
   }

   public Cell<T> add(final T button, final ClickListener listener) {
      button.addCaptureListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            if (!button.isDisabled()) {
               listener.clicked(event, x, y);
            }
         }

         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (!button.isDisabled()) {
               listener.enter(event, x, y, pointer, fromActor);
            }
         }

         @Override
         public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            if (!button.isDisabled()) {
               listener.exit(event, x, y, pointer, toActor);
            }
         }
      });

      Cell<T> cell = center().add(button);
      if (style.vertical) {
         row();
      }
      if (!buttons.isEmpty()) {
         if (style.vertical) {
            cell.padTop(style.padding);
         } else {
            cell.padLeft(style.padding);
         }
      }
      button.addCaptureListener(new ClickListener() {
         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (!button.isDisabled() && !button.isChecked()) {
               super.enter(event, x, y, pointer, fromActor);
               setChecked(buttons.indexOf(button));
            }
         }
      });
      buttons.add(button);
      validateCheckState();
      return cell;
   }

   @Override
   public void next() {
      setChecked(getNextIndex());
   }

   @Override
   public void previous() {
      setChecked(getPreviusIndex());
   }

   @Override
   public void enter() {
      if (currentCheckIndex >= 0f && currentCheckIndex < buttons.size()) {
         Button button = buttons.get(currentCheckIndex);
         for (EventListener l : button.getCaptureListeners()) {
            if (l instanceof ClickListener) {
               ClickListener cl = (ClickListener)l;
               cl.clicked(new InputEvent(), 0f, 0f);
            }
         }
         if (style.enterSound != null) {
            style.enterSound.play();
         }
         button.setChecked(true);
      }
   }

   private void validateCheckState() {
      if (style.checkMode && buttons.size() == 1) {
         setChecked(0);
      }
   }

   private boolean isValidCheck(int index) {
      return index >= 0 && index < buttons.size() && !buttons.get(index).isDisabled();
   }

   private void setChecked(int index) {
      for (int i = 0; i < buttons.size(); ++i) {
         Button button = buttons.get(i);
         button.setChecked(i == index);
      }
      if (currentCheckIndex >= 0 && index != currentCheckIndex && style.hoverSound != null) {
         style.hoverSound.play();
      }
      currentCheckIndex = index;
   }

   private int getNextIndex() {
      int index = currentCheckIndex;
      boolean inc = false;
      int count = -1;
      while (!inc || !isValidCheck(index)) {
         if (count > buttons.size()) {
            return -1;
         }
         inc = true;
         index++;
         if (index >= buttons.size()) {
            index = 0;
         }
         count++;
      }
      return index;
   }

   private int getPreviusIndex() {
      int index = currentCheckIndex;
      boolean dec = false;
      int count = -1;
      while (!dec || !isValidCheck(index)) {
         if (count > buttons.size()) {
            return -1;
         }
         dec = true;
         index--;
         if (index < 0) {
            index = buttons.size() - 1;
         }
         count++;
      }
      return index;
   }
}
