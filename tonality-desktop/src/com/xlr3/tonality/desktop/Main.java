package com.xlr3.tonality.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.TonalityGame;

import java.net.URL;
import java.util.jar.Manifest;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

        cfg.title = "Tonality";

        try {
            String className = Main.class.getSimpleName() + ".class";
            String classPath = Main.class.getResource(className).toString();
            if (classPath.startsWith("jar")) {
                String manifestPath = classPath.substring(0, classPath.lastIndexOf("!") + 1) +
                        "/META-INF/MANIFEST.MF";
                Manifest manifest = new Manifest(new URL(manifestPath).openStream());
                cfg.title += " v" + manifest.getMainAttributes().getValue("Manifest-Version");
            } else {
                cfg.title += " (dev)";
            }
        }
        catch (Exception e) {
            Gdx.app.log(TonalityGame.LOG, "Couldn't retrieve manifest version");
        }

		cfg.useGL20 = true;
		cfg.width = Constants.GAME_VIEWPORT_WIDTH;
		cfg.height = Constants.GAME_VIEWPORT_HEIGHT;

        new LwjglApplication(new TonalityGame(new MidiPlayer()), cfg);
	}
}
