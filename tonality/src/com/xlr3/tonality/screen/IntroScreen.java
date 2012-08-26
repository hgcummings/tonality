package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xlr3.tonality.Constants;

public class IntroScreen extends AbstractScreen {
    public static interface ExitListener {
        public void exit();
    }

    public IntroScreen(final ExitListener exitListener) {
        Table table = new Table(getSkin());

        table.setWidth(Constants.GAME_VIEWPORT_WIDTH);
        table.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        table.row().padBottom(50);
        table.add("t o n a l i t y | evolution").colspan(3);

        table.row();
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("sprite/intro.atlas"));
        for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
            table.add(new Image(new TextureRegionDrawable(region)));
        }

        table.row().padBottom(50);

        table.add(TableUtils.createLabel("Touch any bacterium\nto hear its sequence", getSkin()));
        table.add(TableUtils.createLabel("Transcribe sequences\nin the grid on the right", getSkin())).padLeft(50).padRight(50);
        table.add(TableUtils.createLabel("Use the Dispatch button\nto kill bacteria\nwith a matching sequence", getSkin())).pad(50);

        table.row();

        TextButton button = new TextButton("Start!", getSkin());

        table.add(button).colspan(3);

        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                exitListener.exit();
                return true;
            }
        });

        stage.addActor(table);
    }
}
