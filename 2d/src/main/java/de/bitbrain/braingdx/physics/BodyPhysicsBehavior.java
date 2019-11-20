package de.bitbrain.braingdx.physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class BodyPhysicsBehavior extends BehaviorAdapter {

   private final Body body;

   public BodyPhysicsBehavior(Body body) {
      this.body = body;
   }

   @Override
   public void onAttach(GameObject source) {
      super.onAttach(source);
   }

   @Override
   public void onDetach(GameObject source) {
      super.onDetach(source);
      this.body.getWorld().destroyBody(body);
   }

   @Override
   public void update(GameObject source, float delta) {
      super.update(source, delta);
      source.setPosition(body.getPosition().x, body.getPosition().y);
      source.setRotation(body.getAngle());
      if (!body.getFixtureList().isEmpty()) {
         Fixture fixture = body.getFixtureList().get(0);
         Shape shape = fixture.getShape();
         if (!(shape instanceof PolygonShape)) {
            source.setDimensions(shape.getRadius() * 2f, shape.getRadius() * 2f);
            source.setPosition(body.getPosition().x - shape.getRadius(), body.getPosition().y - shape.getRadius());
         }
         source.setPosition(body.getPosition().x - source.getWidth() / 2f, body.getPosition().y - source.getHeight() / 2f);
      }
   }
}
