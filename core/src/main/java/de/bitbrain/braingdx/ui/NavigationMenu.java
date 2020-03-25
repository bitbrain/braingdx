package de.bitbrain.braingdx.ui;

import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.List;

import static de.bitbrain.braingdx.tweens.TweenUtils.toColor;

/**
 * Provides a fully-fledged navigation menu which can be used to navigate over multiple buttons via controller or keyboard.
 *
 * @since 0.4.10
 */
public class NavigationMenu<T extends Button> extends Table implements Navigateable {

   public static class NavigationMenuStyle {
      public boolean checkMode = false;
      public float padding;
      public Alignment alignment = Alignment.AUTO;
      public Sound hoverSound = null;
      public Sound enterSound = null;
      public TweenEquation fadeOutEquation = TweenEquations.easeNone;
      public float fadeOutDuration = 0.3f;
      public TweenEquation fadeInEquation = TweenEquations.easeNone;
      public float fadeInDuration = 0.1f;
      public Color fadeInColor = Color.WHITE;
      public Color fadeOutColor = Color.WHITE;
      public float fadeInAlpha = 1f;
      public float fadeOutAlpha = 0.75f;

      public NavigationMenuStyle() {
         // noOp
      }

      public NavigationMenuStyle(NavigationMenuStyle original) {
         this.checkMode = original.checkMode;
         this.padding = original.padding;
         this.alignment = original.alignment;
         this.hoverSound = original.hoverSound;
         this.enterSound = original.enterSound;
         this.fadeOutEquation = original.fadeOutEquation;
         this.fadeOutDuration = original.fadeOutDuration;
         this.fadeInEquation = original.fadeInEquation;
         this.fadeInDuration = original.fadeInDuration;
         this.fadeInColor = original.fadeInColor;
         this.fadeOutColor = original.fadeOutColor;
         this.fadeInAlpha = original.fadeInAlpha;
         this.fadeOutAlpha = original.fadeOutAlpha;
      }

      public static enum Alignment {
         VERTICAL,
         HORIZONTAL,
         AUTO;
      }
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

   public Cell<T> add(final T button, final ClickListener listener, Boolean newRow) {
      button.getColor().set(getFadeToColor(false));
      if (button instanceof TextButton) {
         ((TextButton) button).getLabel().setColor(getFadeToColor(false));
      }
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
      if ((newRow == null && calculateAlignment() == NavigationMenuStyle.Alignment.VERTICAL) || (newRow != null && newRow)) {
         row();
      }
      if (!buttons.isEmpty()) {
         if ((newRow == null && calculateAlignment() == NavigationMenuStyle.Alignment.VERTICAL) || (newRow != null && newRow)) {
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

   public Cell<T> add(final T button, final ClickListener listener) {
      return add(button, listener, calculateAlignment() == NavigationMenuStyle.Alignment.VERTICAL);
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
               ClickListener cl = (ClickListener) l;
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
         final boolean wasChecked = button.isChecked();
         button.setChecked(i == index);
         final Color color = getFadeToColor(button.isChecked());
         if (button.isChecked()) {
            toColor(button.getColor(), color, style.fadeInDuration, style.fadeInEquation);
         } else {
            toColor(button.getColor(), color, style.fadeOutDuration, style.fadeOutEquation);
         }
         if (button instanceof TextButton) {
            TextButton textButton = (TextButton) button;
            if (button.isChecked()) {
               toColor(textButton.getLabel().getColor(), color, style.fadeInDuration, style.fadeInEquation);
            } else {
               toColor(textButton.getLabel().getColor(), color, style.fadeOutDuration, style.fadeOutEquation);
            }
         }
         final InputEvent inputEvent = new InputEvent();
         inputEvent.setStage(getStage());
         inputEvent.setRelatedActor(button);
         for (EventListener l : button.getCaptureListeners()) {
            if (l instanceof ClickListener) {
               ClickListener cl = (ClickListener) l;
               if (button.isChecked() && !wasChecked) {
                  inputEvent.setType(InputEvent.Type.enter);
                  cl.enter(inputEvent, 0f, 0f, 0, button);
               } else if (!button.isChecked() && wasChecked) {
                  inputEvent.setType(InputEvent.Type.exit);
                  cl.exit(inputEvent, 0f, 0f, 0, button);
               }
            }
         }
      }
      if (currentCheckIndex >= 0 && index != currentCheckIndex && style.hoverSound != null) {
         style.hoverSound.play();
      }
      currentCheckIndex = index;
   }

   private Color getFadeToColor(boolean checked) {
      if (checked) {
         return new Color(
               style.fadeInColor.r,
               style.fadeInColor.g,
               style.fadeInColor.b,
               style.fadeInAlpha
         );
      } else {
         return new Color(
               style.fadeOutColor.r,
               style.fadeOutColor.g,
               style.fadeOutColor.b,
               style.fadeOutAlpha
         );
      }
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

   private NavigationMenuStyle.Alignment calculateAlignment() {
      if (style.alignment == NavigationMenuStyle.Alignment.AUTO) {
         if (Gdx.graphics.getWidth() > Gdx.graphics.getHeight()) {
            return NavigationMenuStyle.Alignment.HORIZONTAL;
         } else {
            return NavigationMenuStyle.Alignment.VERTICAL;
         }
      }
      return style.alignment;
   }
}
