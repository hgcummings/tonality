package com.xlr3.tonality.desktop;

import com.badlogic.gdx.Gdx;
import com.xlr3.tonality.TonalityGame;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/*
 * See @link http://code.google.com/p/libgdx-users/wiki/MidiPlayerInterface
 */
public class MidiPlayer implements com.xlr3.tonality.platform.MidiPlayer {
    private Sequence sequence;
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
    public void play(Sequence sequence) {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }

        try {
            if (!sequencer.isOpen()) {
                sequencer.open();
            }

            sequencer.setSequence(sequence);
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
