package com.xlr3.tonality.service;

import com.xlr3.tonality.Options;
import com.xlr3.tonality.platform.MidiPlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/*
 * Plays a Tonality Sequence as a MIDI Sequence
 * Note: This class is probably desktop-specific (since it relies on Sun Java runtime libraries)
 */
public class SequencePlayer {
    private final int[] notes;
    private final MidiPlayer midiPlayer;

    private static final int[] INTERVALS_PENTATONIC = new int[] { 2, 3, 2, 2, 3 };

    public SequencePlayer(MidiPlayer midiPlayer, Options options) {
        this.midiPlayer = midiPlayer;
        this.notes = new int[options.notes];

        int note = 60;
        for (int i = 0; i<options.notes; i++) {
            notes[i] = note;
            note += INTERVALS_PENTATONIC[i % INTERVALS_PENTATONIC.length];
        }
    }

    public void playSequence(Sequence sequence, int tempo)
    {
        try {
            javax.sound.midi.Sequence midiSequence
                    = new javax.sound.midi.Sequence(javax.sound.midi.Sequence.PPQ, 1);
            Track track = midiSequence.createTrack();

            ShortMessage programChangeMessage = new ShortMessage();
            programChangeMessage.setMessage(ShortMessage.PROGRAM_CHANGE, 90, 0);
            track.add(new MidiEvent(programChangeMessage , 0));

            for (int tick = 0; tick < sequence.getLength(); tick++)
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

            midiPlayer.play(midiSequence, tempo);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }
}
