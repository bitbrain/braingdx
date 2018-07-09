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

package de.bitbrain.braingdx.audio;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.braingdx.tweens.MusicTween;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This audio manager implementation provides utility methods to fade between different music and
 * sounds.
 * 
 * @author Miguel Gonzalez Sanchez
 * @since 1.0
 * @version 1.0
 */
public class AudioManagerImpl implements AudioManager {

   private final static float DEFAULT_DURATION = 1f;
   private final static float DEFAULT_VOLUME = 1f;
   
   private final Vector3 tmp = new Vector3();
   
   private final TweenManager tweenManager;
   private final AssetManager assetManager;
   private final GameWorld gameWorld;
   private final BehaviorManager behaviorManager;
   private final GameCamera camera;
   private float volume;
   
   private final Map<String, Music> musicObjects = new HashMap<String, Music>();
   private final Map<String, Sound> soundObjects = new HashMap<String, Sound>();
   private final Map<String, Long> soundHandles = new HashMap<String, Long>();

   public AudioManagerImpl(GameCamera gameCamera, TweenManager tweenManager, AssetManager assetManager, GameWorld gameWorld, BehaviorManager behaviorManager) {
      this.tweenManager = tweenManager;
      this.assetManager = assetManager;
      this.gameWorld = gameWorld;
      this.behaviorManager = behaviorManager;
      this.volume = DEFAULT_VOLUME;
      this.camera = gameCamera;
      Tween.registerAccessor(MusicClassWrapper.class, new MusicTween());
   }

   @Override
   public void spawnSound(String path, float x, float y, float pitch, final float volume, float hearingRange) {
      final Sound sound = assetManager.get(path, Sound.class);
      final long handle = sound.play(volume, pitch, 0f);
      float computedPan = computePan(x, y, hearingRange);
      float computedVolume = computeVolume(x, y, hearingRange) * volume * this.volume;
      sound.setPan(handle, computedPan, computedVolume);
   }

   @Override
   public void spawnSoundLooped(String path, float x, float y, float pitch, final float volume, final float hearingRange) {
      GameObject soundObject = gameWorld.addObject(true);
      soundObject.setPersistent(true);
      soundObject.setActive(false);
      soundObject.setPosition(x, y);
      spawnSoundLooped(path, soundObject, pitch, volume, hearingRange);
   }
   
   @Override
   public void spawnSoundLooped(String path, GameObject target, float pitch, final float volume, final float hearingRange) {
      final Sound sound = assetManager.get(path, Sound.class);
      final long handle = sound.play(0f, pitch, 0f);
      sound.setLooping(handle, true);
      behaviorManager.apply(new BehaviorAdapter() {
         @Override
         public void update(GameObject source, float delta) {
            float computedPan = computePan(source.getLeft(), source.getTop(), hearingRange);
            float computedVolume = computeVolume(source.getLeft(), source.getTop(), hearingRange) * volume * AudioManagerImpl.this.volume;
            sound.setPan(handle, computedPan, computedVolume);
         }
      }, target);
   }

   @Override
   public void spawnMusic(String path, float x, float y, final float volume, final float hearingRange) {
      GameObject soundObject = gameWorld.addObject(true);
      soundObject.setPersistent(true);
      soundObject.setActive(false);
      soundObject.setPosition(x, y);
      soundObject.setDimensions(1, 1);
      final Music music = assetManager.get(path, Music.class);
      music.play();
      music.setLooping(true);
      behaviorManager.apply(new BehaviorAdapter() {
         @Override
         public void update(GameObject source, float delta) {
            float computedPan = computePan(source.getLeft(), source.getTop(), hearingRange);
            float computedVolume = computeVolume(source.getLeft(), source.getTop(), hearingRange) * volume * AudioManagerImpl.this.volume;
            music.setPan(computedPan, computedVolume);
         }
      }, soundObject);
   }

   @Override
   public void clearMusic() {
      for (Entry<String, Music> musicEntry : musicObjects.entrySet()) {
         GameObject object = gameWorld.getObjectById(musicEntry.getKey());
         if (object != null) {
            Gdx.app.debug("AudioManager", "Remove music object " + object);
            gameWorld.remove(object);
         } else {
            Gdx.app.debug("AudioManager", "Unable to remove music object - not found!");
         }
         musicEntry.getValue().stop();
         musicEntry.getValue().setPosition(0f);
      }
      musicObjects.clear();
   }

   @Override
   public void clearSounds() {
      for (Entry<String, Sound> soundEntry : soundObjects.entrySet()) {
         GameObject object = gameWorld.getObjectById(soundEntry.getKey());
         if (object != null) {
            Gdx.app.debug("AudioManager", "Remove sound object " + object);
            gameWorld.remove(object);
         } else {
            Gdx.app.debug("AudioManager", "Unable to remove sound object - not found!");
         }
         long soundHandle = soundHandles.get(soundEntry.getKey());
         soundEntry.getValue().stop(soundHandle);
      }
      soundObjects.clear();
      soundHandles.clear();
   }

