package de.bitbrain.braingdx.graphics.lighting;

import aurelienribon.tweenengine.TweenEquation;
import box2dLight.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import de.bitbrain.braingdx.world.GameObject;

/**
 * Manages box2d lights internally and stores them in memory.
 *
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public interface LightingManager {

   void setConfig(LightingConfig lightingConfig);

   void setAmbientLight(Color ambientLightColor);

   void setAmbientLight(Color color, float interval, TweenEquation equation);

   PointLight createPointLight(Vector2 pos, float distance, Color color);

   PointLight createPointLight(float x, float y, float distance, Color color);

   PointLight createPointLight(float distance, Color color);

   DirectionalLight createDirectionalLight(Color color, float degree);

   ChainLight createChainLight(float distance, int direction, Color color);

   ChainLight createChainLight(float distance, int direction, Color color, float... chain);

   ConeLight createConeLight(float distance, float directionDegree, float coneDegree,
                             Color color);

   ConeLight createConeLight(float x, float y, float distance, float directionDegree, float coneDegree,
                          Color color);

   ConeLight createConeLight(Vector2 pos, float distance, float directionDegree, float coneDegree,
                             Color color);

   void destroyLight(Light light);

   void clear();

   void attach(Light light, GameObject object);

   void attach(Light light, GameObject object, boolean centered);

   void attach(Light light, GameObject object, float offsetX, float offsetY);

   int size();
}
