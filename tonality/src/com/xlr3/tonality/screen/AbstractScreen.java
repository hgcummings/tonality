package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.xlr3.tonality.Globals;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen
        implements
        Screen {
    protected final Stage stage;

    private Skin skin;

    public AbstractScreen() {
        this.stage = new Stage(Globals.GAME_VIEWPORT_WIDTH, Globals.GAME_VIEWPORT_HEIGHT, true);
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(
            int width,
            int height) {
        // resize the stage
        stage.setViewport(width, height, true);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(
            float delta) {
        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        skin.dispose();
    }

    protected Skin getSkin() {
        if (skin == null) {
            FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
            skin = new Skin(skinFile);
        }
        return skin;
    }
}
