package com.xlr3.tonality;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * The base class for all game screens.
 */
public abstract class AbstractScreen
        implements
        Screen
{
    protected final BitmapFont font;
    protected final SpriteBatch batch;
    protected final Stage stage;

    private Skin skin;

    public AbstractScreen()
    {
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        this.stage = new Stage(Constants.GAME_VIEWPORT_WIDTH, Constants.GAME_VIEWPORT_HEIGHT, true);
    }

    @Override
    public void show()
    {
    }

    @Override
    public void resize(
            int width,
            int height )
    {
        // resize the stage
        stage.setViewport( width, height, true );
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(
            float delta )
    {
        // the following code clears the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void pause()
    {
    }

    @Override
    public void resume()
    {
    }

    @Override
    public void dispose()
    {
        font.dispose();
        batch.dispose();
    }

    protected Skin getSkin()
    {
        if( skin == null ) {
            FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
            skin = new Skin(skinFile);
        }
        return skin;
    }
}
