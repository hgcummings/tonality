package com.xlr3.tonality;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.xlr3.tonality.ui.DefaultInputListener;

public class MainScreen extends AbstractScreen {
    private Table table;

    public MainScreen() {
        table = new Table();

        table.setX(Constants.GAME_VIEWPORT_WIDTH / 2);
        table.setY(0);

        table.setWidth(Constants.GAME_VIEWPORT_WIDTH /2);
        table.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        int notes = 5;
        int ticks = 5;

        for (int note = 0; note < notes; note++)
        {
            for (int tick = 0; tick < ticks; tick++)
            {
                table.add(createGridButton(note, tick)).uniform().fill().expand();
            }
            table.row().expandX();
        }

        table.row().expandX();
        TextButton testButton = new TextButton("Test", getSkin());
        testButton.addListener(new DefaultInputListener());
        table.add(testButton).colspan(ticks).fillX();


        table.row().expandX();
        table.add(new TextButton("Dispatch", getSkin())).colspan(ticks).fillX();

        stage.addActor(table);
    }

    private Button createGridButton(int note, int tick) {
        Button button = new Button(getSkin(), "toggle");
        button.addListener(new DefaultInputListener());
        return button;
    }
}
