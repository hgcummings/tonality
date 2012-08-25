package com.xlr3.tonality;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.sun.media.sound.StandardMidiFileReader;
import com.sun.xml.internal.ws.util.StringUtils;
import com.xlr3.tonality.ui.ControlPanel;

import javax.sound.midi.*;
import javax.sound.midi.spi.MidiFileReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.xlr3.tonality.ui.ControlPanel.EventType.*;

public class MainScreen extends AbstractScreen implements ControlPanel.Listener {
    private final ControlPanel controlPanel;
    private final MidiPlayer midiPlayer;

    private final int ticks;
    private final int notes;

    public MainScreen(MidiPlayer midiPlayer) {
        ticks = 5;
        notes = 5;

        controlPanel = new ControlPanel(getSkin(), 5, 5, this);
        stage.addActor(controlPanel.getActor());
        this.midiPlayer = midiPlayer;

        FileHandle midiFileHandle = Gdx.files.internal("audio/test.mid");
        MidiFileReader midiFileReader = new StandardMidiFileReader();
//        try {
//            midiPlayer.play(midiFileReader.getSequence(midiFileHandle.read()));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            Sequence sequence = midiFileReader.getSequence(midiFileHandle.read());

            Track track = sequence.getTracks()[1];

            for (int i = 0; i < track.size(); i++)
            {
                MidiEvent event = track.get(i);

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Status: ");
                stringBuilder.append(event.getMessage().getStatus());
                stringBuilder.append(" ,Tick: ");
                stringBuilder.append(event.getTick());
                stringBuilder.append(", Bytes: [ ");
                for (int j = 0; j < event.getMessage().getLength(); j++)
                {
                    stringBuilder.append(event.getMessage().getMessage()[j]);
                    stringBuilder.append(" ");
                }
                stringBuilder.append("]");

                Gdx.app.log("MIDI", stringBuilder.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testSequence() {
        try {
            Sequence midiSequence = new Sequence(Sequence.PPQ, 1);
            Track track = midiSequence.createTrack();

            ShortMessage programChangeMessage = new ShortMessage();
            programChangeMessage.setMessage(ShortMessage.PROGRAM_CHANGE, 90, 0);
            track.add(new MidiEvent(programChangeMessage , 0));

            for (int tick = 0; tick < ticks; tick++)
            {
                for (int note = 0; note < notes; note++)
                {
                    if (controlPanel.getActive(note, tick))
                    {
                        ShortMessage onMessage = new ShortMessage();
                        onMessage.setMessage(ShortMessage.NOTE_ON, 60 + note, 95);
                        track.add(new MidiEvent(onMessage , tick));

                        ShortMessage offMessage = new ShortMessage();
                        offMessage.setMessage(ShortMessage.NOTE_OFF, 60 + note, 80);
                        track.add(new MidiEvent(offMessage , tick + 1));
                    }
                }
            }

            midiPlayer.play(midiSequence);
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void raise(ControlPanel.EventType eventType) {
        switch (eventType) {
            case TEST:
                testSequence();
                break;
            case DISPATCH:
                break;
            default:
                Gdx.app.log(TonalityGame.LOG, "Unrecognised control panel event type");
                break;
        }
    }
}
