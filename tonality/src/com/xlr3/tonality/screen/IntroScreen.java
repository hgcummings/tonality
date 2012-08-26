package com.xlr3.tonality.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.xlr3.tonality.Globals;

public class IntroScreen extends AbstractScreen {
    public enum ButtonType {
        TUTORIAL,
        GAME,
        OPTIONS
    }

    public IntroScreen(GenericListener<ButtonType> listener) {
        Table table = new Table(getSkin());

        table.setWidth(Globals.GAME_VIEWPORT_WIDTH);
        table.setHeight(Globals.GAME_VIEWPORT_HEIGHT);

        table.row().padBottom(40);

        table.add("t o n a l i t y | evolution");

        addButton(table, listener, "Take the two-minute tutorial", ButtonType.TUTORIAL);
        addButton(table, listener, "Start playing now", ButtonType.GAME);
        Button optionsButton = addButton(table, listener, "Tweak the difficulty level", ButtonType.OPTIONS);
        optionsButton.setDisabled(true);

        stage.addActor(table);
    }

    private Button addButton(Table table, GenericListener<ButtonType> listener, String text, ButtonType type) {
        table.row().padBottom(40);
        Button button = new TextButton(text, getSkin());
        button.addListener(new ButtonListener<ButtonType>(type, listener));
        table.add(button).width(240).height(60);
        return button;
    }
}
