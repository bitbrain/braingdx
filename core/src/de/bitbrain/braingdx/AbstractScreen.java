package de.bitbrain.braingdx;

import com.badlogic.gdx.Screen;

/**
 * Abstract base class for screens
 *
 * @author Miguel Gonzalez <miguel-gonzalez@gmx.de>
 */
public abstract class AbstractScreen<T extends BrainGdxGame> implements Screen {

    private T game;

    public AbstractScreen(T game) {
        this.game = game;
    }

    public T getGame() {
        return game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

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

    @Override
    public void dispose() {

    }
}
