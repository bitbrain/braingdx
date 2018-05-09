package de.bitbrain.braingdx.audio;

import com.badlogic.gdx.audio.Music;

/**
 * Provides a variety of audio controls such as spawning
 * audio, fading between audio etc.
 * 
 * @since 0.2.5
 * @author Miguel Gonzalez Sanchez
 */
public interface AudioManager {
   
   /**
    * plays a sound in the {@link GameWorld} relative to the {@link GameCamera}.
    * 
    * @param path the path of the audio file
    * @param x the horizontal position in the world
    * @param y the vertical position in the world
    * @param pitch the audio pitch
    * @param volume the audio volume. <code>FinalVolume=MasterVolume*volume*directionalVolume</code>
    * @param hearingRange the hearing range where the audio is hearable from
    */
   void spawnSound(String path, float x, float y, float pitch, float volume, float hearingRange);
   
   /**
    * Spawns a {@link Sound} in the {@link GameWorld} relative to the {@link GameCamera}. The sound object is removed as soon the sound has stopped playing.
    * 
    * @param path the path of the audio file
    * @param x the horizontal position in the world
    * @param y the vertical position in the world
    * @param pitch the audio pitch
    * @param volume the audio volume. <code>FinalVolume=MasterVolume*volume*directionalVolume</code>
    * @param hearingRange the hearing range where the audio is hearable from
    */
   void spawnSoundLooped(String path, float x, float y, float pitch, float volume, float hearingRange);
   
   /**
    * Spawns a {@link Music} in the world. The music persists until it is deleted via {@link clearMusic}.
    * 
    * @param path the path of the audio file
    * @param x the horizontal position in the world
    * @param y the vertical position in the world
    * @param pitch the audio pitch
    * @param volume the audio volume. <code>FinalVolume=MasterVolume*volume*directionalVolume</code>
    * @param hearingRange the hearing range where the audio is hearable from
    */
   void spawnMusic(String path, float x, float y, float volume, float hearingRange);
   
   /**
    * Clears all music and removes all related objects from the {@link GameWorld}.
    */
   void clearMusic();
   
   /**
    * Clears all sounds and removed all related objects from the {@link GameWorld}.
    */
   void clearSounds();
   
   /**
    * Clears all audio.
    */
   void clear();
   
   /**
    * Sets a new master volume. Master volume is by default set to <code>1.0</code>
    * 
    * @param volume the master volume
    */
   void setMasterVolume(float volume);
   
   /**
    * Fades music from one music to another
    * 
    * @param fromPath the source audio
    * @param toPath the target audio
    */
   void crossFadeMusic(String fromPath, String toPath);
   
   /**
    * Fades music from one music to another
    * 
    * @param fromPath the source audio
    * @param toPath the target audio
    * @param duration the duration in seconds
    */
   void crossFadeMusic(String fromPath, String toPath, float duration);
   
   /**
    * Fades music from one music to another
    * 
    * @param fromPath the source {@link Music}
    * @param toPath the target {@link Music}
    * @param duration the duration in seconds
    */
   void crossFadeMusic(Music from, Music to, float duration);
   
   /**
    * Fades out music
    * 
    * @param path the path to the music file
    */
   void fadeOutMusic(final String path);
   
   /**
    * Fades out music
    * 
    * @param path the path to the music file
    * @param duration the duration in seconds
    */
   void fadeOutMusic(final String path, float duration);
   
   /**
    * Fades out music
    * 
    * @param path the {@link Music} to fade out.
    * @param duration the duration in seconds
    */
   void fadeOutMusic(final Music music, float duration);
   
   /**
    * Fades in music
    * 
    * @param path the path to the music file
    */
   void fadeInMusic(String path);
   
   /**
    * Fades in music
    * 
    * @param path the path to the music file
    * @param duration the duration in seconds
    */
   void fadeInMusic(String path, float duration);
   
   /**
    * Fades in music
    * 
    * @param path the {@link Music} to fade in.
    * @param duration the duration in seconds
    */
   void fadeInMusic(Music music, float duration);
   
   /**
    * Plays a given music.
    * 
    * @param path the path to the audio file
    */
   void playMusic(String path);
   
   /**
    * Stops given music.
    * 
    * @param path the path to the audio file
    */
   void stopMusic(String path);
   
   /**
    * Pauses given music.
    * 
    * @param path the path to the audio file
    */
   void pauseMusic(String path);
}
