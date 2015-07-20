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

/**
 * Abstract base class for screens
 *
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

    @Override
    public final void show() {
        camera = new OrthographicCamera();
        world = new GameWorld(camera);
        batch = new SpriteBatch();
    }

    @Override
    public final void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        stage.act(delta);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            beforeWorldRender(batch, delta);
            world.updateAndRender(batch, delta);
            afterWorldRender(batch, delta);
        batch.end();
        stage.draw();
    }

    @Override
    public final void resize(int width, int height) {
        if (stage == null) {
            stage = new Stage(getViewport(width, height));
            onCreateStage(stage, width, height);
        } else {
            stage.getViewport().update(width, height);
        }
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
