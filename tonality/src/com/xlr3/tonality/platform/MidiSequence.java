package com.xlr3.tonality.platform;

import com.badlogic.gdx.utils.Array;

/*
 * Non-platform-dependent MIDI sequence (javax.sound classes rely on the JRE, which
 * can't be used with Android or GWT)
 */
public interface MidiSequence {
    public Array<Integer> getNotes(int tick);

    public int getLength();
}