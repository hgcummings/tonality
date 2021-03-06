package com.xlr3.tonality.domain;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.screen.ButtonListener;
import com.xlr3.tonality.screen.GenericListener;

public class ControlPanel implements Sequence {
    private final Skin skin;
    private final GenericListener<EventType> listener;
    private final int notes;
    private final int ticks;

    public static enum EventType {
        GO
    }

    public ControlPanel(
            Skin skin,
            int notes,
            int ticks,
            GenericListener<EventType> listener
    ) {
        this.skin = skin;
        this.listener = listener;
        this.notes = notes;
        this.ticks = ticks;
    }

    public boolean getActive(int note, int tick) {
        return noteButtons[note][tick].isChecked();
    }

    public void setActive(int note, int tick, boolean value) {
        noteButtons[note][tick].setChecked(value);
    }

    @Override
    public int getLength() {
        return ticks;
    }

    public Actor getActor() {
        return buildTable(notes, ticks);
    }

    private Button[][] noteButtons;

    private Table buildTable(int notes, int ticks) {
        Table table = new Table();

        table.setX(Globals.GAME_VIEWPORT_WIDTH / 2);
        table.setY(0);

        table.setWidth(Globals.GAME_VIEWPORT_WIDTH / 2);
        table.setHeight(Globals.GAME_VIEWPORT_HEIGHT);

        noteButtons = new Button[notes][ticks];

        for (int note = 0; note < notes; note++) {
            for (int tick = 0; tick < ticks; tick++) {
                Button button = new Button(skin, "toggle");
                noteButtons[notes - note - 1][tick] = button;
                table.add(button).uniform().fill().expand();
                button.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        Globals.SOUND_SELECT.play(0.5f);
                        return false;
                    }
                });
            }
            table.row().expandX();
        }

        table.row().expandX();
        buildButton(table, "Go!", EventType.GO);

        return table;
    }

    private Button buildButton(Table table, String name, EventType eventType) {
        TextButton button = new TextButton(name, skin);
        button.addListener(new ButtonListener<EventType>(eventType, this.listener));
        table.add(button).colspan(ticks).fillX().height(Globals.GAME_VIEWPORT_HEIGHT / (notes + 1));
        return button;
    }


}
