package com.xlr3.tonality;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.screen.*;

public class TonalityGame extends Game {
    private MidiPlayer midiPlayer;

    public static final String LOG = TonalityGame.class.getSimpleName();
    private Options options = Options.DEFAULT;

    public TonalityGame(MidiPlayer midiPlayer) {
        this.midiPlayer = midiPlayer;
    }

    @Override
    public void create() {
        launchIntroScreen();
    }

    private void launchIntroScreen() {
        setScreen(new MenuScreen(new GenericListener<MenuScreen.ButtonType>() {
            @Override
            public void fire(MenuScreen.ButtonType payload) {
                switch (payload) {
                    case TUTORIAL:
                        launchTutorialScreen();
                        break;
                    case GAME:
                        launchMainScreen();
                        break;
                    case OPTIONS:
                        launchOptionsScreen();
                        break;
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
                this.options,
                new GenericListener<Score>() {
                    @Override
                    public void fire(Score payload) {
                        launchGameOverScreen(payload);
                    }
                }));
    }

    private void launchOptionsScreen() {
        setScreen(new OptionsScreen(this.options, new GenericListener<Options>() {
            @Override
            public void fire(Options payload) {
                TonalityGame.this.options = payload;
                launchIntroScreen();
            }
        }));
    }

    private void launchGameOverScreen(Score payload) {
        setScreen(new GameOverScreen(payload, new GenericListener<Boolean>() {
            @Override
            public void fire(Boolean payload) {
                if (payload) {
                    launchMainScreen();
                } else {
                    launchIntroScreen();
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
