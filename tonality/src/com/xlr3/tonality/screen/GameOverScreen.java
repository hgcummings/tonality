package com.xlr3.tonality.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Score;

public class GameOverScreen extends AbstractScreen {
    private GenericListener<Boolean> exitListener;

    public GameOverScreen(Score score, GenericListener<Boolean> exitListener) {
        this.exitListener = exitListener;

        Table table = new Table(getSkin());

        table.setWidth(Globals.GAME_VIEWPORT_WIDTH);
        table.setHeight(Globals.GAME_VIEWPORT_HEIGHT);

        table.add(TableUtils.createLabel("GAME OVER!", getSkin())).colspan(2).height(50);
        table.row();

        table.add(TableUtils.createLabel(score.toString(), getSkin())).colspan(2).pad(50);
        table.row();

        buildButton(table, "Play again", true);
        buildButton(table, "Return to menu", false);

        stage.addActor(table);
    }

    private Button buildButton(Table table, String name, boolean payLoad) {
        TextButton button = new TextButton(name, getSkin());
        button.addListener(new ButtonListener<Boolean>(payLoad, this.exitListener));
        table.add(button).width(200).height(100).pad(25);
        return button;
    }
}
