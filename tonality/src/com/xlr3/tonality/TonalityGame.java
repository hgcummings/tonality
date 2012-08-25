package com.xlr3.tonality;

import com.badlogic.gdx.Game;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.screen.MainScreen;
import com.xlr3.tonality.service.SequencePlayer;

public class TonalityGame extends Game {
    private MidiPlayer midiPlayer;

    public static final String LOG = TonalityGame.class.getSimpleName();

    public TonalityGame(MidiPlayer midiPlayer)
    {
        this.midiPlayer = midiPlayer;
    }

	@Override
	public void create() {		
		setScreen(new MainScreen(new SequencePlayer(midiPlayer)));
	}

    @Override
    public void dispose() {
        super.dispose();
        midiPlayer.dispose();
    }
}
