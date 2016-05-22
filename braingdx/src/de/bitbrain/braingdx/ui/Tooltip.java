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
