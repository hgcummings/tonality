package com.xlr3.tonality;

public class Options {
    private static final int[] INTERVALS_PENTATONIC = new int[]{2, 3, 2, 2, 3};

    public final int notes;
    public final int ticks;
    public final int seed;
    public final float mutationRate;
    public final float maxAge;
    public final int startPhase;
    public final Integer rootNote;
    public final int[] intervals;

    public Options(
            int notes,
            int ticks,
            int seed,
            float mutationRate,
            float maxAge,
            int startPhase,
            int[] intervals,
            Integer rootNote) {
        this.notes = notes;
        this.ticks = ticks;
        this.seed = seed;
        this.mutationRate = mutationRate;
        this.maxAge = maxAge;
        this.startPhase = startPhase;
        this.intervals = intervals;
        this.rootNote = rootNote;
    }

    public static final Options DEFAULT = new Options(6, 6, 1, 0.2f, 14.5f, 60, INTERVALS_PENTATONIC, null);

    public Options withSeed(int seed) {
        return new Options(notes, ticks, seed, mutationRate, maxAge, startPhase, intervals, rootNote);
    }
}
