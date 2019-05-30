package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import de.bitbrain.braingdx.GameSettings;
import de.bitbrain.braingdx.event.GameEventManager;
import de.bitbrain.braingdx.graphics.event.GraphicsSettingsChangeEvent;
import de.bitbrain.braingdx.graphics.postprocessing.filters.RadialBlur;

import static com.badlogic.gdx.math.MathUtils.floor;

public class GraphicsSettings {

   public static final String ID = "graphics";

   private static final String RADIAL_BLUR_QUALITY = "radialBlurQuality";
   private static final String RENDER_SCALE = "renderScale";
   private static final String PARTICLE_MULTIPLIER = "particleMultiplier";

   private RadialBlur.Quality radialBlurQuality;
   private float renderScale;
   private float particleMultiplier;

   private final GameEventManager gameEventManager;

   public GraphicsSettings(GameEventManager gameEventManager) {
      this.gameEventManager = gameEventManager;
      Preferences prefs = getPreferences();
      String radialBlurName = prefs.getString(RADIAL_BLUR_QUALITY, RadialBlur.Quality.Medium.name());
      radialBlurQuality = RadialBlur.Quality.valueOf(radialBlurName);
      renderScale = prefs.getFloat(RENDER_SCALE, 1f);
      particleMultiplier = prefs.getFloat(PARTICLE_MULTIPLIER, 1f);
   }

   public void save() {
      Preferences prefs = getPreferences();
      prefs.putString(RADIAL_BLUR_QUALITY, radialBlurQuality.name());
      prefs.putFloat(RENDER_SCALE, renderScale);
      prefs.putFloat(PARTICLE_MULTIPLIER, particleMultiplier);
      prefs.flush();
   }

   public int getScaledRenderWidth() {
      return floor(Gdx.graphics.getWidth() * renderScale);
   }

   public int getScaledRenderHeight() {
      return floor(Gdx.graphics.getHeight() * renderScale);
   }

   public float getParticleMultiplier() {
      return particleMultiplier;
   }

   public GraphicsSettings setParticleMultiplier(float multiplier) {
      this.particleMultiplier = multiplier;
      return this;
   }

   public RadialBlur.Quality getRadialBlurQuality() {
      return radialBlurQuality;
   }

   public GraphicsSettings setRadialBlurQuality(RadialBlur.Quality quality) {
      this.radialBlurQuality = quality;
      gameEventManager.publish(new GraphicsSettingsChangeEvent());
      return this;
   }

   public float getRenderScale() {
      return renderScale;
   }

   public GraphicsSettings setRenderScale(float renderScale) {
      this.renderScale = renderScale;
      gameEventManager.publish(new GraphicsSettingsChangeEvent());
      return this;
   }

   private Preferences getPreferences() {
      return Gdx.app.getPreferences(GameSettings.ID + "." + ID);
   }
}
