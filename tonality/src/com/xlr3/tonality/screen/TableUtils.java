package com.xlr3.tonality.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public abstract class TableUtils {
    public static Label createLabel(String s, Skin skin) {
        Label label = new Label(s, skin);
        label.setAlignment(Align.center, Align.center);
        return label;
    }
}
