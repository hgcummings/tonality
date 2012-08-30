package com.xlr3.tonality.domain;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.platform.MidiSequenceImpl;

/*
 * Plays a Tonality Sequence as a MIDI Sequence
 * Note: This class is probably desktop-specific (since it relies on Sun Java runtime libraries)
 */
public class SequencePlayer {
    private static final Pool<MidiSequenceImpl> sequencePool = new ReflectionPool<MidiSequenceImpl>(MidiSequenceImpl.class);
    private final int[] notes;
    private final int ticks;
    private final MidiPlayer midiPlayer;

    public SequencePlayer(MidiPlayer midiPlayer, Options options) {
    	this.midiPlayer = midiPlayer;
    	this.notes = new int[options.notes];
    	this.ticks = options.ticks;
    	
        int note;

        if (options.rootNote == null) {
            note = Globals.RANDOM.nextInt(13) + 48;
        } else {
            note = options.rootNote;
        }

        for (int i = 0; i < options.notes; i++) {
            notes[i] = note;
            note += options.intervals[i % options.intervals.length];
        }
    }
    
    public void playSequence(Sequence sequence, int tempo) {
    	MidiSequenceImpl midiSequence = sequencePool.obtain();
    	midiSequence.initialise(notes, ticks, sequence);
    	midiPlayer.play(midiSequence, tempo);
    	sequencePool.free(midiSequence);
    }
}
