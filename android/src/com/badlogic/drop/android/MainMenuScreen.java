package com.badlogic.drop.android;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class MainMenuScreen implements Screen {

    final ArtiBirEksiIki game;

    Texture startButtonImage;
    Rectangle startButton;

    Texture exitButtonImage;
    Rectangle exitButton;
    int touchedButton=0;
    OrthographicCamera camera;

    public MainMenuScreen( ArtiBirEksiIki game) {

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 950);


        startButtonImage = new Texture(Gdx.files.internal("buttons/start.png"));
        startButton = new Rectangle();
        startButton.x=245;
        startButton.y=500;
        startButton.width=157;
        startButton.height=51;

        exitButtonImage = new Texture(Gdx.files.internal("buttons/exit.png"));
        exitButton = new Rectangle();
        exitButton.x=250;
        exitButton.y=400;
        exitButton.width=148;
        exitButton.height=51;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        game.batch.draw(startButtonImage, startButton.x, startButton.y);
        game.batch.draw(exitButtonImage, exitButton.x, exitButton.y);

        game.batch.end();

        if (Gdx.input.isTouched()) {

            Vector3 touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            touchedButton = isButtonsTouched(touchPosition);

            if(touchedButton == 1)
                game.setScreen(ArtiBirEksiIki.introScreen);
            if(touchedButton == 2)
                System.exit(0);

            dispose();
        }
    }

    public int isButtonsTouched(Vector3 touchPos){

        if(startButton.contains(touchPos.x, touchPos.y))
            return 1;
        if(exitButton.contains(touchPos.x, touchPos.y))
            return 2;
        return 0;

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}