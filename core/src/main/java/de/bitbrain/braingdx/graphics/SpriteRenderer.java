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

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.bitbrain.braingdx.GameObject;
import de.bitbrain.braingdx.assets.SharedAssetManager;

/**
 * renderer implementation for sprites
 *
 * @since 1.0.0
 * @version 1.0.0
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
	sprite.setPosition(object.getLeft() + object.getOffset().x, object.getTop() + object.getOffset().y);
	sprite.setSize(object.getWidth(), object.getHeight());
	sprite.setColor(object.getColor());
	sprite.setScale(object.getScale().x, object.getScale().y);
	sprite.draw(batch, 1f);
    }
}
