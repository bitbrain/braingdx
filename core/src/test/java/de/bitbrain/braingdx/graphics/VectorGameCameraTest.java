package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VectorGameCameraTest {

   private static final float WORLD_WIDTH = 500;
   private static final float WORLD_HEIGHT = 800;

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
   }

   @Test
   public void testDefaultInitialization() {
      setCameraBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
      setWorldBounds(0f, 0f, WORLD_WIDTH, WORLD_HEIGHT);
      camera.update(0f);
      assertThat(orthographicCamera.position.len()).isZero();
      assertThat(orthographicCamera.viewportWidth).isEqualTo(WORLD_WIDTH);
      assertThat(orthographicCamera.viewportHeight).isEqualTo(WORLD_HEIGHT);
   }

   @Test
   public void testSetDefaultZoom() {
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
   public void testSetDefaultZoom_FocusCentered_NoTrackingTarget() {
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

      assertThat(orthographicCamera.position.x).isEqualTo(WORLD_WIDTH / 2f);
      assertThat(orthographicCamera.position.y).isEqualTo(WORLD_HEIGHT / 2f);
      assertThat(orthographicCamera.viewportWidth * orthographicCamera.zoom)
            .isEqualTo(WORLD_WIDTH);
      assertThat(orthographicCamera.viewportHeight * orthographicCamera.zoom)
            .isEqualTo(WORLD_HEIGHT);
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