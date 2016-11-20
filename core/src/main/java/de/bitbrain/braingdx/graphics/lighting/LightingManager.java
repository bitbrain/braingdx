/* Copyright 2016 Miguel Gonzalez Sanchez
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
package de.bitbrain.braingdx.graphics.lighting;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import de.bitbrain.braingdx.graphics.pipeline.RenderLayer;

/**
 * Manages box2d lights internally and stores them in memory.
 * 
 * @author Miguel Gonzalez Sanchez
 * @version 1.0.0
 */
public class LightingManager implements RenderLayer {

    public class LightingConfig {
	boolean shadows = true;
	boolean diffuseLighting = true;
	boolean blur = true;
	boolean culling = true;
	boolean gammaCorrection = true;

	public LightingConfig shadows(boolean enabled) {
	    this.shadows = enabled;
	    return this;
	}

	public LightingConfig diffuseLighting(boolean enabled) {
	    this.diffuseLighting = enabled;
	    return this;
	}

	public LightingConfig blur(boolean enabled) {
	    this.blur = enabled;
	    return this;
	}

	public LightingConfig culling(boolean enabled) {
	    this.culling = enabled;
	    return this;
	}

	public LightingConfig gammaCorrection(boolean enabled) {
	    this.gammaCorrection = enabled;
	    return this;
	}

    }

    private static final int DEFAULT_RAYS = 80;

    private final RayHandler handler;

    private final Map<String, PointLight> pointLights = new HashMap<String, PointLight>();

    private final Map<String, DirectionalLight> dirLights = new HashMap<String, DirectionalLight>();

    private final Map<String, ChainLight> chainLights = new HashMap<String, ChainLight>();

    private final Map<String, ConeLight> coneLights = new HashMap<String, ConeLight>();

    private final OrthographicCamera camera;

    public LightingManager(World world, OrthographicCamera camera) {
	this.handler = new RayHandler(world);
	this.camera = camera;
	setConfig(new LightingConfig());
    }

    public void setConfig(LightingConfig lightingConfig) {
	this.handler.setShadows(lightingConfig.shadows);
	this.handler.setBlur(lightingConfig.blur);
	this.handler.setCulling(lightingConfig.culling);
	RayHandler.setGammaCorrection(lightingConfig.gammaCorrection);
	RayHandler.useDiffuseLight(lightingConfig.diffuseLighting);
    }

    public void setAmbientLight(Color color) {
	handler.setAmbientLight(color);
    }

    public PointLight addPointLight(String id, Vector2 pos, float distance, Color color) {
	return addPointLight(id, pos.x, pos.y, distance, color);
    }

    public PointLight addPointLight(String id, float x, float y, float distance, Color color) {
	PointLight light = new PointLight(handler, DEFAULT_RAYS, color, distance, x, y);
	pointLights.put(id, light);
	return light;
    }

    public DirectionalLight addDirectionalLight(String id, Color color, float degree) {
	DirectionalLight light = new DirectionalLight(handler, DEFAULT_RAYS, color, degree);
	dirLights.put(id, light);
	return light;
    }

    public ChainLight addChainLight(String id, float distance, int direction, Color color) {
	return addChainLight(id, distance, direction, color);
    }

    public ChainLight addChainLight(String id, float distance, int direction, Color color, float... chain) {
	ChainLight light = new ChainLight(handler, DEFAULT_RAYS, color, distance, direction, chain);
	chainLights.put(id, light);
	return light;
    }

    public ConeLight addConeLight(String id, float x, float y, float distance, float directionDegree, float coneDegree,
	    Color color) {
	ConeLight light = new ConeLight(handler, DEFAULT_RAYS, color, distance, x, y, directionDegree, coneDegree);
	coneLights.put(id, light);
	return light;
    }

    public void removePointLight(String id) {
	PointLight light = pointLights.remove(id);
	if (light != null) {
	    light.remove();
	}
    }

    public void removeDirectionalLight(String id) {
	DirectionalLight light = dirLights.remove(id);
	if (light != null) {
	    light.remove();
	}
    }

    public void removeChainLight(String id) {
	ChainLight light = chainLights.remove(id);
	if (light != null) {
	    light.remove();
	}
    }

    public void removeConeLight(String id) {
	ConeLight light = coneLights.remove(id);
	if (light != null) {
	    light.remove();
	}
    }

    @Override
    public void render(Batch batch, float delta) {
	handler.setCombinedMatrix(camera);
	handler.updateAndRender();
    }

    /**
     * Lighting does not like shading per-se
     */
    @Override
    public boolean hasShaderSupport() {
	return false;
    }
}