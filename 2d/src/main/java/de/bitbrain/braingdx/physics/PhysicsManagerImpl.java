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

   public PhysicsManagerImpl(World physicsWorld, GameWorld gameWorld, BehaviorManager behaviorManager) {
      this.gameWorld = gameWorld;
      this.behaviorManager = behaviorManager;
      this.physicsWorld = physicsWorld;
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
      GameObject object = gameWorld.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setPosition(bodyDef.position.x, bodyDef.position.y);
         }
      });
      object.setType(type);
      return attachBody(bodyDef, fixtureDef, object);
   }

   @Override
   public Body addBody(final BodyDef bodyDef, float width, float height, Object type) {
      GameObject object = gameWorld.addObject(new Mutator<GameObject>() {
         @Override
         public void mutate(GameObject target) {
            target.setPosition(bodyDef.position.x, bodyDef.position.y);
         }
      });
      FixtureDef fixtureDef = new FixtureDef();
      PolygonShape shape = new PolygonShape();
      shape.setAsBox(width / 2f, height / 2f);
      fixtureDef.shape = shape;
      fixtureDef.density = 1f;
      object.setType(type);
      object.setDimensions(width, height);
      return attachBody(bodyDef, fixtureDef, object);
   }

   @Override
   public Body attachBody(BodyDef bodyDef, FixtureDef fixtureDef, GameObject gameObject) {
      final Body body = physicsWorld.createBody(bodyDef);
      body.createFixture(fixtureDef);
      behaviorManager.apply(new BodyPhysicsBehavior(body), gameObject);
      body.setUserData(gameObject);
      fixtureDef.shape.dispose();
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
