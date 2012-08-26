package com.xlr3.tonality.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.xlr3.tonality.Globals;

public class ButtonListener<T> extends InputListener {
    private T payload;
    private GenericListener<T> listener;

    public ButtonListener(T payload, GenericListener<T> listener) {
        this.payload = payload;
        this.listener = listener;
    }

    @Override
    public boolean touchDown(
            InputEvent event,
            float x,
            float y,
            int pointer,
            int button) {
        if (((Button) event.getListenerActor()).isDisabled()) {
            return false;
        } else {
            Globals.SOUND_SELECT.play(0.5f);
            return true;
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        listener.fire(payload);
    }
}