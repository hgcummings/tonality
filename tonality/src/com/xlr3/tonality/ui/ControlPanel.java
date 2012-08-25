package com.xlr3.tonality.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.xlr3.tonality.Constants;

public class ControlPanel {
    private final Skin skin;
    private final Table table;
    private final Listener listener;

    public static enum EventType {
        TEST,
        DISPATCH
    }

    public static interface Listener {
        public void raise(EventType eventType);
    }

    public ControlPanel(
            Skin skin,
            int notes,
            int ticks,
            Listener listener
        )
    {
        this.skin = skin;
        this.table = buildTable(notes, ticks);
        this.listener = listener;
    }

    public boolean getActive(int note, int tick)
    {
        return buttons[note][tick].isChecked();
    }

    public Actor getActor()
    {
        return table;
    }

    private Button[][] buttons;

    private Table buildTable(int notes, int ticks) {
        Table table = new Table();

        table.setX(Constants.GAME_VIEWPORT_WIDTH / 2);
        table.setY(0);

        table.setWidth(Constants.GAME_VIEWPORT_WIDTH /2);
        table.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        buttons = new Button[notes][ticks];

        for (int note = 0; note < notes; note++)
        {
            for (int tick = 0; tick < ticks; tick++)
            {
                buttons[notes - note - 1][tick] = new Button(skin, "toggle");
                table.add(buttons[notes - note - 1][tick]).uniform().fill().expand();
            }
            table.row().expandX();
        }

        buildButton(table, ticks, "Test", EventType.TEST);
        buildButton(table, ticks, "Dispatch", EventType.DISPATCH);

        return table;
    }

    private void buildButton(Table table, int colSpan, String name, EventType eventType) {
        table.row().expandX();
        TextButton testButton = new TextButton(name, skin);
        testButton.addListener(new ButtonListener(eventType));
        table.add(testButton).colspan(colSpan).fillX();
    }

    private class ButtonListener extends InputListener
    {
        private EventType eventType;

        public ButtonListener(EventType eventType)
        {
            this.eventType = eventType;
        }

        @Override
        public boolean touchDown(
                InputEvent event,
                float x,
                float y,
                int pointer,
                int button )
        {
            listener.raise(eventType);
            return true;
        }
    }
}
