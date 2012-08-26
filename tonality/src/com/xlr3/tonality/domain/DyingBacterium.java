package com.xlr3.tonality.domain;

import com.badlogic.gdx.utils.Pool;
import com.xlr3.tonality.Globals;

public class DyingBacterium extends BacteriumBase {
    private static final Float MAX_AGE = 0.5f;
    private Pool<DyingBacterium> returnPool;

    public void initialise(float x, float y, Pool<DyingBacterium> returnPool) {
        super.initialise(x, y);
        this.returnPool = returnPool;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.getAge() > MAX_AGE) {
            this.remove();
            this.returnPool.free(this);
            return;
        }

        this.size(-SIZE * (delta / MAX_AGE));
        this.invalidate();

        tryUpdateX(delta * (Globals.RANDOM.nextFloat() - 0.5f) * 100);
        tryUpdateY(delta * (Globals.RANDOM.nextFloat() - 0.5f) * 100);
    }
}
