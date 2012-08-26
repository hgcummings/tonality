package com.xlr3.tonality.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.service.SequencePlayer;

import java.util.ArrayList;
import java.util.Random;

public class Colony {
    private Pool<Bacterium> pool = new ReflectionPool<Bacterium>(Bacterium.class, 512, 1024);
    private Group group;
    private Random random = new Random();
    private int population;
    private int activeSequences;
    private final Options options;
    private final InputListener inputListener;

    public Colony(Options options, final SequencePlayer sequencePlayer) {
        this.options = options;

        this.inputListener = new InputListener() {
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                Bacterium bacterium = (Bacterium) event.getListenerActor();
                sequencePlayer.playSequence(bacterium.getSequence());
                return true;
            }
        };

        this.activeSequences = 1;
    }

    public Actor getActor() {
        group = new Group();

        group.setX(0);
        group.setY(0);

        group.setWidth(Constants.GAME_VIEWPORT_WIDTH /2);
        group.setHeight(Constants.GAME_VIEWPORT_HEIGHT);

        Bacterium bacterium = pool.obtain();
        bacterium.initialise(
                Constants.GAME_VIEWPORT_WIDTH / 4,
                Constants.GAME_VIEWPORT_HEIGHT / 2,
                Sequence.create(options),
                inputListener);
        group.addActor(bacterium);
        population = 1;

        return group;
    }

    public void updateState() {
        for (Actor actor : group.getChildren()) {
            Bacterium bacterium = (Bacterium)actor;

            if (bacterium.getAge() > random.nextFloat() * options.maxAge)
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

    public void dispatchSequence(Sequence sequence) {
        ArrayList<Sequence> matchedSequences = new ArrayList<Sequence>();

        SnapshotArray<Actor> children = group.getChildren();
        Actor[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            Bacterium bacterium = (Bacterium)actors[i];

            boolean match = false;

            if (matchedSequences.contains(bacterium.getSequence())) {
                match = true;
            } else if (bacterium.getSequence().matches(sequence)) {
                match = true;
                matchedSequences.add(bacterium.getSequence());
            }

            if (match) {
                group.removeActor(bacterium);
                pool.free(bacterium);
                population--;
            }
        }
        children.end();

        activeSequences -= matchedSequences.size();
    }

    private Bacterium createChild(Bacterium parent) {
        Bacterium child = pool.obtain();
        Sequence newSequence;
        if (options.mutationRate > random.nextFloat()) {
            newSequence = parent.getSequence().createMutation();
            activeSequences++;
        } else {
            newSequence = parent.getSequence();
        }

        child.initialise(parent.getX(), parent.getY(), newSequence, inputListener);
        return child;
    }

    public int getActiveSequences() {
        return activeSequences;
    }
}