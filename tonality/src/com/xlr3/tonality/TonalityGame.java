package com.xlr3.tonality;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.screen.GameOverScreen;
import com.xlr3.tonality.screen.GenericListener;
import com.xlr3.tonality.screen.IntroScreen;
import com.xlr3.tonality.screen.MainScreen;

public class TonalityGame extends Game {
    private MidiPlayer midiPlayer;

    public static final String LOG = TonalityGame.class.getSimpleName();

    public TonalityGame(MidiPlayer midiPlayer)
    {
        this.midiPlayer = midiPlayer;
    }

	@Override
	public void create() {
		setScreen(new IntroScreen(new IntroScreen.ExitListener() {
            @Override public void exit() { launchMainScreen(); }
        }));
	}

    private void launchMainScreen() {
        setScreen(new MainScreen(
                midiPlayer,
                new Options(6, 6, 1, 0.2f, 2000),
                new GenericListener<Score>() {
                    @Override public void fire(Score payload) { launchGameOverScreen(payload); }
                }));
    }

    private void launchGameOverScreen(Score payload) {
        setScreen(new GameOverScreen(payload, new GenericListener<Boolean>() {
            @Override public void fire(Boolean payload) {
                if (payload) {
                    Gdx.app.exit();
                } else {
                    launchMainScreen();
                }
            }
        }));
    }

    @Override
    public void dispose() {
        super.dispose();
        midiPlayer.dispose();
    }
}
