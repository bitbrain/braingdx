package de.bitbrain.braingdx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Basic implementation of a game object
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class GameObject implements Pool.Poolable {

    private Vector2 position, dimensions, lastPosition;

    private String id = "";

    private int type;

    private Color color = Color.WHITE.cpy();

    private Vector2 scale;

    public GameObject() {
        position = new Vector2();
        dimensions = new Vector2();
        lastPosition = new Vector2();
        scale = new Vector2(1f, 1f);
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

    @Override
    public void reset() {
        lastPosition.x = 0;
        lastPosition.y = 0;
        position.x = 0;
        position.y = 0;
        dimensions.x = 0;
        dimensions.y = 0;
        id = "";
        scale.set(1f, 1f);
        color = Color.WHITE.cpy();
    }
}
