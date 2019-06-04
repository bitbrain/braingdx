package de.bitbrain.braingdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import de.bitbrain.braingdx.graphics.animation.AnimationCache;
import de.bitbrain.braingdx.graphics.animation.AnimationConfig;
import de.bitbrain.braingdx.graphics.animation.AnimationSpriteSheet;

public class AnimationDrawable extends BaseDrawable {

   public static final String DEFAULT_FRAME_ID = "default.frame.id";

   private final AnimationCache animationCache;
   private final Sprite sprite;
   private Color color = Color.WHITE.cpy();

   private float stateTime;

   public AnimationDrawable(AnimationSpriteSheet spriteSheet, AnimationConfig config) {
      this.animationCache = new AnimationCache(spriteSheet, config);
      this.sprite = new Sprite();
   }

   public void setAlpha(float alpha) {
      color.a = alpha;
   }

   @Override
   public void draw(Batch batch, float x, float y, float width, float height) {
      stateTime += Gdx.graphics.getDeltaTime();
      batch.setColor(color);
      drawRegion(
            batch,
            animationCache.getAnimation(DEFAULT_FRAME_ID).getKeyFrame(stateTime),
            x,
            y,
            width,
            height
      );
      batch.setColor(Color.WHITE);
   }

   private void drawRegion(Batch batch, TextureRegion region, float x, float y, float width, float height) {
      sprite.setRegion(region);
      sprite.setOrigin(x + width / 2f, y + height / 2f);
      sprite.setColor(color);
      sprite.setBounds(
            x,
            y,
            width,
            height
      );
      sprite.setScale(1f, 1f);
      sprite.draw(batch);
   }
}
