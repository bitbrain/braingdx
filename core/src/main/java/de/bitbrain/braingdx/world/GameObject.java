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

package de.bitbrain.braingdx.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Simple game object implementation which can be pooled
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez Sanchez
 */
public class GameObject implements Pool.Poolable {

    private static int COUNTER = 0;

    private final Vector2 position, dimensions, lastPosition, offset;

    private String id;

    private int type;

    private Color color = Color.WHITE.cpy();

    private final Vector2 scale;

    private float zIndex;

    public GameObject() {
	position = new Vector2();
	dimensions = new Vector2();
	lastPosition = new Vector2();
	offset = new Vector2();
	scale = new Vector2(1f, 1f);
	id = getClass().getCanonicalName() + "_" + String.valueOf(COUNTER++);
    }

    public void setType(int typeId) {
	this.type = typeId;
    }

    public int getType() {
	return type;
    }

    public void setDimensions(float width, float height) {
	this.dimensions.x = width;
	this.dimensions.y = height;
    }

    public void move(float x, float y) {
	setPosition(this.position.x + x, this.position.y + y);
    }

    public void setPosition(float x, float y) {
	this.lastPosition.x = this.position.x;
	this.lastPosition.y = this.position.y;
	this.position.x = x;
	this.position.y = y;
    }

    public float getLeft() {
	return this.position.x;
    }

    public float getTop() {
	return this.position.y;
    }

    public float getRight() {
	return getLeft() + getWidth();
    }

    public float getBottom() {
	return getTop() + getHeight();
    }

    public float getWidth() {
	return this.dimensions.x;
    }

    public float getHeight() {
	return this.dimensions.y;
    }

    public Vector2 getLastPosition() {
	return lastPosition;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public Color getColor() {
	return color;
    }

    public void setColor(Color color) {
	setColor(color.r, color.g, color.b, color.a);
    }

    public void setColor(float r, float g, float b, float a) {
	color.set(r, g, b, a);
    }

    public Vector2 getScale() {
	return scale;
    }

    public Vector2 getOffset() {
	return offset;
    }

    public void setOffset(float x, float y) {
	offset.x = x;
	offset.y = y;
    }

    public void setZIndex(float zIndex) {
	this.zIndex = zIndex;
    }

    public float getZIndex() {
	return this.zIndex;
    }

    @Override
    public void reset() {
	lastPosition.x = 0;
	lastPosition.y = 0;
	position.x = 0;
	position.y = 0;
	dimensions.x = 0;
	dimensions.y = 0;
	offset.x = 0;
	offset.y = 0;
	zIndex = 0;
	id = getClass().getCanonicalName() + "_" + String.valueOf(COUNTER++);
	scale.set(1f, 1f);
	color = Color.WHITE.cpy();
    }
}
