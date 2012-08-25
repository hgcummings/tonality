package com.xlr3.tonality.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.TonalityGame;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Tonality v0.0.1";
		cfg.useGL20 = true;
		cfg.width = Constants.GAME_VIEWPORT_WIDTH;
		cfg.height = Constants.GAME_VIEWPORT_HEIGHT;

        new LwjglApplication(new TonalityGame(new MidiPlayer()), cfg);
	}
}
