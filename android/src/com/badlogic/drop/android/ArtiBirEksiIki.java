package com.badlogic.drop.android;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ArtiBirEksiIki extends Game {

    SpriteBatch batch;
    BitmapFont font;
    static GameScreen gameScreen;
    static IntroScreen introScreen;
    static MainMenuScreen mainMenuScreen;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        gameScreen = new GameScreen(this);
        introScreen = new IntroScreen(this);
        mainMenuScreen = new MainMenuScreen(this);

        this.setScreen(mainMenuScreen);

    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}