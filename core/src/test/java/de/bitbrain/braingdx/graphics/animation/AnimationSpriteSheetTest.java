package de.bitbrain.braingdx.graphics.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit test of {@code AnimationSpriteSheet}
 */
@RunWith(MockitoJUnitRunner.class)
public class AnimationSpriteSheetTest {

   private static final int TILE_SIZE = 8;
   private static final int TEXTURE_SIZE = 128;

   private AnimationSpriteSheet spriteSheet;

   @Mock
   private Texture texture;

   @Before
   public void setup() {
      when(texture.getWidth()).thenReturn(TEXTURE_SIZE);
      when(texture.getHeight()).thenReturn(TEXTURE_SIZE);
   }

   @Test
   public void testGetFrames_Vertical_HappyPath_ConstructorA() {
      spriteSheet = new AnimationSpriteSheet(texture, TILE_SIZE);
      TextureRegion[] regions = spriteSheet.getFrames(0, 0, 0, 7);
      assertThat(regions.length).isEqualTo(8);
      regions = spriteSheet.getFrames(0, 0, 7, 7);
      assertThat(regions.length).isEqualTo(64);
   }

   @Test
   public void testGetFrames_Horizontal_HappyPath_ConstructorA() {
      spriteSheet = new AnimationSpriteSheet(texture, TILE_SIZE);
      TextureRegion[] regions = spriteSheet.getFrames(0, 0, 7, 0);
      assertThat(regions.length).isEqualTo(8);
      regions = spriteSheet.getFrames(0, 0, 7, 7);
      assertThat(regions.length).isEqualTo(64);
   }

   @Test
   public void testGetFrames_Vertical_HappyPath_ConstructorB() {
      spriteSheet =  new AnimationSpriteSheet(
            texture,
            TEXTURE_SIZE / TILE_SIZE,
            TEXTURE_SIZE / TILE_SIZE
      );
      TextureRegion[] regions = spriteSheet.getFrames(0, 0, 0, 7);
      assertThat(regions.length).isEqualTo(8);
      regions = spriteSheet.getFrames(0, 0, 7, 7);
      assertThat(regions.length).isEqualTo(64);
   }

   @Test
   public void testGetFrames_Horizontal_HappyPath_ConstructorB() {
      spriteSheet = new AnimationSpriteSheet(
            texture,
            TEXTURE_SIZE / TILE_SIZE,
            TEXTURE_SIZE / TILE_SIZE
      );
      TextureRegion[] regions = spriteSheet.getFrames(0, 0, 7, 0);
      assertThat(regions.length).isEqualTo(8);
      regions = spriteSheet.getFrames(0, 0, 7, 7);
      assertThat(regions.length).isEqualTo(64);
   }
}
