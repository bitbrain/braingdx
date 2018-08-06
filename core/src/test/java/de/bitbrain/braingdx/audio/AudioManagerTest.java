package de.bitbrain.braingdx.audio;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.assets.AssetManager;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.world.GameWorld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AudioManagerTest {

   @InjectMocks
   private AudioManagerImpl impl;

   @Mock
   private TweenManager tweenManager;

   @Mock
   private AssetManager assetManager;

   @Mock
   private GameCamera gameCamera;

   @Mock
   private GameWorld gameWorld;

   @Mock
   private BehaviorManager behaviorManager;

   @Test
   public void testSpawnMusic() {
      // TODO
   }

   @Test
   public void testSpawnSound() {
      // TODO
   }

}
