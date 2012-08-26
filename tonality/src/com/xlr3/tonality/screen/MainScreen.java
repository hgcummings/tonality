package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.xlr3.tonality.Constants;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.service.SequencePlayer;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.ui.Colony;
import com.xlr3.tonality.ui.ControlPanel;
import com.xlr3.tonality.ui.Sequence;

public class MainScreen extends AbstractScreen implements ControlPanel.Listener {
    private final Colony colony;
    private final ControlPanel controlPanel;
    private final SequencePlayer sequencePlayer;
    private final Label populationLabel;
    private final Label sequencesLabel;
    private final Options options;

    public MainScreen(MidiPlayer midiPlayer, Options options) {
        this.options = options;
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

    public void testSequence() {
        sequencePlayer.playSequence(controlPanel);
    }

    public void dispatchSequence() {
        Sequence sequence = Sequence.createClone(options.notes, options.ticks, controlPanel);
        colony.dispatchSequence(sequence);
    }

    @Override
    public void raise(ControlPanel.EventType eventType) {
        switch (eventType) {
            case TEST:
                testSequence();
                break;
            case DISPATCH:
                dispatchSequence();
                break;
            default:
                Gdx.app.log(TonalityGame.LOG, "Unrecognised control panel event type");
                break;
        }
    }

    @Override
    public void render(float delta) {
        colony.updateState();
        populationLabel.setText(getPopulationText());
        sequencesLabel.setText(getSequencesText());
        if (colony.getActiveSequences() > 1) {
            controlPanel.activate();
        }
        super.render(delta);
    }

    public String getPopulationText() {
        return String.format("Population: %d", colony.getPopulation());
    }

    public String getSequencesText() {
        return String.format("Active strains: %d", colony.getActiveSequences());
    }
}
