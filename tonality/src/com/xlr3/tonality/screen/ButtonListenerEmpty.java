package com.xlr3.tonality.screen;

public class ButtonListenerEmpty extends ButtonListener<Object> {
    public ButtonListenerEmpty(final Runnable runnable) {
        super(null, new GenericListener<Object>() {
            @Override
            public void fire(Object payload) {
                runnable.run();
            }
        });
    }
}
