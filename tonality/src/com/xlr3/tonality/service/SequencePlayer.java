package com.xlr3.tonality.service;

import com.xlr3.tonality.platform.MidiPlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class SequencePlayer {
    private final int[] notes;
    private final int ticks;
    private final MidiPlayer midiPlayer;

    public SequencePlayer(MidiPlayer midiPlayer) {
        this.midiPlayer = midiPlayer;
        this.notes = new int[]{60, 62, 65, 67, 69};
        this.ticks = 5;
    }

    public void playSequence(Sequence sequence)
    {
        try {
            javax.sound.midi.Sequence midiSequence
                    = new javax.sound.midi.Sequence(javax.sound.midi.Sequence.PPQ, 1);
            Track track = midiSequence.createTrack();

            ShortMessage programChangeMessage = new ShortMessage();
            programChangeMessage.setMessage(ShortMessage.PROGRAM_CHANGE, 90, 0);
            track.add(new MidiEvent(programChangeMessage , 0));

            for (int tick = 0; tick < ticks; tick++)
            {
                for (int note = 0; note < notes.length; note++)
                {
                    if (sequence.getActive(note, tick))
                    {
                        ShortMessage onMessage = new ShortMessage();
                        onMessage.setMessage(ShortMessage.NOTE_ON, notes[note], 95);
                        track.add(new MidiEvent(onMessage , tick));

                        ShortMessage offMessage = new ShortMessage();
                        offMessage.setMessage(ShortMessage.NOTE_OFF, notes[note], 80);
                        track.add(new MidiEvent(offMessage , tick + 1));
                    }
                }
            }

            midiPlayer.play(midiSequence, 240);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNotes() {
        return notes.length;
    }

    public int getTicks() {
        return ticks;
    }
}
