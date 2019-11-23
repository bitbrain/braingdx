package de.bitbrain.braingdx.tmx.events;

import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.event.GameEventListener;
import de.bitbrain.braingdx.tmx.TiledMapEvents;
import de.bitbrain.braingdx.world.GameObject;

public class TmxAudioConfigurer implements GameEventListener<TiledMapEvents.OnLoadGameObjectEvent> {

   private static final String AUDIO_FILE = "audio.file";
   private static final String AUDIO_RANGE = "audio.range";
   private static final String AUDIO_PITCH = "audio.pitch";
   private static final String AUDIO_VOLUME = "audio.volume";

   private static final float DEFAULT_AUDIO_RANGE = 256f;
   private static final float DEFAULT_AUDIO_PITCH = 0f;
   private static final float DEFAULT_AUDIO_VOLUME = 1f;

   private final AudioManager audioManager;

   public TmxAudioConfigurer(AudioManager audioManager) {
      this.audioManager = audioManager;
   }

   @Override
   public void onEvent(TiledMapEvents.OnLoadGameObjectEvent event) {
      GameObject object = event.getObject();
      if (object.hasAttribute(AUDIO_FILE)) {
         final String file = object.getAttribute(AUDIO_FILE, String.class);
         final float range = object.getAttribute(AUDIO_RANGE, DEFAULT_AUDIO_RANGE);
         final float pitch = object.getAttribute(AUDIO_PITCH, DEFAULT_AUDIO_PITCH);
         final float volume = object.getAttribute(AUDIO_VOLUME, DEFAULT_AUDIO_VOLUME);
         audioManager.spawnSoundLooped(file, object, pitch, volume, range);
      }
   }
}
