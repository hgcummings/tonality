package com.xlr3.tonality.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.xlr3.tonality.Constants;

public class Colony {

    public Actor getActor() {
        Group group = new Group();

        group.setX(0);
        group.setY(0);

        group.setWidth(Constants.GAME_VIEWPORT_WIDTH /2);
        group.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        for (int i = 0; i < 64; i++)
        {
            Bacterium bacterium = new Bacterium();
            group.addActor(bacterium);
        }

        return group;
    }
}
