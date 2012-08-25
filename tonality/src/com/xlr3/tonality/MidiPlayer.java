package com.xlr3.tonality;

import javax.sound.midi.Sequence;

public interface MidiPlayer {
    public void play(Sequence sequence);
    public void dispose();
}

