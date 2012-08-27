package com.xlr3.tonality.domain;

import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Options;

public class SequenceImpl implements Sequence {
    private final boolean[][] notes;
    private final int ticks;

    public SequenceImpl(int notes, int ticks) {
        this.notes = new boolean[notes][ticks];
        this.ticks = ticks;
    }

    @Override
    public boolean getActive(int note, int tick) {
        return notes[note][tick];
    }

    public boolean matches(SequenceImpl other) {
        if (this.getLength() != other.getLength()) {
            return false;
        }

        for (int i = 0; i < this.getLength(); i++) {
            for (int note = 0; note < notes.length; note++) {
                if ((this.getActive(note, i + this.getStart())) != (other.getActive(note, i + other.getStart()))) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getStart() {
        for (int tick = 0; tick < ticks; tick++) {
            if (tickHasNote(tick)) {
                return tick;
            }
        }
        return -1;
    }

    private int getEnd() {
        for (int tick = ticks - 1; tick >= 0; tick--) {
            if (tickHasNote(tick)) {
                return tick;
            }
        }
        return -1;
    }

    @Override
    public int getLength() {
        int start = getStart();
        return start == -1 ? 0 : getEnd() - getStart() + 1;
    }

    private boolean tickHasNote(int tick) {
        for (boolean[] note : notes) {
            if (note[tick]) {
                return true;
            }
        }
        return false;
    }

    private int tickNoteCount(int tick) {
        int count = 0;
        for (boolean[] note : notes) {
            if (note[tick]) {
                count++;
            }
        }
        return count;
    }

    public static SequenceImpl create(Options options) {
        SequenceImpl newSequence = new SequenceImpl(options.notes, options.ticks);
        for (int i = 0; i < options.seed; i++) {
            newSequence.addNote();
        }
        return newSequence;
    }

    public SequenceImpl createMutation() {
        SequenceImpl newSequence = this.createClone();
        // Half the mutations 'move' a note, others add a new one
        if (Globals.RANDOM.nextFloat() > 0.5f) {
            newSequence.removeNote();
        }
        newSequence.addNote();
        return newSequence;
    }

    private void removeNote() {
        int note = Globals.RANDOM.nextInt(notes.length);
        int tick = Globals.RANDOM.nextInt(ticks);

        while (!notes[note][tick]) {
            note = Globals.RANDOM.nextInt(notes.length);
            tick = Globals.RANDOM.nextInt(ticks);
        }

        notes[note][tick] = false;
    }

    public SequenceImpl createClone() {
        return createClone(this.notes.length, this.ticks, this);
    }

    public Sequence getNormalised() {
        return new Sequence() {
            @Override
            public boolean getActive(int note, int tick) {
                return notes[note][tick + getStart()];
            }

            @Override
            public int getLength() {
                return SequenceImpl.this.getLength();
            }
        };
    }

    public static SequenceImpl createClone(int notes, int ticks, com.xlr3.tonality.domain.Sequence parent) {
        SequenceImpl clone = new SequenceImpl(notes, ticks);
        for (int note = 0; note < notes; note++) {
            for (int tick = 0; tick < ticks; tick++) {
                clone.notes[note][tick] = parent.getActive(note, tick);
            }
        }
        return clone;
    }

    private void addNote() {
        float max = 0f;
        int chosenTick = -1;

        // Favour melodic complexity before harmonic
        for (int tick = 0; tick < ticks; tick++) {
            float weighting = Globals.RANDOM.nextFloat() * (ticks - tickNoteCount(tick));
            if (weighting > max) {
                max = weighting;
                chosenTick = tick;
            }
        }

        int chosenNote = Globals.RANDOM.nextInt(notes.length);

        while (getActive(chosenNote, chosenTick)) {
            chosenNote = Globals.RANDOM.nextInt(notes.length);
        }

        notes[chosenNote][chosenTick] = true;
    }
}
