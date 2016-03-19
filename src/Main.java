import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {


    AnimationTimer mainLoop;
    private int generation;

    private boolean solutionFound;
    private GUIController guiController;
    private EAController eaController;
    private boolean shouldRestart;
    public boolean simulationPaused;
    private long startTime;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        guiController = new GUIController();

        Values.BOARD = new Board();
        Values.ANN = new ANN();

        Pane pane = guiController.generateGUI(this);

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

        mainLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!simulationPaused) {

                    if (shouldRestart){
                        startTime = System.currentTimeMillis();
                        eaController = new EAController();
                        generation = 0;
                        solutionFound = false;

                        guiController.clearGUI();

                        shouldRestart = false;
                    }


                    if (!solutionFound) {
                        generation += 1;

                        eaController.generatePhenotypes();

                        solutionFound = eaController.testAndUpdateFitnessOfPhenotypes();

                        guiController.updateFPS(now, primaryStage);
                        if (generation % Values.GENERATION_PRINT_THROTTLE == 0){
                            updateGUI(now, primaryStage);
                        }
                        if (solutionFound) {
                            solutionFound = true;
                            guiController.appendTextToConsole("\nSolution found!\n");
                            updateGUI(now, primaryStage);
                            long endTime = System.currentTimeMillis();
                            long milliseconds = endTime - startTime;
                            int minutes = (int) ((milliseconds / (1000*60)) % 60);
                            int seconds = (int) (milliseconds / 1000) % 60 ;
                            guiController.appendTextToConsole("\nTook " + minutes + " minutes and " + seconds + " seconds.");
                        }
                        eaController.adultSelection();
                        eaController.parentSelection();

                        eaController.generateNewPopulation();
                    }
                }


            }
        };
        mainLoop.start();
    }

    private void updateGUI(long now, Stage primaryStage) {

        double avgFitness = eaController.calculateAvarageFitness(eaController.getPopulation());
        AbstractHypothesis bestHypothesis = eaController.getBestHypothesis(eaController.getPopulation());

        guiController.updateLineCharts(eaController.getPopulation(), bestHypothesis.getFitness(), avgFitness, eaController.calculateStandardDeviation(eaController.getPopulation(), avgFitness), generation, bestHypothesis.getPhenotypeString());
//        guiController.updateFPS(now, primaryStage);
        guiController.drawMovement(bestHypothesis);
    }

    public void restartAlgorithm() {
        shouldRestart = true;
    }
}
