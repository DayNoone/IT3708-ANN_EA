package project5;

import enums.EAdultSelection;
import enums.EParentSelection;
import general.Values;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Created by markus on 21.02.2016.
 */
@SuppressWarnings("unchecked")
public class P5GUIController {

    NumberAxis xAxis = new NumberAxis(0, Values.POPULATION_SIZE + Values.NUMBER_OF_ELITES - 1, 2);
    NumberAxis yAxis = new NumberAxis(0, 1, 0.1);

    LineChart<Number, Number> populationFitnessLineChart = new LineChart<>(xAxis, yAxis);
    final NumberAxis xAxis2 = new NumberAxis();
    final NumberAxis yAxis2 = new NumberAxis();
//    final NumberAxis yAxis2 = new NumberAxis(0, 1, 0.1);

    final LineChart<Number, Number> maxFitnessLineChart = new LineChart<>(xAxis2, yAxis2);
    final NumberAxis xAxis3 = new NumberAxis();
//    final NumberAxis yAxis3 = new NumberAxis(0, 1, 0.1);
    final NumberAxis yAxis3 = new NumberAxis();

    final LineChart<Number, Number> avarageFitnessLineChart = new LineChart<>(xAxis3, yAxis3);
    final NumberAxis xAxis4 = new NumberAxis();
    final NumberAxis yAxis4 = new NumberAxis();
    final LineChart<Number, Number> stdFitnessLineChart = new LineChart<>(xAxis4, yAxis4);

    final NumberAxis xAxis5 = new NumberAxis();
    final NumberAxis yAxis5 = new NumberAxis();
    final ScatterChart<Number, Number> bestWorstScatter = new ScatterChart<Number, Number>(xAxis5, yAxis5);

    final NumberAxis xAxis6 = new NumberAxis();
    final NumberAxis yAxis6 = new NumberAxis();
    final LineChart<Number, Number> paretoFrontLineChart = new LineChart<>(xAxis6, yAxis6);

    final NumberAxis xAxis7 = new NumberAxis();
    final NumberAxis yAxis7 = new NumberAxis();
    final LineChart<Number, Number> bestCostLineChart = new LineChart<>(xAxis7, yAxis7);

    final NumberAxis xAxis8 = new NumberAxis();
    final NumberAxis yAxis8 = new NumberAxis();
    final LineChart<Number, Number> bestDistanceLineChart = new LineChart<>(xAxis8, yAxis8);

    XYChart.Series<Number, Number> populationSeries;
    XYChart.Series<Number, Number> maxFitnessSeries;
    XYChart.Series<Number, Number> avgSeries;
    XYChart.Series<Number, Number> stdSeries;
    XYChart.Series<Number, Number> bestSeries;
    XYChart.Series<Number, Number> worstSeries;
    XYChart.Series<Number, Number> paretoFrontSeries;
    XYChart.Series<Number, Number> bestCostSeries;
    XYChart.Series<Number, Number> bestDistanceSeries;

    private TextArea consoleTextArea;

    // Fields needed to calculate FPS
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;
    private P5Main p5MainClass;

    private TextField adultSizeTextField;
    private TextField tournamentGroupSize;
    private TextField tournamentEpsilon;
    private Label tournamentGroupSizeLabel;
    private Label tournamentEpsilonLabel;

    private BorderPane mainPane;

    public Pane generateGUI(P5Main p5Main) {
        p5MainClass = p5Main;
        mainPane = new BorderPane();

        GridPane gridPane = getCharts();
        mainPane.setRight(gridPane);

        VBox controlPanelVBox = getControlPanelVBox();
        mainPane.setLeft(controlPanelVBox);

        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);

        mainPane.setBottom(consoleTextArea);

        updateProblemSpesificGUIElementVisibilities();

