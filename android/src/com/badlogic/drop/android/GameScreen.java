package com.badlogic.drop.android;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameScreen implements Screen {

    final ArtiBirEksiIki game;

    final int MAX_LEVEL = 6;

    Texture backgroundImage;

    Texture submitButtonImage;
    Rectangle submitButton;

    Texture zoomInButtonImage;
    Rectangle zoomInButton;

    Texture zoomOutButtonImage;
    Rectangle zoomOutButton;

    Texture mainMenuButtonImage;
    Rectangle mainMenuButton;

    Texture restartButtonImage;
    Rectangle restartButton;

    Array<Texture> numberImages;
    Array<Rectangle> numbers;

    Array<Texture> shipImages;
    Array<Rectangle> ships;

    Texture destroyedShipImage;
    Rectangle destroyedShip;

    OrthographicCamera camera;

    List<Integer> usersNumber = new ArrayList<Integer>();
    Set<Integer> computersNumber = new HashSet<Integer>();

    List<Integer> usersGuess = new ArrayList<Integer>();
    // List<Integer> computersGuess = new ArrayList<Integer>(); AI Not implemented yet

    List<Integer> selectedNumbers = new ArrayList<Integer>();

    List<Integer> guessResult = new ArrayList<Integer>();
    int plus;
    int minus;

    int touchedNumber = 10;
    int selectedNumbersCount=0;
    int numberLevel=2;
    int guessCount=0;
    int points=100;

    Boolean userNumberSelected = false;
    Boolean computerNumberSelected = false;
    Boolean gameFinished = false;

    String guessHistory = "Guess History\n------------------------------------\n";


    public GameScreen( ArtiBirEksiIki game ) {

        this.game = game;

        // Init background image
        backgroundImage = new Texture(Gdx.files.internal("background.png"));

        // Init number images
        numberImages = new Array<Texture>();
        for(int i=1;i<10;i++){
            numberImages.add(new Texture(Gdx.files.internal("numbers/" + i + ".png")));
        }

        // Init number rectangles
        numbers = new Array<Rectangle>();
        for(int i=0;i<9;i++){
            numbers.add(new Rectangle());
            numbers.get(i).x = 50 + (i*60);
            numbers.get(i).y=810;
            numbers.get(i).width = 50;
            numbers.get(i).height = 50;
        }

        // Init ship images
        shipImages = new Array<Texture>();
        for(int i=1;i<6;i++){
            shipImages.add(new Texture(Gdx.files.internal("ships/ship"+ i + ".png")));
        }

        // Init ship rectangles
        ships = new Array<Rectangle>();
        for(int i=0;i<5;i++){
            ships.add(new Rectangle());
            ships.get(i).x = 50 + (i*80) + MathUtils.random(10,150);
            ships.get(i).y=110 + (i*100) ;
            ships.get(i).width = 64;
            ships.get(i).height = 64;
        }

        // Init button images and rectangles
        submitButtonImage = new Texture(Gdx.files.internal("buttons/button.png"));
        submitButton = new Rectangle();
        submitButton.x=300;
        submitButton.y=300;
        submitButton.width=87;
        submitButton.height=51;

        zoomInButtonImage = new Texture(Gdx.files.internal("buttons/zoomin.png"));
        zoomInButton = new Rectangle();
        zoomInButton.x=565;
        zoomInButton.y=700;
        zoomInButton.width=69;
        zoomInButton.height=43;

        zoomOutButtonImage = new Texture(Gdx.files.internal("buttons/zoomout.png"));
        zoomOutButton = new Rectangle();
        zoomOutButton.x=560;
        zoomOutButton.y=650;
        zoomOutButton.width=83;
        zoomOutButton.height=43;

        mainMenuButtonImage = new Texture(Gdx.files.internal("buttons/main.png"));
        mainMenuButton = new Rectangle();
        mainMenuButton.x=552;
        mainMenuButton.y=600;
        mainMenuButton.width=96;
        mainMenuButton.height=41;

        restartButtonImage = new Texture(Gdx.files.internal("buttons/restart.png"));
        restartButton = new Rectangle();
        restartButton.x=275;
        restartButton.y=450;
        restartButton.width=150;
        restartButton.height=51;

        destroyedShipImage = new Texture(Gdx.files.internal("ships/blast.png"));
        destroyedShip = new Rectangle();
        destroyedShip.x=0;
        destroyedShip.y=0;
        destroyedShip.width=0;
        destroyedShip.height=0;

        // Init camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 650, 900);

        // AI Not implemented yet
        userNumberSelected = true;

    }


    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // BEGIN DRAWING
        game.batch.begin();
        game.batch.draw(backgroundImage, 0,0);

        // Draw numbers
        for(int i=0;i<9;i++){
            game.batch.draw(numberImages.get(i), numbers.get(i).x, numbers.get(i).y);
        }

        // Draw ships
        for(int i=0;i<(7-numberLevel);i++){
            game.batch.draw(shipImages.get(i), ships.get(i).x, ships.get(i).y);
        }

        for(int i=(7-numberLevel);i<5;i++){
            game.batch.draw(destroyedShipImage, ships.get(i).x, ships.get(i).y);
        }

        // Draw buttons
        game.batch.draw(zoomInButtonImage, zoomInButton.x, zoomInButton.y);
        game.batch.draw(zoomOutButtonImage, zoomOutButton.x, zoomOutButton.y);
        game.batch.draw(mainMenuButtonImage, mainMenuButton.x, mainMenuButton.y);

        // If enough numbers selected, show the OK! button.
        if(selectedNumbersCount == numberLevel)
            game.batch.draw(submitButtonImage, submitButton.x, submitButton.y);

        // If the game is finished, show the Restart button.
        if(gameFinished)
            game.batch.draw(restartButtonImage, restartButton.x, restartButton.y);

        //Print Camera zoom
        game.font.draw(game.batch, "Camera zoom: "+ camera.zoom, 430, 20);

        //Print Points
        game.font.draw(game.batch, "Points: "+ points, 10, 780);

        //Print Level
        game.font.draw(game.batch, "Level: "+ (numberLevel-1), 10, 750);

        // If it is a guess or the initial number selection
        if(!userNumberSelected) {
            game.font.draw(game.batch, "Please select your number", 10, 720);
        }else
        {
            generateComputersNumber();
            computerNumberSelected = true;

            game.font.draw(game.batch, "Take your guess! "+(10-guessCount)+" guesses remained!", 10, 720);
            game.font.draw(game.batch, guessHistory, 10, 680);
        }

        game.batch.end();
        // END DRAWING

        // TOUCH LISTENER
        if (Gdx.input.isTouched()) {

            Vector3 touchPosition = new Vector3();
            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            touchHandler(touchPosition);

        }

        // move the selected number along the y-axis
        for(int i=0;i<selectedNumbersCount;i++){

            if(touchedNumber != 10 && numbers.get(selectedNumbers.get(i)).y > 400) {
                numbers.get(selectedNumbers.get(i)).y -= 400 * Gdx.graphics.getDeltaTime();
            }

        }

        // Move spaceships around. Don't move if destroyed.
        if( numberLevel < 7)
            ships.get(0).x += 130 * Gdx.graphics.getDeltaTime();
        if( numberLevel < 6)
            ships.get(1).x -= 230 * Gdx.graphics.getDeltaTime();
        if( numberLevel < 5)
            ships.get(2).x += 250 * Gdx.graphics.getDeltaTime();
        if( numberLevel < 4)
            ships.get(3).x -= 150 * Gdx.graphics.getDeltaTime();
        if( numberLevel < 3)
            ships.get(4).x += 180 * Gdx.graphics.getDeltaTime();

        for(int i=0;i<5;i++){

                if(ships.get(i).x < -50 && i%2 == 1)
                    ships.get(i).x = 750 + MathUtils.random(10,250);
                if(ships.get(i).x > 800 && i%2 == 0)
                    ships.get(i).x = -50 + MathUtils.random(10,250);

        }


    }

    public void touchHandler(Vector3 touchPos){

        // For our own game space coordinates
        camera.unproject(touchPos);


        switch(isFunctionalButtonsTouched(touchPos)){

            case 1: // Camera zoom in
                camera.zoom -= 0.01;
                break;

            case 2: // Camera zoom out
                camera.zoom += 0.01;
                break;

            case 3: // Return to main menu
                game.setScreen(ArtiBirEksiIki.mainMenuScreen);
                restartGame();
                resetNumbersPositions();
                break;

            case 4: // Restart the game
                restartGame();
                resetNumbersPositions();
                gameFinished = false;
                break;

            default: break;


        }

        // move the selected number along the x-axis and add it to selectedNumbers.
        if( isNumberTouched(touchPos) && selectedNumbersCount < numberLevel && !selectedNumbers.contains(touchedNumber)  ){

            selectedNumbersCount++;
            selectedNumbers.add(touchedNumber);

            numbers.get(touchedNumber).x = 180 + selectedNumbersCount * 60 ;

        }

        // if the user completed his/her selection and pressed the OK! button, clear everything.
        if(selectedNumbersCount == numberLevel && isOKButtonTouched(touchPos)) {

            if(!userNumberSelected) { // INIT USERS NUMBER (AI not implemented yet)
                usersNumber = selectedNumbers;
                userNumberSelected = true;
            }
            else { // TAKE A GUESS
                usersGuess = selectedNumbers;

                guessResult = generateResult(usersGuess,computersNumber);
                plus = guessResult.get(0);
                minus = guessResult.get(1);

                if(guessCount > 10){
                    restartGame();
                    guessHistory = "YOU LOST! YOUR SCORE IS: "+points+" . Take a guess to start a new game!" +
                            "\n------------------------------------\n";
                }
                else if(numberLevel == MAX_LEVEL && plus == numberLevel){
                    guessHistory = "YOU WON! You destroyed ALL of the AI commanders! Our star system is safe NOW!\n";
                    gameFinished = true;
                    numberLevel++;
                }
                else if(plus == numberLevel) {
                    points += 100;
                    guessHistory = "YOU WON! You destroyed one of the AI commanders!\n" +
                            "You gathered "+(100 - guessCount*10)+" points at level "+(numberLevel-1)+". Now next level is "+(numberLevel+1)+" digits!" +
                            "\n------------------------------------";
                    computerNumberSelected = false;
                    guessCount = 0;
                    if(numberLevel < 7) {
                        numberLevel++;
                        generateComputersNumber();
                    }
                }
                else {
                    points -= 10;
                    guessCount++;
                    guessHistory += "\nYour guess: ";
                    for (int i = 0; i < numberLevel; i++) {
                        guessHistory += (usersGuess.get(i) + 1);
                    }
                    guessHistory += "                |\n Result:  ";
                    if (plus > 0 && minus > 0)
                        guessHistory += "Plus " + plus + "  ,  Minus " + minus + "  |\n------------------------------------";
                    if (plus > 0 && minus == 0)
                        guessHistory += "Plus " + plus + "                    |\n------------------------------------";
                    if (plus == 0 && minus > 0)
                        guessHistory += "Minus " + minus + "                 |\n------------------------------------";
                    if (plus == 0 && minus == 0)
                        guessHistory += "ZERO!                  |\n------------------------------------";
                }
            }

            resetNumbersPositions();

        }
    }

    public Boolean isNumberTouched(Vector3 touchPoint){

        Boolean isNumberTouched = false;
        for(int i=0;i<9;i++){
            if(numbers.get(i).contains(touchPoint.x,touchPoint.y)){
                isNumberTouched = true;
                touchedNumber = i;
                break;
            }
        }

        return isNumberTouched;
    }

    public void generateComputersNumber() {
        if (!computerNumberSelected) {
            computersNumber.clear();
            while ( computersNumber.size() < numberLevel) {
                computersNumber.add(MathUtils.random(1, 9));
            }
        }
    }

    public void generateComputersGuess() {



    }

    public List<Integer> generateResult(List<Integer> guess, Set<Integer> selectedNumber) {

        List<Integer> result = new ArrayList<Integer>();
        List<Integer> number = new ArrayList<Integer>();
        number.addAll(selectedNumber);
        int p = 0;
        int m = 0;
        Log.w("22222","Guess: " + guess.toString());
        Log.w("22222","Number: " + number.toString());

        for (int i = 0; i < numberLevel; i++) {
            if(number.contains( (guess.get(i)+1) ) ) {
                for (int j = 0; j < numberLevel; j++) {
                    if ( ( guess.get(i)+1 ) == number.get(i) ) {
                        p++;
                        break;
                    }
                    else {
                        m++;
                        break;
                    }
                }
            }
        }
        result.add(p);
        result.add(m);
        return result;
    }

    public Boolean isOKButtonTouched(Vector3 touchPoint){
        return submitButton.contains(touchPoint.x, touchPoint.y);
    }

    public int isFunctionalButtonsTouched(Vector3 touchPoint){

        if( zoomInButton.contains(touchPoint.x, touchPoint.y) )
            return 1;

        if( zoomOutButton.contains(touchPoint.x, touchPoint.y) )
            return 2;

        if( mainMenuButton.contains(touchPoint.x, touchPoint.y) )
            return 3;

        if( restartButton.contains(touchPoint.x, touchPoint.y) )
            return 4;

        return 0;
    }

    public void restartGame(){

        guessHistory = "Guess History\n------------------------------------\n";
        points = 100;
        numberLevel = 2;
        guessCount=0;
        computerNumberSelected = false;
        generateComputersNumber();

    }

    public void resetNumbersPositions(){

        for(int i=0;i<selectedNumbersCount;i++) {
            numbers.get(selectedNumbers.get(i)).x = 50 + (selectedNumbers.get(i)*60);
            numbers.get(selectedNumbers.get(i)).y = 810;
        }
        selectedNumbersCount = 0;
        selectedNumbers.clear();
        touchedNumber=10;

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
    //TODO

    }

}