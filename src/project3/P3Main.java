package project3;

import general.ANN;
import general.AbstractHypothesis;
import general.EAController;
import general.Values;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import project3.Board;

public class P3Main extends Application {


    AnimationTimer mainLoop;
    private int generation;

    private boolean solutionFound;
    private P3GUIController p3GuiController;
    private EAController eaController;
    private boolean shouldRestart;
    public boolean simulationPaused;
    private long startTime;
    private AbstractHypothesis bestHypothesis;
    private int numberOfMoves;
    private long lastUpdate;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Values.ANN_INPUT_NODES = 6;
        Values.ANN_OUTPUT_NODES = 3;
        p3GuiController = new P3GUIController();

        Values.BOARD = new Board();
        Values.BOARDS = new Board[5];
        for(int i = 0; i < Values.FLATLAND_NUMBER_OF_DIFFERENT_SCENARIOS; i++) {
            Values.BOARDS[i] = new Board();
        }

        Values.ANN = new ANN();

        Pane pane = p3GuiController.generateGUI(this);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

        // run generation loop
        startEvolutionaryAlgorithmLoop(primaryStage);
    }

    private void startEvolutionaryAlgorithmLoop(Stage primaryStage) {

        eaController = new EAController();
        generation = 0;
        solutionFound = false;
        startTime = System.currentTimeMillis();
        numberOfMoves = 60;

        mainLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!simulationPaused) {

                    if (shouldRestart){
                        startTime = System.currentTimeMillis();
                        eaController = new EAController();
                        generation = 0;
                        solutionFound = false;

                        p3GuiController.clearGUI();

                        shouldRestart = false;
                    }

                    if (numberOfMoves < Values.FLATLAND_ITERATIONS){

                        if (now - lastUpdate >= Values.FLATLAND_SLEEP_DURATION * 1000000) { // 20_000_000 = 20ms
                            lastUpdate = now;
                            p3GuiController.drawMovement(numberOfMoves);
                            numberOfMoves++;
                        }

                    }else{
                        numberOfMoves = 0;

                        if (!solutionFound) {
                            generation += 1;

                            eaController.generatePhenotypes();

                            solutionFound = eaController.testAndUpdateFitnessOfPhenotypes();

                            p3GuiController.updateFPS(now, primaryStage);
                            if (generation % Values.GENERATION_PRINT_THROTTLE == 0){
                                updateGUI(now, primaryStage);
                            }

                            if (solutionFound) {
                                solutionFound = true;
                                p3GuiController.appendTextToConsole("\nSolution found!\n");
                                updateGUI(now, primaryStage);
                                long endTime = System.currentTimeMillis();
                                long milliseconds = endTime - startTime;
                                int minutes = (int) ((milliseconds / (1000*60)) % 60);
                                int seconds = (int) (milliseconds / 1000) % 60 ;
                                p3GuiController.appendTextToConsole("\nTook " + minutes + " minutes and " + seconds + " seconds.");
                            }
                            eaController.adultSelection();
                            eaController.parentSelection();

                            eaController.generateNewPopulation();
                        }

                        if (Values.FLATLAND_DYNAMIC){
                            Values.BOARD = new Board();
                            Values.BOARDS = new Board[5];
                            for(int i = 0; i < Values.FLATLAND_NUMBER_OF_DIFFERENT_SCENARIOS; i++) {
                                Values.BOARDS[i] = new Board();
                            }

                        }
                        Values.BOARD.resetBoard();
                        for(Board board: Values.BOARDS){
                            board.resetBoard();
                        }
                        Values.ANN.setNetworkWeights(bestHypothesis.getPhenotype());

                    }
                }
            }
        };
        mainLoop.start();
    }

    private void updateGUI(long now, Stage primaryStage) {

        double avgFitness = eaController.calculateAvarageFitness(eaController.getPopulation());
        bestHypothesis = eaController.getBestHypothesis(eaController.getPopulation());

        p3GuiController.updateLineCharts(eaController.getPopulation(), bestHypothesis.getFitness(), avgFitness, eaController.calculateStandardDeviation(eaController.getPopulation(), avgFitness), generation, bestHypothesis.getPhenotypeString());
//        p3GuiController.updateFPS(now, primaryStage);
//        p3GuiController.drawMovement(bestHypothesis);
    }

    public void restartAlgorithm() {
        shouldRestart = true;
    }
}
