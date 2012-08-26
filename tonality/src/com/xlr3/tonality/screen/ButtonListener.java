package com.xlr3.tonality.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.xlr3.tonality.Constants;

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
        Constants.SELECT.play(0.5f);
        return !((Button) event.getListenerActor()).isDisabled();
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        listener.fire(payload);
    }
}