package com.xlr3.tonality.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.screen.ButtonListener;
import com.xlr3.tonality.screen.GenericListener;
import com.xlr3.tonality.service.Sequence;

public class ControlPanel implements Sequence {
    private final Skin skin;
    private final Table table;
    private final GenericListener<EventType> listener;
    private final int ticks;

    public static enum EventType {
        TEST,
        DISPATCH
    }

    public ControlPanel(
            Skin skin,
            int notes,
            int ticks,
            GenericListener<EventType> listener
        )
    {
        this.skin = skin;
        this.listener = listener;
        this.ticks = ticks;
        this.table = buildTable(notes, ticks);
    }

    public boolean getActive(int note, int tick)
    {
        return noteButtons[note][tick].isChecked();
    }

    @Override
    public int getLength() {
        return ticks;
    }

    public Actor getActor()
    {
        return table;
    }

    private Button[][] noteButtons;

    private Table buildTable(int notes, int ticks) {
        Table table = new Table();

        table.setX(Constants.GAME_VIEWPORT_WIDTH / 2);
        table.setY(0);

        table.setWidth(Constants.GAME_VIEWPORT_WIDTH /2);
        table.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        noteButtons = new Button[notes][ticks];

        for (int note = 0; note < notes; note++)
        {
            for (int tick = 0; tick < ticks; tick++)
            {
                noteButtons[notes - note - 1][tick] = new Button(skin, "toggle");
                table.add(noteButtons[notes - note - 1][tick]).uniform().fill().expand();
            }
            table.row().expandX();
        }

        buildButton(table, ticks, "Test", EventType.TEST);
        buildButton(table, ticks, "Dispatch", EventType.DISPATCH);

        return table;
    }

    private Button buildButton(Table table, int colSpan, String name, EventType eventType) {
        table.row().expandX();
        TextButton button = new TextButton(name, skin);
        button.addListener(new ButtonListener<EventType>(eventType, this.listener));
        table.add(button).colspan(colSpan).fillX();
        return button;
    }


}