   @Override
   public void clear() {
      clearMusic();
      clearSounds();
   }

   @Override
   public void setMasterVolume(float volume) {
      this.volume = volume;
   }

   @Override
   public void crossFadeMusic(String fromPath, String toPath) {
      crossFadeMusic(fromPath, fromPath, DEFAULT_DURATION);
   }

   @Override
   public void crossFadeMusic(String fromPath, String toPath, float duration) {
      crossFadeMusic(assetManager.get(fromPath, Music.class), assetManager.get(toPath, Music.class), duration);
   }

   @Override
   public void crossFadeMusic(Music from, Music to, float duration) {
      fadeOutMusic(from, duration);
      fadeInMusic(to, duration);
   }

   @Override
   public void fadeOutMusic(final String path) {
      fadeOutMusic(path, DEFAULT_DURATION);
   }

   @Override
   public void fadeOutMusic(final String path, float duration) {
      fadeOutMusic(assetManager.get(path, Music.class), duration);
   }

   @Override
   public void fadeOutMusic(final Music music, float duration) {
	  final MusicClassWrapper wrapper = new MusicClassWrapper(music);
      Tween.to(wrapper, MusicTween.VOLUME, duration)
            .setCallbackTriggers(TweenCallback.COMPLETE)
            .target(0f)
            .setCallback(new TweenCallback() {
               @Override
               public void onEvent(int arg0, BaseTween<?> arg1) {
                  wrapper.stop();
               }
            })
            .ease(TweenEquations.easeNone)
            .start(tweenManager);
   }

   @Override
   public void fadeInMusic(String path) {
      fadeInMusic(path, DEFAULT_DURATION);
   }

   @Override
   public void fadeInMusic(String path, float duration) {
      fadeInMusic(assetManager.get(path, Music.class), duration);
   }

   @Override
   public void fadeInMusic(Music music, float duration) {
	  MusicClassWrapper wrapper = new MusicClassWrapper(music);
      wrapper.setVolume(0f);
      wrapper.setPosition(0f);
      wrapper.play();
      Tween.to(wrapper, MusicTween.VOLUME, duration)
           .target(volume)
           .ease(TweenEquations.easeNone)
           .start(tweenManager);
   }

   @Override
   public void playMusic(String path) {
      Music lastMusic = assetManager.get(path, Music.class);
      lastMusic.setVolume(volume);
      lastMusic.setPosition(0f);
      lastMusic.play();
   }

   @Override
   public void stopMusic(String path) {
      assetManager.get(path, Music.class).stop();
   }

   @Override
   public void pauseMusic(String path) {
      assetManager.get(path, Music.class).pause();
   }

   private static class MusicClassWrapper implements Music {

      private Music music;

      public MusicClassWrapper(Music music) {
         this.music = music;
      }

      @Override
      public void play() {
         music.play();
      }

      @Override
      public void pause() {
         music.pause();
      }

      @Override
      public void stop() {
         music.stop();
      }

      @Override
      public boolean isPlaying() {
         return music.isPlaying();
      }

      @Override
      public void setLooping(boolean isLooping) {
         music.setLooping(isLooping);
      }

      @Override
      public boolean isLooping() {
         return music.isLooping();
      }

      @Override
      public void setVolume(float volume) {
         music.setVolume(volume);
      }

      @Override
      public float getVolume() {
         return music.getVolume();
      }

      @Override
      public void setPan(float pan, float volume) {
         music.setPan(pan, volume);
      }

      @Override
      public void setPosition(float position) {
         music.setPosition(position);
      }

      @Override
      public float getPosition() {
         return music.getPosition();
      }

      @Override
      public void dispose() {
         music.dispose();
      }

      @Override
      public void setOnCompletionListener(OnCompletionListener listener) {
         music.setOnCompletionListener(listener);
      }

   }

   private float computeVolume(float sourceX, float sourceY, float maxDistance) {
      Camera camera = this.camera.getInternal();
      tmp.set(camera.position.x, camera.position.y, camera.position.z);
      tmp.x -= sourceX;
      tmp.y -= sourceY;
      return MathUtils.clamp(1 - (tmp.len() / maxDistance), 0.0f, 1f);
   }
   
   private float computePan(float sourceX, float sourceY, float maxDistance) {
      Camera camera = this.camera.getInternal();
      if (camera != null) {
         tmp.set(camera.up);
         tmp.crs(camera.direction);
         tmp.nor();
         final float lenX = camera.position.x - sourceX;
         final float lenY = camera.position.y - sourceY;
         final float lenZ = camera.position.z - 0f;
         final float clampX = tmp.dot(new Vector3(lenX, lenY, lenZ));
         return MathUtils.clamp(clampX / maxDistance, -1f, 1f);
     } else {
         return 0f;
      }
   }
}
