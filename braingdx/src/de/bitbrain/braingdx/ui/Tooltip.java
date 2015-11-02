/* brainGDX project provides utilities for libGDX
 * Copyright (C) 2015 Miguel Gonzalez
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package de.bitbrain.braingdx.ui;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashSet;
import java.util.Set;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;

/**
 * Provides tooltips on the screen
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public class Tooltip {

    private static final Tooltip instance = new Tooltip();

    private TweenManager tweenManager = SharedTweenManager.getInstance();

    private Stage stage;

    private Camera camera;

    private Set<Label> tooltips = new HashSet<Label>();

    private TweenEquation equation;

    private float duration;

    private float scale;

    private Tooltip() {
        setTweenEquation(TweenEquations.easeOutCubic);
        duration = 0.9f;
        scale = 1.0f;
    }

    public static Tooltip getInstance() {
        return instance;
    }

    public void create(float x, float y, Label.LabelStyle style, String text) {
        create(x, y, style, text, Color.WHITE);
    }

    public void create(float x, float y, Label.LabelStyle style, String text, Color color) {
        final Label tooltip = new Label(text, style) {
            @Override
            public float getX() {
                return super.getX() - camera.position.x + camera.viewportWidth / 2f - this.getWidth() / 2f;
            }

            @Override
            public float getY() {
                return super.getY() - camera.position.y + camera.viewportHeight / 2f - this.getHeight() / 2f;
            }

            @Override
            public float getOriginX() {
                return super.getOriginX() + this.getWidth() / 2f;
            }

            @Override
            public float getOriginY() {
                return super.getOriginY() + this.getHeight() / 2f;
            }
        };
        tooltip.setColor(color);
        tooltip.setPosition(x, y);
        stage.addActor(tooltip);
        tooltips.add(tooltip);
        Tween.to(tooltip, ActorTween.ALPHA, this.duration).target(0f).setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        stage.getActors().removeValue(tooltip, true);
                    }
                }).ease(equation).start(tweenManager);
        Tween.to(tooltip, ActorTween.SCALE, this.duration).target(scale).ease(equation).start(tweenManager);
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public void setTweenEquation(TweenEquation equation) {
        this.equation = equation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void clear() {
        for (Label l : tooltips) {
            tweenManager.killTarget(l);
            stage.getActors().removeValue(l, true);
        }
        tooltips.clear();
    }

    public void init(Stage stage, Camera camera) {
        this.stage = stage;
        this.camera = camera;
    }

}