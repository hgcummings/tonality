package com.xlr3.tonality.platform;

/*
 * Platform-specific player for MIDI sequences
 * Should ideally call through to a synthesizer directly, but latency isn't that important
 * (writing the whole sequence out to a file then playing it back is OK)
 */
public interface MidiPlayer {
    public void play(MidiSequence sequence, int tempo);

    public void dispose();
}

