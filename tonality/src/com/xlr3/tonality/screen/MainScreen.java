package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.service.SequencePlayer;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.ui.Colony;
import com.xlr3.tonality.ui.ControlPanel;

public class MainScreen extends AbstractScreen implements ControlPanel.Listener {
    private final Colony colony;
    private final ControlPanel controlPanel;
    private final SequencePlayer sequencePlayer;
    private final Label populationLabel;

    public MainScreen(MidiPlayer midiPlayer, Options options) {
        this.sequencePlayer = new SequencePlayer(midiPlayer, options);
        this.colony = new Colony(options, sequencePlayer);
        this.controlPanel = new ControlPanel(getSkin(), options.notes, options.ticks, this);
        this.populationLabel = new Label(getPopulationText(), getSkin());
        stage.addActor(colony.getActor());
        stage.addActor(controlPanel.getActor());
        stage.addActor(populationLabel);
    }

    public void testSequence() {
        sequencePlayer.playSequence(controlPanel);
    }

    @Override
    public void raise(ControlPanel.EventType eventType) {
        switch (eventType) {
            case TEST:
                testSequence();
                break;
            case DISPATCH:
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
        super.render(delta);
    }

    public String getPopulationText() {
        return "Population: " + colony.getPopulation();
    }
}
