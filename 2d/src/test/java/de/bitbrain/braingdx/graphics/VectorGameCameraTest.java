package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VectorGameCameraTest {

   @Mock
   private GameWorld gameWorld;

   @Spy
   private OrthographicCamera orthographicCamera;

   @Mock
   private GameWorld.WorldBounds bounds;

   private VectorGameCamera camera;

   @Before
   public void setup() {
      camera = new VectorGameCamera(orthographicCamera, gameWorld);
      camera.setStickToWorldBounds(false);
      doNothing().when(orthographicCamera).update();
      doNothing().when(orthographicCamera).update(anyBoolean());
   }

   @Test
   public void testDefaultInitialization() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      setCameraBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
      setWorldBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
      camera.update(0f);
      assertThat(orthographicCamera.position.len()).isZero();
      assertThat(orthographicCamera.viewportWidth).isEqualTo(WORLD_WIDTH);
      assertThat(orthographicCamera.viewportHeight).isEqualTo(WORLD_HEIGHT);
   }

   @Test
   public void testSetDefaultZoom() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / FACTOR, WORLD_HEIGHT / FACTOR);
      camera.setDefaultZoomFactor(0.5f);
      camera.update(0f);

      assertThat(orthographicCamera.position.len()).isZero();
      assertThat(orthographicCamera.viewportWidth).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.viewportHeight).isEqualTo(WORLD_HEIGHT/ FACTOR);
   }

   @Test
   public void testSetZoom_ToValue() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / FACTOR, WORLD_HEIGHT / FACTOR);
      camera.setZoom(0.5f);
      camera.update(0f);

      assertThat(orthographicCamera.position.len()).isZero();
      assertThat(orthographicCamera.viewportWidth).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.viewportHeight).isEqualTo(WORLD_HEIGHT/ FACTOR);
   }

   @Test
   public void testSetZoom_ToWidth() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / FACTOR, WORLD_HEIGHT / FACTOR);
      camera.setZoom(WORLD_WIDTH / 10f, GameCamera.ZoomMode.TO_WIDTH);
      camera.update(0f);

      assertThat(orthographicCamera.position.len()).isZero();
      assertThat(orthographicCamera.viewportWidth).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.viewportHeight).isEqualTo(WORLD_HEIGHT/ FACTOR);
      assertThat(camera.getDefaultZoomFactor()).isEqualTo(0.2f);
   }

   @Test
   public void testSetZoom_ToHeight() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / FACTOR, WORLD_HEIGHT / FACTOR);
      camera.setZoom(WORLD_HEIGHT / 10f, GameCamera.ZoomMode.TO_HEIGHT);
      camera.update(0f);

      assertThat(orthographicCamera.position.len()).isZero();
      assertThat(orthographicCamera.viewportWidth).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.viewportHeight).isEqualTo(WORLD_HEIGHT/ FACTOR);
      assertThat(camera.getDefaultZoomFactor()).isEqualTo(0.2f);
   }

   @Test
   public void testSetDefaultZoom_FocusCentered_NoTrackingTarget() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
      camera.setDefaultZoomFactor(0.5f);
      camera.focusCentered();
      camera.update(0f);

      assertThat(orthographicCamera.position.x).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.position.y).isEqualTo(WORLD_HEIGHT / FACTOR);
      assertThat(orthographicCamera.viewportWidth * orthographicCamera.zoom).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.viewportHeight * orthographicCamera.zoom).isEqualTo(WORLD_HEIGHT / FACTOR);
   }

   @Test
   public void testSetDefaultZoom_FocusCentered_WithTrackingTarget() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2f;
      final float OBJECT_LEFT = 0f;
      final float OBJECT_TOP = 0f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);
      camera.setDefaultZoomFactor(1f / FACTOR);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(orthographicCamera.position.x).isEqualTo(OBJECT_LEFT + OBJECT_WIDTH / FACTOR);
      assertThat(orthographicCamera.position.y).isEqualTo(OBJECT_TOP + OBJECT_HEIGHT / FACTOR);
      assertThat(orthographicCamera.viewportWidth * orthographicCamera.zoom).isEqualTo(WORLD_WIDTH / FACTOR);
      assertThat(orthographicCamera.viewportHeight * orthographicCamera.zoom).isEqualTo(WORLD_HEIGHT / FACTOR);
   }

   @Test
   public void testTrackTarget_WithTrackingSpeed() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
      GameObject object = new GameObject();
      camera.setTrackingTarget(object, false);
      camera.setTargetTrackingSpeed(1f);
      camera.setPosition(100f, 100f);
      float initialDistance1 = camera.getDistanceTo(object);
      camera.update(0.001f);
      float moveDistance1 = camera.getDistanceTo(object);
      assertThat(moveDistance1).isLessThan(initialDistance1);

      camera.setTargetTrackingSpeed(2f);
      camera.setPosition(100f, 100f);
      camera.update(0.001f);
      float moveDistance2 = camera.getDistanceTo(object);
      assertThat(moveDistance2).isLessThan(moveDistance1);

      // move the game object
      object.setPosition(100f, 100f);
      Vector3 oldPosition = camera.getPosition();
      camera.update(0.001f);
      Vector3 newPosition = camera.getPosition();
      assertThat(oldPosition.x).isLessThan(newPosition.x);
      assertThat(oldPosition.y).isLessThan(newPosition.y);
   }

   @Test
   public void testStickToBounds_TooSmallWidth_ShouldStickToWidth() {
      final float WORLD_WIDTH = 500;
      final float WORLD_HEIGHT = 800;
      final float FACTOR = 2;
      final float OBJECT_LEFT = 0f;
      final float OBJECT_TOP = 0f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH * FACTOR, WORLD_HEIGHT * FACTOR);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);

      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(WORLD_WIDTH / 2f);
      assertThat(camera.getPosition().y).isEqualTo(WORLD_HEIGHT / 2f);
      assertThat(camera.getScaledCameraWidth())
            .isEqualTo(WORLD_WIDTH);
      assertThat(camera.getScaledCameraHeight())
            .isEqualTo(WORLD_HEIGHT);
   }

   @Test
   public void testStickToBounds_TooSmallHeight_ShouldStickToHeight() {
      final float WORLD_WIDTH = 800;
      final float WORLD_HEIGHT = 500;
      final float FACTOR = 2;
      final float OBJECT_LEFT = 0f;
      final float OBJECT_TOP = 0f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(0f, 0f, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH * FACTOR, WORLD_HEIGHT * FACTOR);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);

      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(WORLD_WIDTH / 2f);
      assertThat(camera.getPosition().y).isEqualTo(WORLD_HEIGHT / 2f);
      assertThat(camera.getScaledCameraWidth())
            .isEqualTo(WORLD_WIDTH);
      assertThat(camera.getScaledCameraHeight())
            .isEqualTo(WORLD_HEIGHT);
   }

   @Test
   public void testStickToBounds_LeftTop() {
      final float WORLD_X = 100;
      final float WORLD_Y = 200;
      final float WORLD_WIDTH = 800;
      final float WORLD_HEIGHT = 500;
      final float OBJECT_LEFT = 0f;
      final float OBJECT_TOP = 0f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(WORLD_X, WORLD_Y, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);
      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(WORLD_X + camera.getScaledCameraWidth() / 2f);
      assertThat(camera.getPosition().y).isEqualTo(WORLD_Y+ camera.getScaledCameraHeight() / 2f);
   }

   @Test
   public void testStickToBounds_RightTop() {
      final float WORLD_X = 100;
      final float WORLD_Y = 200;
      final float WORLD_WIDTH = 800;
      final float WORLD_HEIGHT = 500;
      final float OBJECT_LEFT = 1000f;
      final float OBJECT_TOP = 0f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(WORLD_X, WORLD_Y, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);
      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(WORLD_X + WORLD_WIDTH - camera.getScaledCameraWidth() / 2f);
      assertThat(camera.getPosition().y).isEqualTo(WORLD_Y+ camera.getScaledCameraHeight() / 2f);
   }

   @Test
   public void testStickToBounds_BottomLeft() {
      final float WORLD_X = 100;
      final float WORLD_Y = 200;
      final float WORLD_WIDTH = 800;
      final float WORLD_HEIGHT = 500;
      final float OBJECT_LEFT = 0f;
      final float OBJECT_TOP = 1000f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(WORLD_X, WORLD_Y, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);
      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(WORLD_X + camera.getScaledCameraWidth() / 2f);
      assertThat(camera.getPosition().y).isEqualTo(WORLD_Y + WORLD_HEIGHT - camera.getScaledCameraHeight() / 2f);
   }

   @Test
   public void testStickToBounds_BottomRight() {
      final float WORLD_X = 100;
      final float WORLD_Y = 200;
      final float WORLD_WIDTH = 800;
      final float WORLD_HEIGHT = 500;
      final float OBJECT_LEFT = 1000f;
      final float OBJECT_TOP = 1000f;
      final float OBJECT_WIDTH = 128;
      final float OBJECT_HEIGHT = 128;
      setWorldBounds(WORLD_X, WORLD_Y, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f);

      GameObject object = new GameObject();
      object.setPosition(OBJECT_LEFT, OBJECT_TOP);
      object.setDimensions(OBJECT_WIDTH, OBJECT_HEIGHT);
      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(WORLD_X + WORLD_WIDTH - camera.getScaledCameraWidth() / 2f);
      assertThat(camera.getPosition().y).isEqualTo(WORLD_Y + WORLD_HEIGHT - camera.getScaledCameraHeight() / 2f);
   }

   @Test
   public void testStickToBounds_WithZoom() {
      setWorldBounds(0, 0, 100, 100);
      setCameraBounds(0f, 0f, 100, 100);

      GameObject object = new GameObject();
      object.setPosition(0f, 0f);
      object.setDimensions(10, 10);
      camera.setStickToWorldBounds(true);
      camera.setDefaultZoomFactor(0.5f);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0f);

      assertThat(camera.getPosition().x).isEqualTo(25f);
      assertThat(camera.getPosition().y).isEqualTo(25f);
      assertThat(camera.getScaledCameraWidth()).isEqualTo(50);
      assertThat(camera.getScaledCameraHeight()).isEqualTo(50);
   }

   @Test
   public void testSmoothTransition_TwoPoints_NoNumberFormatException() {
      float startX = 30.5455f;
      float startY = 11.455002f;
      float targetX = 36.25f;
      float targetY = 60.75f;
      setWorldBounds(0f, 0f, 400f, 400f);
      GameObject object = new GameObject();
      object.setPosition(startX, startY);
      object.setDimensions(0f, 0f);
      camera.setStickToWorldBounds(false);
      camera.setTrackingTarget(object);
      camera.focusCentered(object);
      camera.update(0.1f);
      object.setPosition(targetX, targetY);
      for (int i = 0; i < 10; i++) {
         camera.update(0.1f);
      }
   }

   @Ignore
   @Test
   public void testStickToBounds_TrackingTarget_MovingDown_shouldNotFlicker() {
      // Initial setup
      final int BLOCKS_SIZE = 16;
      final int SCREEN_WIDTH = 1280;
      final int SCREEN_HEIGHT = 1024;
      final int WORLD_WIDTH = 128;
      final int WORLD_HEIGHT = 128;

      final float OBJECT_START_X = 0f;
      final float OBJECT_START_Y  = 48f;

      setWorldBounds(0, 0, WORLD_WIDTH , WORLD_HEIGHT);
      setCameraBounds(0f, 0f, SCREEN_WIDTH, SCREEN_HEIGHT);

      // Positioning the game object
      GameObject object = new GameObject();
      object.setPosition(OBJECT_START_X, OBJECT_START_Y);
      object.setDimensions(BLOCKS_SIZE, BLOCKS_SIZE);

      // Basic camera setup
      camera.setStickToWorldBounds(true);
      camera.setTrackingTarget(object);
      camera.setDefaultZoomFactor(0.15f);
      camera.setTargetTrackingSpeed(0.5f);
      camera.setZoomScalingFactor(0f);
      camera.focusCentered(object);
      camera.update(1f);

      // Test camera positioning
      assertThat(camera.getPosition().x).isEqualTo(WORLD_WIDTH / 2f);
      assertThat(camera.getPosition().y).isEqualTo(56.0f);
      // Test camera down scaling
      assertThat(camera.getScaledCameraWidth()).isEqualTo(WORLD_WIDTH);
      assertThat(camera.getScaledCameraHeight()).isEqualTo(WORLD_HEIGHT / ((float)SCREEN_WIDTH / (float)SCREEN_HEIGHT));

      // Move the game object down by one cell
      object.setPosition(OBJECT_START_X, OBJECT_START_Y + BLOCKS_SIZE);

      camera.update(1f);

      // Verify that camera does not flicker
      assertThat(camera.getPosition().x).isEqualTo(WORLD_WIDTH / 2f);
      assertThat(camera.getPosition().y).isEqualTo(76.8f);
      camera.update(1f);
      assertThat(camera.getPosition().y).isEqualTo(76.8f);
   }

   private void setCameraBounds(float x, float y, float width, float height) {
      orthographicCamera.position.x = x;
      orthographicCamera.position.y = y;
      orthographicCamera.viewportWidth = width;
      orthographicCamera.viewportHeight = height;
   }

   private void setWorldBounds(float x, float y, float width, float height) {
      when(bounds.getWorldOffsetX()).thenReturn(x);
      when(bounds.getWorldOffsetY()).thenReturn(y);
      when(bounds.getWorldWidth()).thenReturn(width);
      when(bounds.getWorldHeight()).thenReturn(height);
      when(gameWorld.getBounds()).thenReturn(bounds);
   }
}