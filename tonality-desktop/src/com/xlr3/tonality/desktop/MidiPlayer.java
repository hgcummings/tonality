package com.xlr3.tonality.desktop;

import com.badlogic.gdx.Gdx;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.platform.MidiSequence;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/*
 * See @link http://code.google.com/p/libgdx-users/wiki/MidiPlayerInterface
 */
public class MidiPlayer implements com.xlr3.tonality.platform.MidiPlayer {
    private Sequencer sequencer;

    public MidiPlayer()
    {
        try {
            sequencer = MidiSystem.getSequencer();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException("Application requires MIDI", e);
        }
    }

    @Override
    public void play(MidiSequence sequence, int tempo) {
        try {
            Sequence javaxMidiSequence = new Sequence(Sequence.PPQ, 1);
            Track track = javaxMidiSequence.createTrack();

            ShortMessage programChangeMessage = new ShortMessage();
            programChangeMessage.setMessage(ShortMessage.PROGRAM_CHANGE, 90, 0);
            track.add(new MidiEvent(programChangeMessage, 0));

            for (int tick = 0; tick < sequence.getLength(); tick++) {
                for (int note : sequence.getNotes(tick)) {
                    ShortMessage onMessage = new ShortMessage();
                    onMessage.setMessage(ShortMessage.NOTE_ON, note, 95);
                    track.add(new MidiEvent(onMessage, tick));

                    ShortMessage offMessage = new ShortMessage();
                    offMessage.setMessage(ShortMessage.NOTE_OFF, note, 80);
                    track.add(new MidiEvent(offMessage, tick + 1));
                }
            }

            play(javaxMidiSequence, tempo);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void play(Sequence sequence, int tempo) {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }

        try {
            if (!sequencer.isOpen()) {
                sequencer.open();
            }

            sequencer.setSequence(sequence);
            sequencer.setTempoInBPM(tempo);
            sequencer.start();
        } catch (Exception e) {
            Gdx.app.log(TonalityGame.LOG, "Could not play MIDI sequencer");
        }
    }

    @Override
    public void dispose() {
        sequencer.close();
    }
}
