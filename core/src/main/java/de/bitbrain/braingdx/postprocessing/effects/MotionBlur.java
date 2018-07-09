
package de.bitbrain.braingdx.postprocessing.effects;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import de.bitbrain.braingdx.postprocessing.PostProcessorEffect;
import de.bitbrain.braingdx.postprocessing.filters.Copy;
import de.bitbrain.braingdx.postprocessing.filters.MotionFilter;

/**
 * A motion blur effect which draws the last frame with a lower opacity. The result is then stored
 * as the next last frame to create the trail effect.
 * 
 * @author Toni Sagrista
 */
public class MotionBlur extends PostProcessorEffect {
   private MotionFilter motionFilter;
   private Copy copyFilter;
   private FrameBuffer fbo;

   public MotionBlur() {
      motionFilter = new MotionFilter();
      copyFilter = new Copy();
   }

   public void setBlurOpacity(float blurOpacity) {
      motionFilter.setBlurOpacity(blurOpacity);
   }
   
   public float getBlurOpacity() {
	   return motionFilter.getBlurOpacity();
   }

   @Override
   public void dispose() {
      if (motionFilter != null) {
         motionFilter.dispose();
         motionFilter = null;
      }
   }

   @Override
   public void rebind() {
      motionFilter.rebind();
   }

   @Override
   public void render(FrameBuffer src, FrameBuffer dest) {
      restoreViewport(dest);
      if (dest != null) {
         motionFilter.setInput(src).setOutput(dest).render();
         fbo = dest;
      } else {
         if (fbo == null) {
            // Init frame buffer
            fbo = new FrameBuffer(Format.RGBA8888, src.getWidth(), src.getHeight(), false);
         }
         motionFilter.setInput(src).setOutput(fbo).render();

         // Copy fbo to screen
         copyFilter.setInput(fbo).setOutput(dest).render();
      }

      // Set last frame
      motionFilter.setLastFrameTexture(fbo.getColorBufferTexture());

   }

}
