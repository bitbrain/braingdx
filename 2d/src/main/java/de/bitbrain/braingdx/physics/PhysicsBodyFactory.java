package de.bitbrain.braingdx.physics;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.ArrayList;
import java.util.List;

public class PhysicsBodyFactory {

   public static PolygonShape getRectangle(Rectangle rectangle) {
      PolygonShape polygon = new PolygonShape();
      Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f),
            (rectangle.y + rectangle.height * 0.5f));
      polygon.setAsBox(rectangle.width * 0.5f,
            rectangle.height * 0.5f,
            size,
            0.0f);
      return polygon;
   }

   public static CircleShape getCircle(Circle circle) {
      CircleShape circleShape = new CircleShape();
      circleShape.setRadius(circle.radius);
      circleShape.setPosition(new Vector2(circle.x, circle.y));
      return circleShape;
   }

   public static List<PolygonShape> getPolygons(Polygon polygon) {
      List<PolygonShape> polygonShapes = new ArrayList<PolygonShape>();
      float[] vertices = polygon.getTransformedVertices();
      PolygonShape polygonShape = new PolygonShape();
      polygonShape.set(vertices);
      polygonShapes.add(polygonShape);
      return polygonShapes;
   }

   public static ChainShape getPolyline(Polyline polyline) {
      float[] vertices = polyline.getTransformedVertices();
      Vector2[] worldVertices = new Vector2[vertices.length / 2];

      for (int i = 0; i < vertices.length / 2; ++i) {
         worldVertices[i] = new Vector2();
         worldVertices[i].x = vertices[i * 2];
         worldVertices[i].y = vertices[i * 2 + 1];
      }

      ChainShape chain = new ChainShape();
      chain.createChain(worldVertices);
      return chain;
   }
}
