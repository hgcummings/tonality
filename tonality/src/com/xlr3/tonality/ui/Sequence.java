package com.xlr3.tonality.ui;

import com.xlr3.tonality.Options;

import java.util.Random;

public class Sequence implements com.xlr3.tonality.service.Sequence {
    private static Random random = new Random();

    private final boolean[][] notes;
    private final int ticks;

    public Sequence(int notes, int ticks) {
        this.notes = new boolean[notes][ticks];
        this.ticks = ticks;
    }

    @Override
    public boolean getActive(int note, int tick) {
        return notes[note][tick];
    }

    public boolean matches(Sequence other) {
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

    public static Sequence create(Options options) {
        Sequence newSequence = new Sequence(options.notes, options.ticks);
        for (int i = 0; i < options.seed; i++) {
            newSequence.addNote();
        }
        return newSequence;
    }

    public Sequence createMutation() {
        Sequence newSequence = this.createClone();
        // Half the mutations 'move' a note, others add a new one
        if (random.nextFloat() > 0.5f) {
            newSequence.removeNote();
        }
        newSequence.addNote();
        return newSequence;
    }

    private void removeNote() {
        int note = random.nextInt(notes.length);
        int tick = random.nextInt(ticks);

        while (!notes[note][tick]) {
            note = random.nextInt(notes.length);
            tick = random.nextInt(ticks);
        }

        notes[note][tick] = false;
    }

    public Sequence createClone() {
        return createClone(this.notes.length, this.ticks, this);
    }

    public com.xlr3.tonality.service.Sequence getNormalised() {
        return new com.xlr3.tonality.service.Sequence() {
            @Override
            public boolean getActive(int note, int tick) {
                return notes[note][tick + getStart()];
            }

            @Override
            public int getLength() {
                return Sequence.this.getLength();
            }
        };
    }

    public static Sequence createClone(int notes, int ticks, com.xlr3.tonality.service.Sequence parent) {
        Sequence clone = new Sequence(notes, ticks);
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
            float weighting = random.nextFloat() * (ticks - tickNoteCount(tick));
            if (weighting > max) {
                max = weighting;
                chosenTick = tick;
            }
        }

        int chosenNote = random.nextInt(notes.length);

        while (getActive(chosenNote, chosenTick)) {
            chosenNote = random.nextInt(notes.length);
        }

        notes[chosenNote][chosenTick] = true;
    }
}