        return mainPane;
    }

    private VBox getControlPanelVBox() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(200);


        generateProblemSpesificGUI(vBox);

        Label EAVariablesLAbel = addLabel(vBox, "General Variables");
        EAVariablesLAbel.setFont(new Font(15));
        addLabel(vBox, "Population Size");
        TextField populationSizeTextField = new TextField();
        populationSizeTextField.setText(String.valueOf(Values.POPULATION_SIZE));
        populationSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int newProblemSize;
            try{
                newProblemSize = Integer.parseInt(newValue);
            }catch (Exception E){
                newProblemSize = Integer.parseInt(oldValue);
            }
            Values.POPULATION_SIZE = newProblemSize;
            Values.MAX_ADULT_SIZE = newProblemSize / 2;
            Values.NUMBER_OF_ELITES = (int) (Values.POPULATION_SIZE * 0.05);
            Values.MAX_PARENT_SIZE = Values.POPULATION_SIZE / 2;
            adultSizeTextField.setText(String.valueOf(newProblemSize/2));
        });
        vBox.getChildren().add(populationSizeTextField);

        addLabel(vBox, "Adult Size");
        adultSizeTextField = new TextField();
        adultSizeTextField.setText(String.valueOf(Values.MAX_ADULT_SIZE));
        adultSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int newProblemSize;
            try{
                newProblemSize = Integer.parseInt(newValue);
            }catch (Exception E){
                newProblemSize = Integer.parseInt(oldValue);
            }
            Values.MAX_ADULT_SIZE = newProblemSize;
        });
        vBox.getChildren().add(adultSizeTextField);

        addLabel(vBox, "Crossover propability");
        TextField crossoverTextField = new TextField();
        crossoverTextField.setText(String.valueOf(Values.CROSSOVER_PROBABILITY));
        crossoverTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            double newProblemSize;
            try{
                newProblemSize = Double.parseDouble(newValue);
            }catch (Exception E){
                newProblemSize = Double.parseDouble(oldValue);
            }
            Values.CROSSOVER_PROBABILITY = newProblemSize;
        });
        vBox.getChildren().add(crossoverTextField);

        addLabel(vBox, "Mutation propability");
        TextField mutationTextField = new TextField();
        mutationTextField.setText(String.valueOf(Values.MUTATION_PROBABILITY));
        mutationTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            double newProblemSize;
            try{
                newProblemSize = Double.parseDouble(newValue);
            }catch (Exception E){
                newProblemSize = Double.parseDouble(oldValue);
            }
            Values.MUTATION_PROBABILITY = newProblemSize;
        });
        vBox.getChildren().add(mutationTextField);





        addLabel(vBox, "Adult selection");
        ComboBox<EAdultSelection> adultSelectionComboBox = new ComboBox<>();
        adultSelectionComboBox.setValue(Values.ADULT_SELECTION);
        adultSelectionComboBox.getItems().setAll(EAdultSelection.values());
        adultSelectionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Values.ADULT_SELECTION = newValue;
        });
        vBox.getChildren().add(adultSelectionComboBox);

        addLabel(vBox, "Parent selection");
        ComboBox<EParentSelection> parentSelectionComboBox = new ComboBox<>();
        parentSelectionComboBox.setValue(Values.PARENT_SELECTION);
        parentSelectionComboBox.getItems().setAll(EParentSelection.values());
        parentSelectionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Values.PARENT_SELECTION = newValue;
            updateProblemSpesificGUIElementVisibilities();
        });
        vBox.getChildren().add(parentSelectionComboBox);

        tournamentGroupSizeLabel = addLabel(vBox, "Tournament group size");
        tournamentGroupSize = new TextField();
        tournamentGroupSize.setText(String.valueOf(Values.TOURNAMENT_SELECTION_GROUP_SIZE));
        tournamentGroupSize.textProperty().addListener((observable, oldValue, newValue) -> {
            int newProblemSize;
            try{
                newProblemSize = Integer.parseInt(newValue);
            }catch (Exception E){
                newProblemSize = Integer.parseInt(oldValue);
            }
            Values.TOURNAMENT_SELECTION_GROUP_SIZE = newProblemSize;
        });
        vBox.getChildren().add(tournamentGroupSize);

        tournamentEpsilonLabel = addLabel(vBox, "Torunament epsilon");
        tournamentEpsilon = new TextField();
        tournamentEpsilon.setText(String.valueOf(Values.TOURNAMENT_SELECTION_EPSILON));
        tournamentEpsilon.textProperty().addListener((observable, oldValue, newValue) -> {
            double newProblemSize;
            try{
                newProblemSize = Double.parseDouble(newValue);
            }catch (Exception E){
                newProblemSize = Double.parseDouble(oldValue);
            }
            Values.TOURNAMENT_SELECTION_EPSILON = newProblemSize;
        });
        vBox.getChildren().add(tournamentEpsilon);


        addLabel(vBox, "Print status every N generation:");
        TextField moduloChanger = new TextField();
        moduloChanger.setText(String.valueOf(Values.GENERATION_PRINT_THROTTLE));
        moduloChanger.textProperty().addListener((observable, oldValue, newValue) -> {
            int newProblemSize;
            try{
                newProblemSize = Integer.parseInt(newValue);
            }catch (Exception E){
                newProblemSize = Integer.parseInt(oldValue);
            }
            Values.GENERATION_PRINT_THROTTLE = newProblemSize;
        });
        vBox.getChildren().add(moduloChanger);

        CheckBox updateCharts = new CheckBox("Update charts");
        updateCharts.setOnAction(event -> {
            Values.UPDATE_CHARTS = updateCharts.isSelected();
        });
        updateCharts.setSelected(Values.UPDATE_CHARTS);
        vBox.getChildren().add(updateCharts);

        return vBox;
    }

    private void generateProblemSpesificGUI(VBox vBox) {
        HBox buttonHBox = new HBox();
        Button restartButton = new Button("New run");
        restartButton.setOnAction(event -> p5MainClass.restartAlgorithm());
        buttonHBox.getChildren().add(restartButton);

        Button pauseButton = getPauseSimulationButton();
        buttonHBox.getChildren().add(pauseButton);





        vBox.getChildren().add(buttonHBox);



    }

    private void updateProblemSpesificGUIElementVisibilities() {

    }

    private Label addLabel(VBox vBox, String s) {
        Label label = new Label(s);
        vBox.getChildren().add(label);
        return label;
    }

    private GridPane getCharts() {
        GridPane gridPane = new GridPane();

        populationFitnessLineChart.setTitle("Population fitness");
        populationFitnessLineChart.setLegendVisible(false);
        populationFitnessLineChart.setPrefSize(500, 300);
        populationFitnessLineChart.setCreateSymbols(false);
        populationSeries = new XYChart.Series<>();
        populationFitnessLineChart.setAnimated(false);

        gridPane.add(populationFitnessLineChart, 0, 0);

        maxFitnessLineChart.setTitle("Best fitness");
        maxFitnessLineChart.setPrefSize(500, 300);
        maxFitnessLineChart.setAnimated(false);
        maxFitnessLineChart.setLegendVisible(false);
        maxFitnessLineChart.setCreateSymbols(false);

        maxFitnessSeries = new XYChart.Series<>();
        maxFitnessLineChart.getData().add(maxFitnessSeries);

        //gridPane.add(maxFitnessLineChart, 1, 0);

        avarageFitnessLineChart.setTitle("Avarage fitness");
        avarageFitnessLineChart.setPrefSize(500, 300);
        avarageFitnessLineChart.setAnimated(false);
        avarageFitnessLineChart.setLegendVisible(false);
        avarageFitnessLineChart.setCreateSymbols(false);

        avgSeries = new XYChart.Series<>();
        avarageFitnessLineChart.getData().add(avgSeries);

        gridPane.add(avarageFitnessLineChart, 0, 1);


        stdFitnessLineChart.setTitle("Standard deviation fitness");
        stdFitnessLineChart.setPrefSize(500, 300);
        stdFitnessLineChart.setAnimated(false);
        stdFitnessLineChart.setLegendVisible(false);
        stdFitnessLineChart.setCreateSymbols(false);
        stdSeries = new XYChart.Series<>();
        stdFitnessLineChart.getData().add(stdSeries);

//        gridPane.add(stdFitnessLineChart, 1, 1);

        bestCostLineChart.setTitle("Best cost");
        bestCostLineChart.setPrefSize(500, 300);
        bestCostLineChart.setAnimated(false);
        bestCostLineChart.setLegendVisible(false);
        bestCostLineChart.setCreateSymbols(false);
        bestCostSeries = new XYChart.Series<>();
        bestCostLineChart.getData().add(bestCostSeries);

        gridPane.add(bestCostLineChart, 1, 0);

        bestDistanceLineChart.setTitle("Best distance");
        bestDistanceLineChart.setPrefSize(500, 300);
        bestDistanceLineChart.setAnimated(false);
        bestDistanceLineChart.setLegendVisible(false);
        bestDistanceLineChart.setCreateSymbols(false);
        bestDistanceSeries = new XYChart.Series<>();
        bestDistanceLineChart.getData().add(bestDistanceSeries);

        gridPane.add(bestDistanceLineChart, 1, 1);

        bestWorstScatter.setTitle("Best and worst rank");
        bestWorstScatter.setPrefSize(500, 300);
        bestWorstScatter.setAnimated(false);
        bestWorstScatter.setStyle("-fx-padding: 1px;");
        bestSeries = new XYChart.Series<>();
        bestSeries.setName("Best");
        bestSeries.getData().add(new XYChart.Data<>(0, 0));
        worstSeries = new XYChart.Series<>();
        worstSeries.setName("Worst");
        worstSeries.getData().add(new XYChart.Data<>(0, 0));
        bestWorstScatter.getData().add(bestSeries);
        bestWorstScatter.getData().add(worstSeries);
        xAxis5.setLabel("Cost");
        yAxis5.setLabel("Distance");

        gridPane.add(bestWorstScatter, 2, 0);

        paretoFrontLineChart.setTitle("Pareto-front");
        paretoFrontLineChart.setPrefSize(500, 300);
        paretoFrontLineChart.setAnimated(false);
        paretoFrontLineChart.setLegendVisible(false);
        paretoFrontLineChart.setCreateSymbols(false);
        paretoFrontSeries = new XYChart.Series<>();
        paretoFrontLineChart.getData().add(paretoFrontSeries);

        gridPane.add(paretoFrontLineChart, 2, 1);


        return gridPane;
    }

    void updateLineCharts(List<MTSPHypothesis> population, double maxFitness, double avgFitness, double stdFitness, int generation, String phenoTypeString, MTSPHypothesis bestHypothesis, MTSPHypothesis worstHypothesis) {
        //noinspection unchecked
        if (Values.UPDATE_CHARTS){
            populationFitnessLineChart.getData().retainAll();

            populationSeries = new XYChart.Series<>();
            for (int i = 0; i < population.size(); i++) {
                populationSeries.getData().add(new XYChart.Data<>(i, (population.get(i)).getFitness()));
            }
            populationFitnessLineChart.getData().add(populationSeries);

            maxFitnessSeries.getData().add(new XYChart.Data<>(generation, maxFitness));
            avgSeries.getData().add(new XYChart.Data<>(generation, avgFitness));
            stdSeries.getData().add(new XYChart.Data<>(generation, stdFitness));

            bestSeries.getData().add(new XYChart.Data<>(bestHypothesis.getDistanceFitness(), bestHypothesis.getCostFitness()));
            worstSeries.getData().add(new XYChart.Data<>(worstHypothesis.getDistanceFitness(), bestHypothesis.getCostFitness()));

            bestCostSeries.getData().add(new XYChart.Data<>(generation, bestHypothesis.getCostFitness()));
            bestDistanceSeries.getData().add(new XYChart.Data<>(generation, bestHypothesis.getDistanceFitness()));
        }

        consoleTextArea.appendText("Gen.:  " + String.format("%03d", generation) +
                " \tBest cost:     " + String.format("%4.3f", (double) bestHypothesis.getCostFitness()) +
                " \tBest distance:     " + String.format("%4.3f", (double) bestHypothesis.getDistanceFitness()) +
                " \tBest fitness:  " + String.format("%4.3f", maxFitness) +
                " \tAvg fitness:   " + String.format("%4.3f", avgFitness) +
//                " \tStandard deviation:  " + String.format("%.3f", stdFitness) +
                " \tPhenotype:  " + phenoTypeString +
                "\n");/**/

//        System.out.println("GENERATION " + generation);
//        Values.CTRNN.printWeights();
//        System.out.println("");
    }

    void appendTextToConsole(String s) {
        consoleTextArea.appendText(s);
    }

    private String getFPSString(long now) {
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = now;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }
        if (arrayFilled) {
            long elapsedNanos = now - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            String fpsString = String.format("Generations created per second: %.0f", frameRate);
            return fpsString;
        }
        return "";
    }

    private Button getPauseSimulationButton() {
        Button restartButton = new Button("Pause simulation");
        restartButton.setOnAction(event -> {
            if (p5MainClass.simulationPaused) {
                restartButton.setText("Pause simulation");
                p5MainClass.simulationPaused = false;
            } else {
                p5MainClass.simulationPaused = true;
                restartButton.setText("Start simulation");
            }
        });
        return restartButton;
    }



    public void clearGUI() {

        xAxis.setUpperBound(Values.POPULATION_SIZE + Values.NUMBER_OF_ELITES - 1);
        stdFitnessLineChart.getData().retainAll();
        stdSeries = new XYChart.Series<>();
        stdFitnessLineChart.getData().add(stdSeries);

        maxFitnessLineChart.getData().retainAll();
        maxFitnessSeries = new XYChart.Series<>();
        maxFitnessLineChart.getData().add(maxFitnessSeries);

        avarageFitnessLineChart.getData().retainAll();
        avgSeries = new XYChart.Series<>();
        avarageFitnessLineChart.getData().add(avgSeries);

        consoleTextArea.appendText("\n");
        consoleTextArea.appendText("\n");
        consoleTextArea.appendText("### RESTART ###");
        consoleTextArea.appendText("\n");
        consoleTextArea.appendText("\n");
    }
}
