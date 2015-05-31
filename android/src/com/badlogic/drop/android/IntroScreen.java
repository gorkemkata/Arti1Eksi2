package com.badlogic.drop.android;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

import java.math.BigInteger;

public class IntroScreen implements Screen {

    final ArtiBirEksiIki game;

    final Long DELAY = new Long("2300000000");

    Texture megatronImage;
    Rectangle megatron;

    Texture titanImage;
    Rectangle titan;

    Texture galacticaImage;
    Rectangle galactica;

    Texture commanderImage;
    Rectangle commander;

    Array<Texture> introImages;
    Array<Rectangle> intro;
    Music introMusic;

    long start;
    long end;

    int j = 0;

    OrthographicCamera camera;

    public IntroScreen( ArtiBirEksiIki game) {

        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 600, 950);

        introImages = new Array<Texture>();
        intro = new Array<Rectangle>();

        introMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/intro.mp3"));
        introMusic.setLooping(true);

        start = TimeUtils.nanoTime();

        galacticaImage = new Texture( Gdx.files.internal("intro/galactica.png") );
        megatronImage = new Texture( Gdx.files.internal("intro/megatron.png") );
        titanImage = new Texture( Gdx.files.internal("intro/planet.png") );
        commanderImage = new Texture( Gdx.files.internal("intro/commander.png") );

        galactica = new Rectangle();
        galactica.x=10;
        galactica.y=250;
        galactica.width=500;
        galactica.height=250;

        commander = new Rectangle();
        commander.x=350;
        commander.y=0;
        commander.width=288;
        commander.height=320;

        megatron = new Rectangle();
        megatron.x=20;
        megatron.y=840;
        megatron.width=100;
        megatron.height=100;

        titan = new Rectangle();
        titan.x=495;
        titan.y=810;
        titan.width=100;
        titan.height=100;




        for(int i=0;i<14;i++){

            introImages.add( new Texture( Gdx.files.internal("intro/"+ (i+1) +".png") ) );

            intro.add( new Rectangle());
            intro.get(i).x=1;
            intro.get(i).y=800 - (i*40);
            intro.get(i).width=601;
            intro.get(i).height=30;

        }



    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();




        for(int i=0;i<j;i++) {
            game.batch.draw(introImages.get(i), intro.get(i).x, intro.get(i).y);
        }

        if((TimeUtils.nanoTime() - start) > DELAY) {
            start = TimeUtils.nanoTime();
            if(j<14)
            j++;
         }

        if(j>4)
            game.batch.draw(titanImage, titan.x, titan.y);
        if(j>=0 && j < 7)
            game.batch.draw(galacticaImage, galactica.x, galactica.y);
        if(j>2)
            game.batch.draw(megatronImage, megatron.x, megatron.y);
        if(j==14)
            game.batch.draw(commanderImage, commander.x, commander.y);


        game.batch.end();

        if (Gdx.input.isTouched()) {

            Vector3 touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);

            if(touchPosition.y > 810)
            game.setScreen(ArtiBirEksiIki.gameScreen);

            dispose();
        }
    }


    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        introMusic.play();
    }

    @Override
    public void hide() {
        j=0;
        introMusic.stop();
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