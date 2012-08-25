package com.xlr3.tonality.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class Bacterium extends Image {
    public Bacterium() {
        Texture texture = new Texture(Gdx.files.internal("sprite\\bacterium.png"));
        this.setDrawable(new SpriteDrawable(new Sprite(texture)));
    }
}
