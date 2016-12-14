package de.bitbrain.braingdx.behavior.movement;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class VelocityMovementBehavior extends BehaviorAdapter implements Movement<Vector2> {

    private final Vector2 velocity = new Vector2(0f, 0f);

    @Override
    public void move(Vector2 velocity) {
	this.velocity.x = velocity.x;
	this.velocity.y = velocity.y;
    }

    @Override
    public void update(GameObject source, float delta) {
	source.setPosition(source.getLeft() + velocity.x * delta, source.getTop() + velocity.y * delta);
    }

    @Override
    public boolean isMoving() {
	return velocity.len() > 0f;
    }

}
