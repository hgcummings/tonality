package com.xlr3.tonality.screen;

import com.xlr3.tonality.Options;
import com.xlr3.tonality.Score;
import com.xlr3.tonality.platform.MidiPlayer;

public class MainScreen extends GameScreen<Score> {
    public MainScreen(MidiPlayer midiPlayer, Options options, GenericListener<Score> exitListener) {
        super(midiPlayer, options, exitListener);
        stage.addActor(colony.getActor());
        stage.addActor(populationLabel);
        stage.addActor(sequencesLabel);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (colony.getPopulation() > 1000 || colony.getPopulation() == 0) {
            exitListener.fire(new Score(totalTime, colony.getBacteriaKilled()));
        }
    }
}