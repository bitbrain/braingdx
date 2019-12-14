/* Copyright 2017 Miguel Gonzalez Sanchez
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import de.bitbrain.braingdx.util.Factory;
import de.bitbrain.braingdx.util.Mutator;

/**
 * Simple game object implementation which can be pooled
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameObject implements Pool.Poolable {

    private final Vector2 position, dimensions, lastPosition, offset, origin, tmp;
    private final Vector2 scale;
    private String id;
    private Object type;
    private Color color = Color.WHITE.cpy();
    private float rotation;

    private float zIndex;

    private Map<Object, Object> attributes;

    private boolean active;

    private boolean persistent;

    private Rectangle rect = new Rectangle();

    private final Mutator<GameObject> mutator = new Mutator<GameObject>() {
        @Override
        public void mutate(GameObject target) {
            target.lastPosition.x = lastPosition.x;
            target.lastPosition.y = lastPosition.y;
            target.position.x = position.x;
            target.position.y = position.y;
            target.dimensions.x = dimensions.x;
            target.dimensions.y = dimensions.y;
            target.offset.x = offset.x;
            target.offset.y = offset.y;
            target.zIndex = zIndex;
            target.scale.set(scale.x, scale.y);
            target.color = color.cpy();
            target.attributes.putAll(attributes);
            target.active = active;
            target.rotation = rotation;
            target.origin.x = origin.x;
            target.origin.y = origin.y;
            target.type = type;
            target.persistent = persistent;
        }
    };

    public GameObject() {
        attributes = new HashMap<Object, Object>();
        position = new Vector2();
        dimensions = new Vector2();
        lastPosition = new Vector2();
        offset = new Vector2();
        scale = new Vector2(1f, 1f);
        origin = new Vector2();
        tmp = new Vector2();
        id = UUID.randomUUID().toString();
        active = true;
    }

    public GameObject copy() {
        GameObject object = new GameObject();
        mutator().mutate(object);
        return object;
    }

    public Mutator<GameObject> mutator() {
        return mutator;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object typeId) {
        this.type = typeId;
    }

    public void setDimensions(float width, float height) {
        this.dimensions.x = width;
        this.dimensions.y = height;
    }

    public void move(float x, float y) {
        setPosition(this.position.x + x, this.position.y + y);
    }

    public void setPosition(float x, float y) {
        setLastPosition(this.position.x, this.position.y);
        this.position.x = x;
        this.position.y = y;
    }

    public void setLastPosition(float x, float y) {
        this.lastPosition.x = this.position.x;
        this.lastPosition.y = this.position.y;
    }

    public void setLeft(float x) {
        this.position.x = x;
    }

    public void setTop(float y) {
        this.position.y = y;
    }

    public void setRight(float x) {
        this.position.x = x - getWidth();
    }

    public void setBottom(float y) {
        this.position.y = y - getHeight();
    }

    public void setOrigin(float x, float y) {
        this.origin.set(x, y);
    }

    public float getOriginX() {
        return origin.x;
    }

    public float getOriginY() {
        return origin.y;
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

    public float getLastLeft() {
        return lastPosition.x;
    }

    public float getLastTop() {
        return lastPosition.y;
    }

    public Vector2 getLastPosition() {
        return lastPosition;
    }

    public Vector2 getPosition() {
        tmp.set(position.x, position.y);
        return tmp.cpy();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            Gdx.app.log("ERROR", "Unable to assign id=" + id + " to game object " + toString() + ": invalid ID!");
            return;
        }
        this.id = id;
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

    public float getScaleX() {
        return scale.x;
    }

    public float getScaleY() {
        return scale.y;
    }

    public void scale(float scale) {
        this.scale.scl(scale);
    }

    public void setScaleX(float scaleX) {
        this.scale.x = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scale.y = scaleY;
    }

    public void setScale(float scale) {
        this.scale.x = scale;
        this.scale.y = scale;
    }

    public float getOffsetX() {
        return offset.x;
    }

    public float getOffsetY() {
        return offset.y;
    }

    public void setOffset(float x, float y) {
        offset.x = x;
        offset.y = y;
    }

    public void setAttribute(Object key, Object attribute) {
        if (attribute == null) {
            removeAttribute(key);
        } else {
            attributes.put(key, attribute);
        }
    }

    public void removeAttribute(Object key) {
        attributes.remove(key);
    }

    public boolean hasAttribute(Object key) {
        return attributes.containsKey(key);
    }

    public Object getAttribute(Object key) {
        return getAttribute(key, Object.class);
    }

    public <T> T getAttribute(Object key, Class<T> clazz) {
        return (T) attributes.get(key);
    }

    public <T> T getAttribute(Object key, T defaultValue) {
        if (!attributes.containsKey(key)) {
            return defaultValue;
        }
        return (T) attributes.get(key);
    }

    public <T> T getOrSetAttribute(Object key, T defaultValue) {
        T value = (T) attributes.get(key);
        if (value != null) {
            return value;
        }
        setAttribute(key, defaultValue);
        return defaultValue;
    }

    public <T> T getOrSetAttribute(Object key, Factory<T> defaultValueFactory) {
        T value = (T) attributes.get(key);
        if (value != null) {
            return value;
        }
        value = defaultValueFactory != null ? defaultValueFactory.create() : null;
        setAttribute(key, value);
        return value;
    }

    public float getZIndex() {
        return this.zIndex;
    }

    public void setZIndex(float zIndex) {
        this.zIndex = zIndex;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public float getRotation() {
        return this.rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void rotate(float delta) {
        this.rotation += delta;
    }

    public boolean hasMoved() {
        return position.x != lastPosition.x || position.y != lastPosition.y;
    }

    public boolean collidesWith(GameObject gameObject) {
        rect.set(getLeft(), getTop(), getWidth(), getHeight());
        gameObject.rect.set(gameObject.getLeft(), gameObject.getTop(), gameObject.getWidth(), gameObject.getHeight());
        return rect.contains(gameObject.rect) || rect.overlaps(gameObject.rect);
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
        id = UUID.randomUUID().toString();
        scale.set(1f, 1f);
        color = Color.WHITE.cpy();
        attributes.clear();
        active = true;
        rotation = 0f;
        origin.x = 0f;
        origin.y = 0f;
        type = null;
        persistent = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
       if (this == obj) {
          return true;
       }
       if (obj == null) {
          return false;
       }
       if (getClass() != obj.getClass()) {
          return false;
       }
        GameObject other = (GameObject) obj;
        if (id == null) {
           if (other.id != null) {
              return false;
           }
        } else if (!id.equals(other.id)) {
           return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GameObject [position=" + position + ", dimensions=" + dimensions + ", lastPosition=" + lastPosition + ", id=" + id + ", type=" + type + ", color=" + color + ", zIndex=" + zIndex + ", active=" + active + "]";
    }
}
