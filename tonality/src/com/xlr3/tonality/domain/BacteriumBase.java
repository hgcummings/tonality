package com.xlr3.tonality.domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xlr3.tonality.Globals;

public abstract class BacteriumBase extends Image {
    protected static final int SIZE = 32;
    protected static final int MAX_EXTENT_X = (Globals.GAME_VIEWPORT_WIDTH / 2) - SIZE;
    protected static final int MAX_EXTENT_Y = (Globals.GAME_VIEWPORT_HEIGHT) - SIZE;
    private float age;

    public BacteriumBase() {
        Texture texture = new Texture(Gdx.files.internal("sprite/bacterium.png"));
        TextureRegion textureRegion = new TextureRegion();
        textureRegion.setRegion(texture);
        this.setDrawable(new TextureRegionDrawable(textureRegion));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        age += delta;
    }

    public float getAge() {
        return age;
    }

    protected void initialise(float x, float y) {
        this.setPosition(x, y);
        this.setWidth(SIZE);
        this.setHeight(SIZE);
        this.invalidate();
        this.age = 0;
    }

    protected boolean tryUpdateX(float dx) {
        float newX = this.getX() + dx;

        if (newX > MAX_EXTENT_X) {
            this.setX(MAX_EXTENT_X);
        } else if (newX < 0) {
            this.setX(0);
        } else {
            this.setX(newX);
            return true;
        }

        return false;
    }

    protected boolean tryUpdateY(float dy) {
        float newY = this.getY() + dy;

        if (newY > MAX_EXTENT_Y) {
            this.setY(MAX_EXTENT_Y);
        } else if (newY < 0) {
            this.setY(0);
        } else {
            this.setY(newY);
            return true;
        }

        return false;
    }
}
