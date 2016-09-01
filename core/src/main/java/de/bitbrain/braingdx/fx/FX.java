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

package de.bitbrain.braingdx.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.tweening.Tween;
import de.bitbrain.braingdx.tweening.TweenEquation;
import de.bitbrain.braingdx.tweening.TweenEquations;
import de.bitbrain.braingdx.tweening.TweenManager;
import de.bitbrain.braingdx.tweening.tweens.SpriteTween;

/**
 * Provides special effect utilities
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public final class FX {

    private static final FX INSTANCE = new FX();

    private TweenManager tweenManager;

    private ScreenShake shake;

    private Sprite flash;

    private OrthographicCamera camera;

    private Color flashColor;

    private FX() {
	flash = new Sprite(GraphicsFactory.createTexture(2, 2, Color.WHITE));
	flash.setAlpha(0f);
	flashColor = Color.WHITE.cpy();
    }

    public static FX getInstance() {
	return INSTANCE;
    }

    public void setFadeColor(Color color) {
	flashColor = color.cpy();
    }

    public void begin() {
	if (camera != null && shake != null) {
	    camera.translate(shake.getShake().x, shake.getShake().y);
	}
    }

    public void end() {
	if (camera != null && shake != null) {
	    camera.translate(-shake.getShake().x, -shake.getShake().y);
	}
    }

    public void init(TweenManager tweenManager, OrthographicCamera camera) {
	this.tweenManager = tweenManager;
	shake = new ScreenShake(this.tweenManager);
	this.camera = camera;
    }

    public void render(Batch batch, float delta) {
	flash.setPosition(camera.position.x - (camera.zoom * camera.viewportWidth) / 2,
		camera.position.y - (camera.zoom * camera.viewportHeight) / 2);
	flash.setSize(camera.viewportWidth * camera.zoom, camera.viewportHeight * camera.zoom);
	flash.setColor(flashColor.r, flashColor.g, flashColor.b, flash.getColor().a);
	flash.draw(batch, 1f);
    }

    public void fadeIn(float duration, TweenEquation equation) {
	flash.setAlpha(1f);
	tweenManager.killTarget(flash);
	Tween.to(flash, SpriteTween.ALPHA, duration).target(0f).ease(equation).start(tweenManager);
    }

    public void fadeOut(float duration) {
	fadeOut(duration, TweenEquations.easeInQuad);
    }

    public void fadeOut(float duration, TweenEquation equation) {
	flash.setAlpha(0f);
	tweenManager.killTarget(flash);
	Tween.to(flash, SpriteTween.ALPHA, duration).target(1f).ease(equation).start(tweenManager);
    }

    public void fadeIn(float duration) {
	fadeIn(duration, TweenEquations.easeInQuad);
    }

    /**
     * Shakes the screen by the given intensity for the given duration
     *
     * @param intensity
     *            the intensity of the shake
     * @param duration
     *            the duration of the shake
     */
    public void shake(float intensity, float duration) {
	shake.shake(intensity, duration);
    }
}
