/* Copyright 2016 Miguel Gonzalez Sanchez
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

package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 * Contains utils for graphics creation
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public final class GraphicsFactory {

    /**
     * Creates a new texture of the given color and size
     *
     * @param width width of the texture
     * @param height height of the texture
     * @param color color of the texture
     * @return new texture object
     */
    public static Texture createTexture(int width, int height, Color color) {
	final Pixmap map = new Pixmap(width, height, Pixmap.Format.RGBA8888);
	map.setColor(color);
	map.fill();
	final Texture texture = new Texture(map);
	map.dispose();
	return texture;
    }

    /**
     * Creates a valid NinePatch from a texture with the given radius.
     * 
     * @param texture the texture
     * @param borderRadius the border radius
     * @return a new NinePatch instance
     */
    public static NinePatch createNinePatch(Texture texture, int borderRadius) {
	return new NinePatch(texture, borderRadius, borderRadius, borderRadius, borderRadius);
    }

    /**
     * Creates a valid NinePatch from a texture with the given radius and the given color.
     * 
     * @param texture the texture
     * @param borderRadius the border radius
     * @param color the color
     * @return a new NinePatch instance
     */
    public static NinePatch createNinePatch(Texture texture, int borderRadius, Color color) {
	NinePatch patch = GraphicsFactory.createNinePatch(texture, borderRadius);
	patch.setColor(color.cpy());
	return patch;
    }
}
