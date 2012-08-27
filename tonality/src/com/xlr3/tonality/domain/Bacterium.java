package com.xlr3.tonality.domain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.xlr3.tonality.Globals;

public class Bacterium extends BacteriumBase {
    private final Vector2 velocity;
    private SequenceImpl sequence;
    public static final String FIRST = "First";
    private float splitAge;

    public Bacterium() {
        this.velocity = new Vector2();
    }

    public void initialise(float x, float y, SequenceImpl sequence, float maxAge, InputListener inputListener) {
        super.initialise(x, y);
        this.velocity.x = Globals.RANDOM.nextFloat() - 0.5f;
        this.velocity.y = Globals.RANDOM.nextFloat() - 0.5f;
        this.sequence = sequence;
        this.addListener(inputListener);
        this.splitAge = Globals.RANDOM.nextFloat() * maxAge;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float dx = delta * velocity.x * 100;
        float dy = delta * velocity.y * 100;

        if (!tryUpdateX(dx)) {
            velocity.x = -velocity.x;
        }

        if (!tryUpdateY(dy)) {
            velocity.y = -velocity.y;
        }
    }

    public SequenceImpl getSequence() {
        return sequence;
    }

    public float getSplitAge() {
        return splitAge;
    }
}
