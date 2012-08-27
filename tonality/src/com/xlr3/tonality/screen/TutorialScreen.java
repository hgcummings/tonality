package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.OrderedMap;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.domain.Bacterium;
import com.xlr3.tonality.platform.MidiPlayer;

public class TutorialScreen extends GameScreen<Boolean> {
    private Label tutorialLabel;
    private String currentText;
    private Table tutorialTable;

    @SuppressWarnings("unchecked")
    private static OrderedMap<String, String> tutorialText
            = (OrderedMap<String, String>) new JsonReader().parse(Gdx.files.internal("text/tutorial.json"));

    public TutorialScreen(MidiPlayer midiPlayer, Options options, GenericListener<Boolean> exitListener) {
        super(midiPlayer, options.withSeed(3), exitListener);
        colony.setInhibitSplitting(true);

        for (int i = 0; (i < options.notes && i < options.ticks); i++) {
            controlPanel.setActive(i, i, true);
        }

        currentText = "welcome";
        tutorialLabel = new Label(getTutorialText(), getSkin());
        tutorialTable = createTutorialTable(exitListener);
    }

    @Override
    public void launchSequence() {
        int populationBefore = colony.getPopulation();
        super.launchSequence();
        if (currentText.equals("welcome")) {
            currentText = "grid";
        } else if (currentText.equals("grid")) {
            currentText = "grid-next";
            final Button nextButton = showButton("Next");
            nextButton.addListener(new ButtonListenerEmpty(new Runnable() {
                @Override
                public void run() {
                    nextButton.remove();
                    startBacteriumTutorial();
                }
            }));
        } else if (currentText.equals("sequence") || currentText.equals("miss")) {
            if (colony.getPopulation() == populationBefore) {
                currentText = "miss";
            } else {
                currentText = "hit";
                final Button startButton = showButton("Start playing now");
                startButton.addListener(new ButtonListener<Boolean>(true, exitListener));
            }
        }
    }

    private Button showButton(String text) {
        tutorialTable.row();
        final TextButton button = new TextButton(text, getSkin());
        tutorialTable.add(button).colspan(2).fillX().expandX();
        return button;
    }

    private void startBacteriumTutorial() {
        for (int note = 0; note < options.notes; note++) {
            for (int tick = 0; tick < options.ticks; tick++) {
                controlPanel.setActive(note, tick, false);
            }
        }

        Group colonyGroup = colony.getActor();
        stage.addActor(colonyGroup);

        Actor bacterium = colonyGroup.findActor(Bacterium.FIRST);
        bacterium.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                currentText = "sequence";
                return false;
            }
        });

        currentText = "bacterium";

        tutorialTable.setZIndex(Integer.MAX_VALUE);
    }

    @Override
    public void render(float delta) {
        tutorialLabel.setText(getTutorialText());
        super.render(delta);
    }

    private String getTutorialText() {
        return tutorialText.get(currentText);
    }

    private Table createTutorialTable(GenericListener<Boolean> exitListener) {
        Table table = new Table().pad(10);

        table.setX(0);
        table.setY(0);

        table.setWidth(Globals.GAME_VIEWPORT_WIDTH / 2);
        table.setHeight(Globals.GAME_VIEWPORT_HEIGHT);

        table.row();
        table.add(tutorialLabel).expandX().fillX().top();

        TextButton exitButton = new TextButton("Exit\ntutorial", getSkin());
        exitButton.addListener(new ButtonListener<Boolean>(false, exitListener));

        table.add(exitButton).padTop(40).padBottom(40);

        table.row().expandY();
        table.add();

        stage.addActor(table);
        return table;
    }
}
