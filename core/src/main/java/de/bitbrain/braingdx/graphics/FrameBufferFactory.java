package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;

/**
 * Creates frame buffers
 *
 * @author Miguel Gonzalez Sanchez
 * @since 1.0.0
 */
public interface FrameBufferFactory {

   FrameBuffer create(int width, int height);
}
