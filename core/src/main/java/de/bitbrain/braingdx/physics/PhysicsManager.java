package de.bitbrain.braingdx.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

/**
 * Responsible for adding physics objects to the world
 *
 * @since 0.5.0
 */
public interface PhysicsManager {

   Body addBody(BodyDef bodyDef, FixtureDef fixtureDef, Object type);
   Body addBody(BodyDef bodyDef, float width, float height, Object type);
   void setGravity(float x, float y);
   void setPositionIterations(int positionIterations);
   void setVelocityIterations(int velocityIterations);
}
