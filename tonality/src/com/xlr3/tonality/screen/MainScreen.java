package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.Score;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.service.SequencePlayer;
import com.xlr3.tonality.domain.Colony;
import com.xlr3.tonality.domain.ControlPanel;
import com.xlr3.tonality.domain.Sequence;

public class MainScreen extends AbstractScreen implements GenericListener<ControlPanel.EventType> {
    private final Colony colony;
    private final ControlPanel controlPanel;
    private final SequencePlayer sequencePlayer;
    private final Label populationLabel;
    private final Label sequencesLabel;
    private final Options options;
    private GenericListener<Score> exitListener;
    private float totalTime = 0f;

    public MainScreen(MidiPlayer midiPlayer, Options options, GenericListener<Score> exitListener) {
        this.options = options;
        this.exitListener = exitListener;
        this.sequencePlayer = new SequencePlayer(midiPlayer, options);

        this.colony = new Colony(options, sequencePlayer);
        this.controlPanel = new ControlPanel(getSkin(), options.notes, options.ticks, this);
        this.populationLabel = new Label(getPopulationText(), getSkin());
        this.sequencesLabel = new Label(getSequencesText(), getSkin());
        sequencesLabel.setX(Constants.GAME_VIEWPORT_WIDTH / 4);

        stage.addActor(colony.getActor());
        stage.addActor(controlPanel.getActor());
        stage.addActor(populationLabel);
        stage.addActor(sequencesLabel);
    }

    public void launchSequence() {
        Sequence sequence = Sequence.createClone(options.notes, options.ticks, controlPanel);
        if (!colony.dispatchSequence(sequence)) {
            sequencePlayer.playSequence(sequence.getNormalised(), Constants.TEMPO);
        }
    }

    @Override
    public void fire(ControlPanel.EventType eventType) {
        switch (eventType) {
            case GO:
                launchSequence();
                break;
            default:
                Gdx.app.log(TonalityGame.LOG, "Unrecognised control panel event type");
                break;
        }
    }

    @Override
    public void render(float delta) {
        totalTime += delta;
        colony.updateState(totalTime);
        if (colony.getPopulation() > 1000 || colony.getPopulation() == 0) {
            exitListener.fire(new Score(totalTime, colony.getBacteriaKilled()));
        }
        populationLabel.setText(getPopulationText());
        sequencesLabel.setText(getSequencesText());
        super.render(delta);
    }

    public String getPopulationText() {
        return String.format("Population: %d", colony.getPopulation());
    }

    public String getSequencesText() {
        return String.format("Active strains: %d", colony.getActiveSequences());
    }
}
