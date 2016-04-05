package project4;

import general.Values;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by DagInge on 01/04/2016.
 */
public class BeerWorld {

    private int objectXPos, objectYPos, objectSize;
    private int trackerXPos;

    private int fallenObjects, captured, avoided, failedCapture, failedAvoid;

    private static Random random = new Random();

    public BeerWorld(){
        trackerXPos = 0;
        spawnObject();

//        System.out.println(captured + " " + avoided);
//        System.out.println(failedCapture + " " + failedAvoid);
    }

    private void spawnObject(){
        objectXPos = random.nextInt(Values.BEERWORLD_BOARD_WIDTH);
        objectYPos = 0;
        objectSize = random.nextInt(5) + 1;
        fallenObjects++;
    }

    public void playTimestep(int trackerMove) {
        // Tracker move is -1 for one move left and 2 for two moves right
        if (++objectYPos == Values.BEERWORLD_BOARD_HEIGHT-1) {
            checkCollision();
            spawnObject();
        }


        if(trackerMove == 0) {
            // TODO: Perform pull
        } else {
            moveTracker(trackerMove);
        }
    }

    private void checkCollision() {
        if (objectSize < 5) {
            if (objectXPos >= trackerXPos  && trackerXPos + 5 >= objectXPos + objectSize) {
                captured++;
            } else {
                failedCapture++;
            }
        } else {
            if (trackerXPos + 4 < objectXPos || trackerXPos >= objectXPos + objectSize){
                avoided++;
            } else {
                failedAvoid++;
            }
        }
    }

    private void moveTracker(int trackerMove) {
        trackerXPos = (trackerXPos + trackerMove + Values.BEERWORLD_BOARD_WIDTH) % Values.BEERWORLD_BOARD_WIDTH;
    }

    public ArrayList<Integer> getSensors(){
        ArrayList<Integer> sensors = new ArrayList<>();
        for (int x = trackerXPos; x < trackerXPos + 5; x++) {
            boolean overlap = false;
            for (int y = 0; y < Values.BEERWORLD_BOARD_HEIGHT; y++) {
                if (x >= objectXPos && x < objectXPos + objectSize) {
                    overlap = true;
                    break;
                }
            }
            sensors.add(overlap ? 1 : 0);
        }
        return sensors;
    }

    public GridPane generateBeerWorldGridPane() {
        GridPane gridPane = new GridPane();

        for(int y = 0; y < Values.BEERWORLD_BOARD_HEIGHT; y++) {
            for (int x = 0; x < Values.BEERWORLD_BOARD_WIDTH; x++) {
                HBox tile = new HBox();
                tile.setPrefHeight(20);
                tile.setPrefWidth(20);
                String color;
                if (y == Values.BEERWORLD_BOARD_HEIGHT - 1 && x >= trackerXPos && x < trackerXPos + 5) {
                    color = "black";
                } else if (y == objectYPos && x >= objectXPos && x < objectXPos + objectSize) {
                    color = "yellow";
                } else {
                    color = "white";
                }
                tile.setStyle("-fx-background-color: "+color+";");
                gridPane.add(tile, x, y);
            }
        }
        gridPane.setGridLinesVisible(true);
        return gridPane;
    }

    public int getFallenObjects() {
        return fallenObjects;
    }

    public int getCaptured() {
        return captured;
    }

    public int getAvoided() {
        return avoided;
    }

    public int getFailedCapture() {
        return failedCapture;
    }

    public int getFailedAvoid() {
        return failedAvoid;
    }
}
