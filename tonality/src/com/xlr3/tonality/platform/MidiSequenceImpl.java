package com.xlr3.tonality.platform;

import com.badlogic.gdx.utils.Array;
import com.xlr3.tonality.domain.Sequence;

public class MidiSequenceImpl implements MidiSequence {
    private Array<Array<Integer>> midiNotes;
    private int length;
    
    public void initialise(int[] notes, int ticks, Sequence sequence) {
        if (midiNotes == null) {
            midiNotes = new Array<Array<Integer>>();
        }
        if (midiNotes.size != ticks) {
             midiNotes.clear();
             for (int tick = 0; tick < ticks; tick++) {
                 midiNotes.add(new Array<Integer>());
             }
        }
        
        this.length = sequence.getLength();
        
        for (int tick = 0; tick < sequence.getLength(); tick++) {
            Array<Integer> midiNotesForTick = this.midiNotes.get(tick);
            midiNotesForTick.clear();
            
            for (int note = 0; note < notes.length; note++) {
                if (sequence.getActive(note, tick)) {
                    midiNotesForTick.add(notes[note]);
                }
            }
        }
    }

    @Override
    public Array<Integer> getNotes(int tick) {
        return midiNotes.get(tick);
    }

    @Override
    public int getLength() {
        return length;
    }
}
