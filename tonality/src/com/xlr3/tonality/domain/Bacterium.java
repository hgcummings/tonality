package com.xlr3.tonality.domain;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.xlr3.tonality.Globals;

public class Bacterium extends BacteriumBase {
    private final Vector2 velocity;
    private Sequence sequence;
    public static final String FIRST = "First";

    public Bacterium() {
        this.velocity = new Vector2();
    }

    public void initialise(float x, float y, Sequence sequence, InputListener inputListener) {
        super.initialise(x, y);
        this.velocity.x = Globals.RANDOM.nextFloat() - 0.5f;
        this.velocity.y = Globals.RANDOM.nextFloat() - 0.5f;
        this.sequence = sequence;
        this.addListener(inputListener);
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

    public Sequence getSequence() {
        return sequence;
    }
}
