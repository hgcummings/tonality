package com.xlr3.tonality.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.service.Sequence;

public class ControlPanel implements Sequence {
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
        return noteButtons[note][tick].isChecked();
    }

    public Actor getActor()
    {
        return table;
    }

    public void activate() {
        dispatchButton.setDisabled(false);
    }

    private Button[][] noteButtons;
    private Button dispatchButton;

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
        dispatchButton = buildButton(table, ticks, "Dispatch", EventType.DISPATCH);
        dispatchButton.setDisabled(true);

        return table;
    }

    private Button buildButton(Table table, int colSpan, String name, EventType eventType) {
        table.row().expandX();
        TextButton button = new TextButton(name, skin);
        button.addListener(new ButtonListener(eventType));
        table.add(button).colspan(colSpan).fillX();
        return button;
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
            if (((Button)event.getListenerActor()).isDisabled()) {
                return false;
            }
            listener.raise(eventType);
            return true;
        }
    }
}
