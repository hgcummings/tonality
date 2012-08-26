package com.xlr3.tonality;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;

public class Globals {
    public static final int GAME_VIEWPORT_WIDTH = 800;
    public static final int GAME_VIEWPORT_HEIGHT = 480;
    public static final int TEMPO = 240;
    public static final Sound SOUND_SELECT = Gdx.audio.newSound(Gdx.files.internal("audio/select.wav"));
    public static final Sound SOUND_HIT = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));
    public static final Random RANDOM = new Random();
}
