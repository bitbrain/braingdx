/* brainGDX project provides utilities for libGDX
 * Copyright (C) 2015 Miguel Gonzalez
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bitbrain.braingdx.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.GameObject;

/**
 * Manages rendering of game objects
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class RenderManager {
    private static Map<Integer, Renderer> rendererMap = new HashMap<Integer, Renderer>();

    public void render(GameObject object, Batch batch, float delta) {
        Renderer renderer = rendererMap.get(object.getType());
        if (renderer != null) {
            renderer.render(object, batch, delta);
        }
    }

    public void register(Integer gameObjectType, Renderer renderer) {
        if (!rendererMap.containsKey(gameObjectType)) {
            renderer.init();
            rendererMap.put(gameObjectType, renderer);
        }
    }

    public static interface Renderer {

        void init();

        void render(GameObject object, Batch batch, float delta);
    }
}
