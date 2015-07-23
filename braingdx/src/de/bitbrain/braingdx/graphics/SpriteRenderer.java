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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.assets.SharedAssetManager;

/**
 * Renders a sprite
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class SpriteRenderer implements RenderManager.Renderer {

    protected Sprite sprite;

    private AssetManager assets = SharedAssetManager.getInstance();

    private String textureId;

    public SpriteRenderer(String textureId) {
        this.textureId = textureId;
    }

    @Override
    public void init() {
        sprite = new Sprite(assets.get(textureId, Texture.class));
    }

    @Override
    public void render(GameObject object, Batch batch, float delta) {
        sprite.setPosition(object.getLeft(), object.getTop());
        sprite.setSize(object.getWidth(), object.getHeight());
        sprite.setColor(object.getColor());
        sprite.setScale(object.getScale().x, object.getScale().y);
        sprite.draw(batch, 1f);
    }
}
