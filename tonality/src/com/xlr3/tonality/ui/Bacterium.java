package com.xlr3.tonality.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.xlr3.tonality.Constants;

import java.util.Random;

public class Bacterium extends Image {
    private final Vector2 velocity;
    private static Random random = new Random();
    private static final int SIZE = 32;
    private static final int MAX_EXTENT_X = (Constants.GAME_VIEWPORT_WIDTH / 2) - SIZE;
    private static final int MAX_EXTENT_Y = (Constants.GAME_VIEWPORT_HEIGHT) - SIZE;
    private float age;

    public Bacterium() {
        Texture texture = new Texture(Gdx.files.internal("sprite\\bacterium.png"));
        TextureRegion textureRegion = new TextureRegion();
        textureRegion.setRegion(texture);

        this.setWidth(SIZE);
        this.setHeight(SIZE);

        this.setDrawable(new TextureRegionDrawable(textureRegion));
        this.velocity = new Vector2();
    }

    public void initialise(float x, float y) {
        this.velocity.x = random.nextFloat() - 0.5f;
        this.velocity.y = random.nextFloat() - 0.5f;
        this.setPosition(x, y);
        this.age = 0;
    }

    @Override
    public void act(float delta) {
        float dx = delta * velocity.x * 100;
        float dy = delta * velocity.y * 100;

        float newX = this.getX() + dx;
        float newY = this.getY() + dy;

        if (newX > MAX_EXTENT_X) {
            velocity.x = -velocity.x;
            this.setX(MAX_EXTENT_X);
        } else if (newX < 0) {
            velocity.x = -velocity.x;
            this.setX(0);
        } else {
            this.setX(newX);
        }

        if (newY > MAX_EXTENT_Y) {
            velocity.y = -velocity.y;
            this.setY(MAX_EXTENT_Y);
        } else if (newY < 0) {
            velocity.y = -velocity.y;
            this.setY(0);
        } else {
            this.setY(newY);
        }

        age += delta;
    }

    public float getAge()
    {
        return age;
    }
}
