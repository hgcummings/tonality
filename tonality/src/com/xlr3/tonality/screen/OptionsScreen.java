package com.xlr3.tonality.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.xlr3.tonality.Globals;
import com.xlr3.tonality.Options;
import com.xlr3.tonality.TonalityGame;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class OptionsScreen extends AbstractScreen {
    private static final String HELP_TEXT_DEFAULT = "Hover or drag over a label for more information";
    private ArrayList<SliderListener> sliderListeners = new ArrayList<SliderListener>();

    private Label helpTextLabel;

    @SuppressWarnings("unchecked")
    private static OrderedMap<String, OrderedMap<Object, Object>> optionsText
        = (OrderedMap<String, OrderedMap<Object, Object>>) new JsonReader().parse(Gdx.files.internal("text/options.json"));

    public OptionsScreen(Options currentOptions, final GenericListener<Options> exitListener) {
        final Table table = new Table(getSkin());

        table.setWidth(Globals.GAME_VIEWPORT_WIDTH);
        table.setHeight(Globals.GAME_VIEWPORT_HEIGHT);

        table.row();
        table.add("Options").colspan(10);

        helpTextLabel = new Label(HELP_TEXT_DEFAULT, getSkin());
        helpTextLabel.setAlignment(Align.center, Align.center);

        table.row();
        table.add(helpTextLabel).colspan(10).height(120).expandX();

        for (ObjectMap.Entry<String, OrderedMap<Object, Object>> details : optionsText.entries()) {
            addInputRow(table, details);
        }

        Button resetButton = new TextButton("Restore defaults", getSkin());
        resetButton.addListener(new ButtonListenerEmpty(new Runnable() {
            @Override
            public void run() {
                loadOptions(table, Options.DEFAULT);
            }
        }));
                
        Button saveButton = new TextButton("Save", getSkin());
        saveButton.addListener(new ButtonListenerEmpty(new Runnable() {
            @Override
            public void run() {
                exitListener.fire(createOptions());
            }
        }));

        table.row().pad(40);
        table.add(resetButton).height(80).colspan(5).expandX().fillX();
        table.add(saveButton).height(80).colspan(5).expandX().fillX();

        loadOptions(table, currentOptions);

        stage.addActor(table);
    }


    private void loadOptions(Group group, Options options) {
        for (Field field : Options.class.getDeclaredFields()) {
            Slider slider = (Slider) group.findActor(field.getName() + "Slider");
            if (slider == null) {
                continue;
            }
            try {
                Object value = field.get(options);
                if (value instanceof Float)
                {
                    slider.setValue((Float)value);
                } else if (value instanceof Integer) {
                    slider.setValue((Integer) value);
                } else {
                    throw new Exception();
                }
            } catch (Exception e) {
                Gdx.app.log(TonalityGame.LOG, "Error loading Option field " + field.getName());
            }
        }
        for (SliderListener sliderListener : sliderListeners) {
            sliderListener.fire();
        }
    }

    private Options createOptions() {
        return new Options(
            Math.round(getValue("notes")),
            Math.round(getValue("ticks")),
            Math.round(getValue("seed")),
            getValue("mutationRate"),
            Math.round(getValue("maxAge")),
            Math.round(getValue("startPhase")),
            Options.DEFAULT.intervals,
            Options.DEFAULT.rootNote
        );
    }

    private float getValue(String name) {
        Slider slider = (Slider) stage.getRoot().findActor(name + "Slider");
        return slider.getValue();
    }

    private void addInputRow(Table table, ObjectMap.Entry<String, OrderedMap<Object, Object>> optionDetails) {
        table.row();
        Label label = new Label((String) optionDetails.value.get("name"), getSkin());
        final String helpText = (String) optionDetails.value.get("description");

        label.addListener(new InputListener() {
            @Override
            public void enter (InputEvent event, float x, float y, int pointer, Actor fromActor) {
                helpTextLabel.setText(helpText);
            }

            @Override
            public void exit (InputEvent event, float x, float y, int pointer, Actor toActor) {
                helpTextLabel.setText(HELP_TEXT_DEFAULT);
            }
        });

        table.add(label).colspan(3);

        Float step = (Float)optionDetails.value.get("step");

        Slider slider = new Slider(
                (Float)optionDetails.value.get("min"),
                (Float)optionDetails.value.get("max"),
                step,
                false,
                getSkin());

        slider.setName(optionDetails.key + "Slider");

        table.add(slider).fillX().expandX().colspan(5);

        final Label valueLabel = new Label("", getSkin());
        table.add(valueLabel).colspan(2);

        SliderListener sliderListener = new SliderListener(valueLabel, slider, step);
        slider.addListener(sliderListener);
        sliderListeners.add(sliderListener);
    }

    private static class SliderListener extends InputListener {
        private final Label valueLabel;
        private final Slider slider;
        private final float step;

        private SliderListener(Label valueLabel, Slider slider, float step) {
            this.valueLabel = valueLabel;
            this.slider = slider;
            this.step = step;
        }

        @Override
        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            fire();
        }

        @Override
        public void touchDragged (InputEvent event, float x, float y, int pointer) {
            fire();
        }

        public void fire() {
            valueLabel.setText(String.format(step < 1.0f ? "%.2f" : "%.0f", slider.getValue()));
        }
    }
}
