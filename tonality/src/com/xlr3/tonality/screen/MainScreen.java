package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.Score;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.domain.Colony;
import com.xlr3.tonality.domain.ControlPanel;
import com.xlr3.tonality.domain.Sequence;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.service.SequencePlayer;

public class MainScreen extends GameScreen {
    public MainScreen(MidiPlayer midiPlayer, Options options, GenericListener<Score> exitListener) {
        super(midiPlayer, options, exitListener);
    }
}