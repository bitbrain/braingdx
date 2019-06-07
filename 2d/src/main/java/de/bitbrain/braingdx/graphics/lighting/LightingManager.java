package de.bitbrain.braingdx.graphics.lighting;

import aurelienribon.tweenengine.TweenEquation;
import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

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

   PointLight addPointLight(String id, Vector2 pos, float distance, Color color);

   PointLight addPointLight(String id, float x, float y, float distance, Color color);

   DirectionalLight addDirectionalLight(String id, Color color, float degree);

   ChainLight addChainLight(String id, float distance, int direction, Color color);

   ChainLight addChainLight(String id, float distance, int direction, Color color, float... chain);

   ConeLight addConeLight(String id, float x, float y, float distance, float directionDegree, float coneDegree,
                          Color color);

   void removePointLight(final String id);

   void removeDirectionalLight(final String id);

   void removeChainLight(final String id);

   void removeConeLight(final String id);

   void clear();





}
