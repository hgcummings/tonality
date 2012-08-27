package com.xlr3.tonality.domain;

public interface Sequence {
    public boolean getActive(int note, int tick);

    public int getLength();
}
