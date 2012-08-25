package com.xlr3.tonality.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import com.xlr3.tonality.Constants;

import java.util.Random;

public class Colony {
    private Pool<Bacterium> pool = new ReflectionPool<Bacterium>(Bacterium.class, 512, 1024);
    private Group group;
    private Random random = new Random();
    private int population;

    public Actor getActor() {
        group = new Group();

        group.setX(0);
        group.setY(0);

        group.setWidth(Constants.GAME_VIEWPORT_WIDTH /2);
        group.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        Bacterium bacterium = pool.obtain();
        bacterium.initialise(Constants.GAME_VIEWPORT_WIDTH / 4, Constants.GAME_VIEWPORT_HEIGHT / 2);
        group.addActor(bacterium);
        population = 1;

        return group;
    }

    public void updateState() {
        for (Actor actor : group.getChildren()) {
            Bacterium bacterium = (Bacterium)actor;

            if (bacterium.getAge() > random.nextFloat() * 1000)
            {
                group.removeActor(bacterium);
                group.addActor(createChild(bacterium));
                group.addActor(createChild(bacterium));

                pool.free(bacterium);
                population++;
            }
        }
    }

    public int getPopulation() {
        return population;
    }

    private Bacterium createChild(Bacterium parent) {
        Bacterium child = pool.obtain();
        child.initialise(parent.getX(), parent.getY());
        return child;
    }
}