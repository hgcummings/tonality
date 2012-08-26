package com.xlr3.tonality;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Constants {
    public static final int GAME_VIEWPORT_WIDTH = 800;
    public static final int GAME_VIEWPORT_HEIGHT = 480;
    public static final int TEMPO = 240;
    public static final Sound SELECT = Gdx.audio.newSound(Gdx.files.internal("audio/select.wav"));
    public static final Sound HIT = Gdx.audio.newSound(Gdx.files.internal("audio/hit.wav"));
}
