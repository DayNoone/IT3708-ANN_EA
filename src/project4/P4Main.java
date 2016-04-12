package project4;

import enums.EProblemSelection;
import general.AbstractHypothesis;
import general.EAController;
import general.Values;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class P4Main extends Application {


    AnimationTimer mainLoop;
    private int generation;

    private boolean solutionFound;
    private P4GUIController p4GuiController;
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


        Values.ANN_OUTPUT_NODES = 2;
        Values.SELECTED_PROBLEM = EProblemSelection.TRACKER;

        Values.CTRNN = new CTRNN();

        Values.BEERWORLD = new BeerWorld();

        p4GuiController = new P4GUIController();

        Pane pane = p4GuiController.generateGUI(this);

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
        numberOfMoves = 600;
        mainLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!simulationPaused) {

                    if (shouldRestart){
                        startTime = System.currentTimeMillis();
                        resetStaticValues();

                        generation = 0;
                        solutionFound = false;
                        p4GuiController.clearGUI();
                        shouldRestart = false;

                        eaController = new EAController();
                        Values.CTRNN = new CTRNN();
                    }

                    if (numberOfMoves < Values.BEERWORLD_ITERATIONS && generation > 0 && Values.DRAW_MOVEMENT){

                        if (now - lastUpdate >= Values.FLATLAND_SLEEP_DURATION * 1000000) { // 20_000_000 = 20ms
                            lastUpdate = now;
                            p4GuiController.drawMovement(numberOfMoves);
                            numberOfMoves++;
                        }

                    }else{
                        numberOfMoves = 0;

                        if (!solutionFound) {
                            generation += 1;

                            eaController.generatePhenotypes();

                            solutionFound = eaController.testAndUpdateFitnessOfPhenotypes();

                            p4GuiController.updateFPS(now, primaryStage);
                            if (generation % Values.GENERATION_PRINT_THROTTLE == 0){
                                updateGUI(now, primaryStage);
                            }

                            if (solutionFound) {
                                solutionFound = true;
                                p4GuiController.appendTextToConsole("\nSolution found!\n");
                                updateGUI(now, primaryStage);
                                long endTime = System.currentTimeMillis();
                                long milliseconds = endTime - startTime;
                                int minutes = (int) ((milliseconds / (1000*60)) % 60);
                                int seconds = (int) (milliseconds / 1000) % 60 ;
                                p4GuiController.appendTextToConsole("\nTook " + minutes + " minutes and " + seconds + " seconds.");
                            }
                            eaController.adultSelection();
                            eaController.parentSelection();

                            eaController.generateNewPopulation();
                        }
                        Values.BEERWORLD.resetBoard();
                        Values.CTRNN.resetNetwork();
                        Values.CTRNN.setNetworkValues(bestHypothesis.getPhenotype());

                    }
                }
            }
        };
        mainLoop.start();
    }

    private void resetStaticValues() {
        if(Values.BEERWORLD_PULL){

            Values.BEERWORLD_FAILEDAVOID_PENALTY = Values.BEERWORLD_PULL_FAILEDAVOID_PENALTY;
            Values.BEERWORLD_FAILEDCAPTURE_PENALTY = Values.BEERWORLD_PULL_FAILEDCAPTURE_PENALTY;
            Values.BEERWORLD_CAPTURE_PRIZE = Values.BEERWORLD_PULL_CAPTURE_PRIZE;
            Values.BEERWORLD_AVOID_PRIZE = Values.BEERWORLD_PULL_AVOID_PRIZE;
            Values.BEERWORLD_PULLED_CAPTURE = Values.BEERWORLD_PULL_PULLED_CAPTURE;
            Values.BEERWORLD_PULLED_AVOID = Values.BEERWORLD_PULL_PULLED_AVOID;
            Values.BEERWORLD_PULLED_FAILEDCAPTURE = Values.BEERWORLD_PULL_PULLED_FAILEDCAPTURE;
            Values.BEERWORLD_PULLED_FAILEDAVOID = Values.BEERWORLD_PULL_PULLED_FAILEDAVOID;

            Values.CTRNN_INPUT_NODES = 5;

        } else if (Values.BEERWORLD_NO_WRAP){

            Values.BEERWORLD_FAILEDAVOID_PENALTY = Values.BEERWORLD_NOWRAP_FAILEDAVOID_PENALTY;
            Values.BEERWORLD_FAILEDCAPTURE_PENALTY = Values.BEERWORLD_NOWRAP_FAILEDCAPTURE_PENALTY;
            Values.BEERWORLD_CAPTURE_PRIZE = Values.BEERWORLD_NOWRAP_CAPTURE_PRIZE;
            Values.BEERWORLD_AVOID_PRIZE = Values.BEERWORLD_NOWRAP_AVOID_PRIZE;
            Values.BEERWORLD_PULLED_CAPTURE = Values.BEERWORLD_NOWRAP_PULLED_CAPTURE;
            Values.BEERWORLD_PULLED_AVOID = Values.BEERWORLD_NOWRAP_PULLED_AVOID;
            Values.BEERWORLD_PULLED_FAILEDCAPTURE = Values.BEERWORLD_NOWRAP_PULLED_FAILEDCAPTURE;
            Values.BEERWORLD_PULLED_FAILEDAVOID = Values.BEERWORLD_NOWRAP_PULLED_FAILEDAVOID;

            Values.CTRNN_INPUT_NODES = 7;

        }else{

            Values.BEERWORLD_FAILEDAVOID_PENALTY = Values.BEERWORLD_WRAP_FAILEDAVOID_PENALTY;
            Values.BEERWORLD_FAILEDCAPTURE_PENALTY = Values.BEERWORLD_WRAP_FAILEDCAPTURE_PENALTY;
            Values.BEERWORLD_CAPTURE_PRIZE = Values.BEERWORLD_WRAP_CAPTURE_PRIZE;
            Values.BEERWORLD_AVOID_PRIZE = Values.BEERWORLD_WRAP_AVOID_PRIZE;
            Values.BEERWORLD_PULLED_CAPTURE = Values.BEERWORLD_WRAP_PULLED_CAPTURE;
            Values.BEERWORLD_PULLED_AVOID = Values.BEERWORLD_WRAP_PULLED_AVOID;
            Values.BEERWORLD_PULLED_FAILEDCAPTURE = Values.BEERWORLD_WRAP_PULLED_FAILEDCAPTURE;
            Values.BEERWORLD_PULLED_FAILEDAVOID = Values.BEERWORLD_WRAP_PULLED_FAILEDAVOID;

            Values.CTRNN_INPUT_NODES = 5;

        }
    }

    private void updateGUI(long now, Stage primaryStage) {

        double avgFitness = eaController.calculateAvarageFitness(eaController.getPopulation());
        bestHypothesis = eaController.getBestHypothesis(eaController.getPopulation());

        p4GuiController.updateLineCharts(eaController.getPopulation(), bestHypothesis.getFitness(), avgFitness, eaController.calculateStandardDeviation(eaController.getPopulation(), avgFitness), generation, bestHypothesis.getPhenotypeString());
//        p4GuiController.updateFPS(now, primaryStage);
//        p4GuiController.drawMovement(bestHypothesis);
    }

    public void restartAlgorithm() {
        shouldRestart = true;
    }
}
