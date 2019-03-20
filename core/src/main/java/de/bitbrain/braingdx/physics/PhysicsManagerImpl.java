package de.bitbrain.braingdx.physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.util.Updateable;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

public class PhysicsManagerImpl implements PhysicsManager, Updateable, Disposable {

   static final float TIME_STEP = 1f / 60f;

   private final GameWorld gameWorld;
   private final BehaviorManager behaviorManager;
   private final World physicsWorld;
   private final Vector2 gravity = new Vector2();
   private int positionIterations = 10;
   private int velocityIterations = 10;

   public PhysicsManagerImpl(GameWorld gameWorld, BehaviorManager behaviorManager) {
      this.gameWorld = gameWorld;
      this.behaviorManager = behaviorManager;
      physicsWorld = new World(gravity, true);
   }


   @Override
   public void dispose() {
      physicsWorld.dispose();
   }

   @Override
   public void update(float delta) {
      physicsWorld.step(TIME_STEP, velocityIterations, positionIterations);
   }

   @Override
   public Body addBody(final BodyDef bodyDef, FixtureDef fixtureDef, Object type) {
      Body body = physicsWorld.createBody(bodyDef);
      body.createFixture(fixtureDef);
      GameObject object = gameWorld.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setPosition(bodyDef.position.x, bodyDef.position.y);
         }
      });
      object.setType(type);
      behaviorManager.apply(new BodyPhysicsBehavior(body), object);
      body.setUserData(object);
      return body;
   }

   @Override
   public Body addBody(final BodyDef bodyDef, float width, float height, Object type) {
      final Body body = physicsWorld.createBody(bodyDef);
      PolygonShape shape = new PolygonShape();
      shape.setAsBox(width / 2f, height / 2f);
      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.shape = shape;
      fixtureDef.density = 1f;
      body.createFixture(fixtureDef);
      GameObject object = gameWorld.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setPosition(bodyDef.position.x, bodyDef.position.y);
         }
      });
      object.setType(type);
      object.setDimensions(width, height);
      behaviorManager.apply(new BodyPhysicsBehavior(body), object);
      body.setUserData(object);
      shape.dispose();
      return body;
   }

   @Override
   public void setGravity(float x, float y) {
      gravity.set(x, y);
      physicsWorld.setGravity(gravity);
   }

   @Override
   public void setPositionIterations(int positionIterations) {
      this.positionIterations = positionIterations;
   }

   @Override
   public void setVelocityIterations(int velocityIterations) {
      this.velocityIterations = velocityIterations;
   }
}
