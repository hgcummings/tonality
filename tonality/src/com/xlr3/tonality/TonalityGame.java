package com.xlr3.tonality;

import com.badlogic.gdx.Game;

public class TonalityGame extends Game {
    private MidiPlayer midiPlayer;

    public static final String LOG = TonalityGame.class.getSimpleName();

    public TonalityGame(MidiPlayer midiPlayer)
    {
        this.midiPlayer = midiPlayer;
    }

	@Override
	public void create() {		
		setScreen(new MainScreen(midiPlayer));
	}

    @Override
    public void dispose() {
        super.dispose();
        midiPlayer.dispose();
    }
}
