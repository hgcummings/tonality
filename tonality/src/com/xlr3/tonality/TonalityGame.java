package com.xlr3.tonality;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.screen.*;

public class TonalityGame extends Game {
    private MidiPlayer midiPlayer;

    public static final String LOG = TonalityGame.class.getSimpleName();

    public TonalityGame(MidiPlayer midiPlayer) {
        this.midiPlayer = midiPlayer;
    }

    @Override
    public void create() {
        launchIntroScreen();
    }

    private void launchIntroScreen() {
        setScreen(new IntroScreen(new GenericListener<IntroScreen.ButtonType>() {
            @Override
            public void fire(IntroScreen.ButtonType payload) {
                switch (payload) {
                    case TUTORIAL:
                        launchTutorialScreen();
                        break;
                    case GAME:
                        launchMainScreen();
                        break;
                    /*case OPTIONS:
                        launchOptionsScreen();
                        break;*/
                    default:
                        Gdx.app.log(TonalityGame.LOG, "Unrecognised intro screen button type " + payload);
                }
            }
        }));
    }

    private void launchTutorialScreen() {
        setScreen(new TutorialScreen(
                midiPlayer,
                Options.DEFAULT,
                new GenericListener<Boolean>() {
                    @Override
                    public void fire(Boolean payload) {
                        if (payload) {
                            launchMainScreen();
                        } else {
                            launchIntroScreen();
                        }
                    }
                }
        ));
    }

    private void launchMainScreen() {
        setScreen(new MainScreen(
                midiPlayer,
                Options.DEFAULT,
                new GenericListener<Score>() {
                    @Override
                    public void fire(Score payload) {
                        launchGameOverScreen(payload);
                    }
                }));
    }

    private void launchGameOverScreen(Score payload) {
        setScreen(new GameOverScreen(payload, new GenericListener<Boolean>() {
            @Override
            public void fire(Boolean payload) {
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
