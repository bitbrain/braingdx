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

package de.bitbrain.braingdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.fx.FX;
import de.bitbrain.braingdx.ui.Tooltip;

/**
 * Abstract base class for screens
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public abstract class AbstractScreen<T extends BrainGdxGame> implements Screen {

    protected T game;

    protected GameWorld world;

    protected OrthographicCamera camera;

    private Color backgroundColor = Color.BLACK.cpy();

    private Batch batch;

    private Stage stage;

    public AbstractScreen(T game) {
        this.game = game;
    }

    public T getGame() {
        return game;
    }

    protected TweenManager tweenManager;

    protected FX fx = FX.getInstance();

    protected Tooltip tooltip = Tooltip.getInstance();

    @Override
    public final void show() {
        camera = new OrthographicCamera();
        world = new GameWorld(camera);
        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        fx.init(tweenManager, camera);
    }

    @Override
    public final void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
        fx.begin();
        camera.update();
        stage.act(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            beforeWorldRender(batch, delta);
            world.updateAndRender(batch, delta);
            afterWorldRender(batch, delta);
            fx.render(batch, delta);
        batch.end();
        stage.draw();
        fx.end();
    }

    @Override
    public final void resize(int width, int height) {
        if (stage == null) {
            stage = new Stage(getViewport(width, height));
            tooltip.init(tweenManager, stage, camera);
            onCreateStage(stage, width, height);
        } else {
            stage.getViewport().update(width, height);
        }
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    protected void beforeWorldRender(Batch batch, float delta) {

    }

    protected void afterWorldRender(Batch batch, float delta) {

    }

    protected void onCreateStage(Stage stage, int width, int height) {

    }

    protected Viewport getViewport(int width, int height) {
        return new ScreenViewport();
    }

    @Override
    public final void dispose() {
        world.reset();
        stage.dispose();
    }

    public void setBackgroundColor(Color color) {
        this.backgroundColor = color;
    }
}
