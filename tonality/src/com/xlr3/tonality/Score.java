package com.xlr3.tonality;

public class Score {
    public final float totalTime;
    public final int bacteriaKilled;

    public Score(float totalTime, int bacteriaKilled) {
        this.totalTime = totalTime;
        this.bacteriaKilled = bacteriaKilled;
    }

    @Override
    public String toString() {
        return String.format("You survived for %.0f seconds and killed %d bacteria", totalTime, bacteriaKilled);
    }
}
