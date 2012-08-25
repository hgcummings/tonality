package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.xlr3.tonality.service.SequencePlayer;
import com.xlr3.tonality.TonalityGame;
import com.xlr3.tonality.ui.Colony;
import com.xlr3.tonality.ui.ControlPanel;

public class MainScreen extends AbstractScreen implements ControlPanel.Listener {
    private final Colony colony;
    private final ControlPanel controlPanel;
    private final SequencePlayer sequencePlayer;

    public MainScreen(SequencePlayer sequencePlayer) {
        colony = new Colony();
        controlPanel = new ControlPanel(getSkin(), sequencePlayer.getNotes(), sequencePlayer.getTicks(), this);
        stage.addActor(colony.getActor());
        stage.addActor(controlPanel.getActor());
        this.sequencePlayer = sequencePlayer;
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
}
