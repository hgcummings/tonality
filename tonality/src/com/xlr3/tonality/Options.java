package com.xlr3.tonality;

public class Options {
    public final int notes;
    public final int ticks;
    public final int seed;
    public final float mutationRate;
    public final int maxAge;

    public Options(int notes, int ticks, int seed, float mutationRate, int maxAge) {
        this.notes = notes;
        this.ticks = ticks;
        this.seed = seed;
        this.mutationRate = mutationRate;
        this.maxAge = maxAge;
    }
}
