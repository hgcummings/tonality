package com.xlr3.tonality.domain;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;
import com.badlogic.gdx.utils.SnapshotArray;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Options;

import java.util.ArrayList;

public class Colony {
    private Pool<Bacterium> livePool = new ReflectionPool<Bacterium>(Bacterium.class, 128, 1024);
    private Pool<DyingBacterium> deadPool = new ReflectionPool<DyingBacterium>(DyingBacterium.class, 32, 256);
    private Group liveGroup;
    private Group deadGroup;
    private int population;
    private int activeSequences;
    private final Options options;
    private final InputListener inputListener;
    private int bacteriaKilled;
    private boolean inhibitSplitting;

    public Colony(Options options, final SequencePlayer sequencePlayer) {
        this.options = options;

        this.inputListener = new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Bacterium bacterium = (Bacterium) event.getListenerActor();
                sequencePlayer.playSequence(bacterium.getSequence().getNormalised(), Globals.TEMPO);
                return true;
            }
        };

        this.activeSequences = 1;
        this.bacteriaKilled = 0;
    }

    public Group getActor() {
        deadGroup = createGroup();
        liveGroup = createGroup();

        Group combinedGroup = createGroup();

        Bacterium bacterium = livePool.obtain();
        bacterium.initialise(
                Globals.GAME_VIEWPORT_WIDTH / 4,
                Globals.GAME_VIEWPORT_HEIGHT / 2,
                SequenceImpl.create(options),
                options.maxAge,
                inputListener);
        bacterium.setName(Bacterium.FIRST);
        liveGroup.addActor(bacterium);
        population = 1;

        combinedGroup.addActor(deadGroup);
        combinedGroup.addActor(liveGroup);

        return combinedGroup;
    }

    private Group createGroup() {
        Group group = new Group();
        group.setWidth(Globals.GAME_VIEWPORT_WIDTH / 2);
        group.setHeight(Globals.GAME_VIEWPORT_HEIGHT);
        return group;
    }

    public void updateState(float totalTime) {
        if (inhibitSplitting) {
            return;
        }

        for (Actor actor : liveGroup.getChildren()) {
            Bacterium bacterium = (Bacterium) actor;

            if ((activeSequences == 1 && (population == 1 || totalTime < options.startPhase))
                    || bacterium.getAge() > bacterium.getSplitAge()) {
                liveGroup.removeActor(bacterium);
                liveGroup.addActor(createChild(bacterium, false));

                if (activeSequences == 1 || options.mutationRate > Globals.RANDOM.nextFloat()) {
                    liveGroup.addActor(createChild(bacterium, true));
                    activeSequences++;
                } else {
                    liveGroup.addActor(createChild(bacterium, false));
                }

                livePool.free(bacterium);
                population++;
            }
        }
    }

    public int getPopulation() {
        return population;
    }

    public int dispatchSequence(SequenceImpl sequence) {
        int hits = 0;

        ArrayList<SequenceImpl> matchedSequences = new ArrayList<SequenceImpl>();
        ArrayList<SequenceImpl> unmatchedSequences = new ArrayList<SequenceImpl>(activeSequences);

        SnapshotArray<Actor> children = liveGroup.getChildren();
        Actor[] actors = children.begin();
        for (int i = 0, n = children.size; i < n; i++) {
            Bacterium bacterium = (Bacterium) actors[i];

            boolean match;

            if (unmatchedSequences.contains(bacterium.getSequence())) {
                match = false;
            } else if (matchedSequences.contains(bacterium.getSequence())) {
                match = true;
            } else if (bacterium.getSequence().matches(sequence)) {
                match = true;
                matchedSequences.add(bacterium.getSequence());
            } else {
                match = false;
                unmatchedSequences.add(bacterium.getSequence());
            }

            if (match) {
                liveGroup.removeActor(bacterium);

                DyingBacterium dyingBacterium = deadPool.obtain();
                dyingBacterium.initialise(bacterium.getX(), bacterium.getY(), deadPool);
                deadGroup.addActor(dyingBacterium);

                livePool.free(bacterium);
                hits++;
            }
        }
        children.end();

        population -= hits;
        bacteriaKilled += hits;
        activeSequences -= matchedSequences.size();
        return hits;
    }

    public boolean initialised() {
        return liveGroup != null;
    }

    public void setInhibitSplitting(boolean value) {
        this.inhibitSplitting = value;
    }

    private Bacterium createChild(Bacterium parent, boolean mutate) {
        Bacterium child = livePool.obtain();

        child.initialise(
                parent.getX(),
                parent.getY(),
                mutate ? parent.getSequence().createMutation() : parent.getSequence(),
                options.maxAge,
                inputListener);

        return child;
    }

    public int getActiveSequences() {
        return activeSequences;
    }

    public int getBacteriaKilled() {
        return bacteriaKilled;
    }
}