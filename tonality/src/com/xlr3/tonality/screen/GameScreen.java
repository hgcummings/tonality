package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.domain.Colony;
import com.xlr3.tonality.domain.ControlPanel;
import com.xlr3.tonality.domain.Sequence;
import com.xlr3.tonality.platform.MidiPlayer;
import com.xlr3.tonality.service.SequencePlayer;

public abstract class GameScreen<T> extends AbstractScreen implements GenericListener<ControlPanel.EventType> {
    protected final Colony colony;
    protected final ControlPanel controlPanel;
    private final SequencePlayer sequencePlayer;
    protected final Label populationLabel;
    protected final Label sequencesLabel;
    protected final Options options;
    protected GenericListener<T> exitListener;
    protected float totalTime = 0f;

    public GameScreen(MidiPlayer midiPlayer, Options options, GenericListener<T> exitListener) {
        this.options = options;
        this.exitListener = exitListener;
        this.sequencePlayer = new SequencePlayer(midiPlayer, options);

        this.colony = new Colony(options, sequencePlayer);
        this.controlPanel = new ControlPanel(getSkin(), options.notes, options.ticks, this);
        this.populationLabel = new Label(getPopulationText(), getSkin());
        this.sequencesLabel = new Label(getSequencesText(), getSkin());
        sequencesLabel.setX(Globals.GAME_VIEWPORT_WIDTH / 4);

        stage.addActor(controlPanel.getActor());
    }

    protected void launchSequence() {
        Sequence sequence = Sequence.createClone(options.notes, options.ticks, controlPanel);
        int hits;
        if (colony.initialised() && ((hits = colony.dispatchSequence(sequence)) != 0)) {
            Globals.SOUND_HIT.play(hits / 100f);
        } else {
            sequencePlayer.playSequence(sequence.getNormalised(), Globals.TEMPO);
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
        if (colony.initialised()) {
            colony.updateState(totalTime);
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
