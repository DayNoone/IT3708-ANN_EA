package project5;

import enums.EProblemSelection;
import general.Values;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class P5Main extends Application {


    private int generationCounter;

    private P5GUIController p5GuiController;
    private MTSPEAController eaController;
    private boolean shouldRestart;
    boolean simulationPaused;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        XLSXReader xlsxReader = new XLSXReader();
        Values.MTSP_DISTANCES = xlsxReader.read("src/project5/Distance.xlsx", 48);
        Values.MTSP_COSTS = xlsxReader.read("src/project5/Cost.xlsx", 48);

        Values.SELECTED_PROBLEM = EProblemSelection.MTSP;

        p5GuiController = new P5GUIController();

        Pane pane = p5GuiController.generateGUI(this);

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();

//         run generationCounter loop
        startEvolutionaryAlgorithmLoop();
    }

    private void startEvolutionaryAlgorithmLoop() {

        eaController = new MTSPEAController();
        eaController.generateInitialPopulation(new MTSPHypothesis(), Values.POPULATION_SIZE + Values.NUMBER_OF_ELITES);
        generationCounter = 0;

        AnimationTimer mainLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!simulationPaused) {

                    if (shouldRestart) {
                        generationCounter = 0;
                        p5GuiController.clearGUI();
                        shouldRestart = false;

                        eaController = new MTSPEAController();
                        eaController.generateInitialPopulation(new MTSPHypothesis(), Values.POPULATION_SIZE + Values.NUMBER_OF_ELITES);

                    }
                    if (generationCounter >= Values.MAX_GENERATIONS && Values.MAX_GENERATIONS > 0){
                        p5GuiController.setLegendParametersText();
                    }else{
                        generationCounter += 1;

                        eaController.generatePhenotypes();

                        eaController.testAndUpdateFitnessOfPhenotypes();



                        eaController.adultSelection();

                        if (Values.GENERATION_PRINT_THROTTLE != 0 && generationCounter % Values.GENERATION_PRINT_THROTTLE == 0) {
                            updateGUI();
                        }


                        eaController.parentSelection();
                        eaController.generateNewPopulation();
                    }
                }
            }
        };
        mainLoop.start();
    }

    private void updateGUI() {
        MTSPHypothesis bestNonInfiniteHypothesis = eaController.getBestNonInfiniteHypothesis(eaController.getPopulation());

        MTSPHypothesis bestHypothesis = eaController.getBestHypothesis(eaController.getPopulation());
        MTSPHypothesis worstHypothesis = eaController.getWorstHypothesis(eaController.getPopulation());

        p5GuiController.updateLineCharts(generationCounter, bestHypothesis.getPhenotypeString(), bestHypothesis, worstHypothesis, eaController.getPopulation(), bestNonInfiniteHypothesis);
    }

    void restartAlgorithm() {
        shouldRestart = true;
    }
}
