package com.xlr3.tonality.screen;

public interface GenericListener<T> {
    public void fire(T payload);
}