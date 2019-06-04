package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit test for {@code AnimationCache}
 */
@RunWith(MockitoJUnitRunner.class)
public class AnimationCacheTest {

   private static final String ANIMATION_TYPE_A = "animation-type-a";
   private static final String ANIMATION_TYPE_B = "animation-type-b";
   private static final float DURATION_A = 1f;
   private static final float DURATION_B = 2f;
   private static final Animation.PlayMode PLAY_MODE_A = Animation.PlayMode.LOOP;
   private static final Animation.PlayMode PLAY_MODE_B = Animation.PlayMode.LOOP_PINGPONG;

   private static final int TEXTURE_SIZE = 128;
   private static final int TILE_SIZE = 8;
   private static final int FRAME_COUNT = 5;

   @Mock
   private Texture texture;

   private AnimationCache cache;

   private AnimationFrames framesA =  AnimationFrames.builder()
         .direction(AnimationFrames.Direction.HORIZONTAL)
         .frames(FRAME_COUNT)
         .origin(0, 0)
         .playMode(PLAY_MODE_A)
         .duration(DURATION_A)
         .build();

   private AnimationFrames framesB = AnimationFrames.builder()
         .direction(AnimationFrames.Direction.HORIZONTAL)
         .frames(FRAME_COUNT)
         .origin(0, 0)
         .playMode(PLAY_MODE_B)
         .duration(DURATION_B)
         .build();

   private AnimationSpriteSheet sheet;

   @Before
   public void setup() {
      when(texture.getWidth()).thenReturn(TEXTURE_SIZE);
      when(texture.getHeight()).thenReturn(TEXTURE_SIZE);
      sheet = new AnimationSpriteSheet(texture, TILE_SIZE);
      cache = new AnimationCache(sheet, AnimationConfig.builder()
            .registerFrames(ANIMATION_TYPE_A, framesA)
            .registerFrames(ANIMATION_TYPE_B, framesB)
            .build()
      );
   }

   @Test
   public void testGetAnimation_ShouldHaveEachDifferentKeyFrames_ForAnimationTypeA() {
      Animation<TextureRegion> animation = cache.getAnimation(ANIMATION_TYPE_A);
      TextureRegion frame = animation.getKeyFrame(0f);
      for (float time = 1f; time < 10f; time += 1f) {
         TextureRegion temporary = animation.getKeyFrame(time);
         assertThat(temporary).isNotEqualTo(frame);
         frame = temporary;
      }
   }

   @Test
   public void testGetAnimation_ShouldHaveEachDifferentKeyFrames_ForAnimationTypeB() {
      Animation<TextureRegion> animation = cache.getAnimation(ANIMATION_TYPE_B);
      TextureRegion frame = animation.getKeyFrame(0f);
      for (float time = 2f; time < 10f; time += 2f) {
         TextureRegion temporary = animation.getKeyFrame(time);
         assertThat(temporary).isNotEqualTo(frame);
         frame = temporary;
      }
   }

   @Test
   public void testGetAnimation_ShouldSupportIndividualPlayModes_IncludingCaching() {
      Animation<TextureRegion> animationA = cache.getAnimation(ANIMATION_TYPE_A);
      Animation<TextureRegion> animationB = cache.getAnimation(ANIMATION_TYPE_B);
      boolean reversed = false;
      for (float time = 0f; time < 10f; time += 1f) {
         TextureRegion regionA = animationA.getKeyFrame(time);
         TextureRegion regionB = animationB.getKeyFrame(time * 2);
         if (time != 0 && time % FRAME_COUNT == 0) {
            reversed = true;
         }
         if (reversed) {
            assertThat(regionA).isNotEqualTo(regionB);
         } else {
            assertThat(regionA).isEqualTo(regionB);
         }
      }
   }

   @Test
   public void testGetAnimation_ShouldUpdatePlayModeDynamically() {
      Animation<TextureRegion> animationA = cache.getAnimation(ANIMATION_TYPE_A);
      assertThat(animationA.getPlayMode()).isEqualTo(PLAY_MODE_A);
      framesA.setPlayMode(PLAY_MODE_B);
      animationA = cache.getAnimation(ANIMATION_TYPE_A);
      assertThat(animationA.getPlayMode()).isEqualTo(PLAY_MODE_B);
   }

   @Test
   public void testGetAnimation_ShouldUpdateDurationDynamically() {
      Animation<TextureRegion> animationA = cache.getAnimation(ANIMATION_TYPE_A);
      assertThat(animationA.getFrameDuration()).isEqualTo(DURATION_A);
      framesA.setDuration(DURATION_B);
      animationA = cache.getAnimation(ANIMATION_TYPE_A);
      assertThat(animationA.getFrameDuration()).isEqualTo(DURATION_B);
   }

   @Test
   public void testGetAnimation_ShouldUpdateDirectionDynamically() {
      Animation<TextureRegion> animation = cache.getAnimation(ANIMATION_TYPE_A);
      TextureRegion[] regionsHorizontal = sheet.getFrames(0, 0, 5, 0);
      TextureRegion[] regionsVertical = sheet.getFrames(0, 0, 0, 5);
      TextureRegion region_0_0 = regionsHorizontal[0];
      TextureRegion region_1_0 = regionsHorizontal[1];
      TextureRegion region_0_1 = regionsVertical[1];
      assertThat(animation.getKeyFrame(DURATION_A * 0)).isEqualTo(region_0_0);
      assertThat(animation.getKeyFrame(DURATION_A * 1)).isEqualTo(region_1_0);

      framesA.setDirection(AnimationFrames.Direction.VERTICAL);
      animation = cache.getAnimation(ANIMATION_TYPE_A);

      assertThat(animation.getKeyFrame(DURATION_A * 0)).isEqualTo(region_0_0);
      assertThat(animation.getKeyFrame(DURATION_A * 1)).isEqualTo(region_0_1);
   }
}