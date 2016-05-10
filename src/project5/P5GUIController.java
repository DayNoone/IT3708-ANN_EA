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

    private final NumberAxis xAxis5 = new NumberAxis(0, 175000, 25000);
    private final NumberAxis yAxis5 = new NumberAxis(0, 2000, 250);
    private final ScatterChart<Number, Number> bestWorstScatter = new ScatterChart<Number, Number>(xAxis5, yAxis5);

    private final NumberAxis xAxis6 = new NumberAxis(0, 175000, 25000);
    private final NumberAxis yAxis6 = new NumberAxis(0, 2000, 250);
    private final ScatterChart<Number, Number> paretoFrontLineChart = new ScatterChart<Number, Number>(xAxis6, yAxis6);

    private final NumberAxis xAxis7 = new NumberAxis();
    private final NumberAxis yAxis7 = new NumberAxis();
    private final LineChart<Number, Number> bestCostLineChart = new LineChart<>(xAxis7, yAxis7);

    private final NumberAxis xAxis8 = new NumberAxis();
    private final NumberAxis yAxis8 = new NumberAxis();
    private final LineChart<Number, Number> bestDistanceLineChart = new LineChart<>(xAxis8, yAxis8);

    private XYChart.Series<Number, Number> populationSeries;
    private XYChart.Series<Number, Number> worstSeries;
    private XYChart.Series<Number, Number> bestSeries;
    private XYChart.Series<Number, Number> bestCostSeries;
    private XYChart.Series<Number, Number> bestDistanceSeries;
    private XYChart.Series<Number, Number> paretoFrontSeries;
    private XYChart.Series<Number, Number> bestParetoFrontSeries;
    private XYChart.Series<Number, Number> worstParetoFrontSeries;

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
        restartButton.setOnAction(event -> {
            if(Values.MTSP_MULTIPLE_PARETO_LINES_PLOT){
                paretoFrontSeries = new XYChart.Series<>();
                paretoFrontLineChart.getData().add(paretoFrontSeries);
            }
            p5MainClass.restartAlgorithm();
        });
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

        worstSeries = new XYChart.Series<>();
        worstSeries.setName("Worst");
        worstSeries.getData().add(new XYChart.Data<>(0, 0));

        bestSeries = new XYChart.Series<>();
        bestSeries.setName("Best");
        bestSeries.getData().add(new XYChart.Data<>(0, 0));

        populationSeries = new XYChart.Series<>();
        populationSeries.setName("Population");
        populationSeries.getData().add(new XYChart.Data<>(0, 0));

        bestWorstScatter.getData().add(populationSeries);
        bestWorstScatter.getData().add(worstSeries);
        bestWorstScatter.getData().add(bestSeries);

        xAxis5.setLabel("Distance");
        yAxis5.setLabel("Cost");

        gridPane.add(bestWorstScatter, 2, 0);

        paretoFrontLineChart.setTitle("Pareto-front");
        paretoFrontLineChart.setPrefSize(500, 300);
        paretoFrontLineChart.setAnimated(false);
        paretoFrontLineChart.setLegendVisible(false);
        paretoFrontSeries = new XYChart.Series<>();

        bestParetoFrontSeries = new XYChart.Series<>();
        bestParetoFrontSeries.setName("Best");
        bestParetoFrontSeries.getData().add(new XYChart.Data<>(0, 0));

        worstParetoFrontSeries = new XYChart.Series<>();
        worstParetoFrontSeries.setName("Best");
        worstParetoFrontSeries.getData().add(new XYChart.Data<>(0, 0));

        paretoFrontLineChart.getData().add(paretoFrontSeries);
        paretoFrontLineChart.getData().add(worstParetoFrontSeries);
        paretoFrontLineChart.getData().add(bestParetoFrontSeries);



        gridPane.add(paretoFrontLineChart, 2, 1);


        return gridPane;
    }

    void updateLineCharts(int generation, String phenoTypeString, MTSPHypothesis bestHypothesis, MTSPHypothesis worstHypothesis, List<MTSPHypothesis> population, MTSPHypothesis bestNonInfiniteHypothesis) {
        //noinspection unchecked
        if (Values.UPDATE_CHARTS){
            populationSeries.getData().retainAll();
            worstSeries.getData().retainAll();
            bestSeries.getData().retainAll();
            paretoFrontSeries.getData().retainAll();
            worstParetoFrontSeries.getData().retainAll();
            bestParetoFrontSeries.getData().retainAll();


            //worstSeries.getData().add(new XYChart.Data<>(bestHypothesis.getDistanceFitness(), bestHypothesis.getCostFitness()));
            //bestSeries.getData().add(new XYChart.Data<>(worstHypothesis.getDistanceFitness(), bestHypothesis.getCostFitness()));

            bestCostSeries.getData().add(new XYChart.Data<>(generation, bestHypothesis.getCostFitness()));
            bestDistanceSeries.getData().add(new XYChart.Data<>(generation, bestHypothesis.getDistanceFitness()));


            MTSPHypothesis worstDistance = population.get(0);
            MTSPHypothesis bestDistance = population.get(0);
            MTSPHypothesis worstCost = population.get(0);
            MTSPHypothesis bestCost = population.get(0);

            MTSPHypothesis worstParetoDistance = population.get(0);
            MTSPHypothesis bestParetoDistance = population.get(0);
            MTSPHypothesis worstParetoCost = population.get(0);
            MTSPHypothesis bestParetoCost = population.get(0);

            for(MTSPHypothesis hypothesis: population){
                if(hypothesis.getRank() == 0){
                    if(hypothesis.getDistanceFitness() > worstParetoDistance.getDistanceFitness()) {
                        worstParetoDistance = hypothesis;
                    } else if (hypothesis.getDistanceFitness() < bestParetoDistance.getDistanceFitness()) {
                        bestParetoDistance = hypothesis;
                    } else if (hypothesis.getCostFitness() > worstParetoCost.getCostFitness()) {
                        worstParetoCost = hypothesis;
                    } else if (hypothesis.getCostFitness() < bestParetoCost.getCostFitness()) {
                        bestParetoCost = hypothesis;
                    } else {
                        paretoFrontSeries.getData().add(new XYChart.Data<>(hypothesis.getDistanceFitness(), hypothesis.getCostFitness()));
                    }

                }
                if(hypothesis.getDistanceFitness() > worstDistance.getDistanceFitness()){
                    worstDistance = hypothesis;
                } else if(hypothesis.getDistanceFitness() < bestDistance.getDistanceFitness()){
                    bestDistance = hypothesis;
                } else if(hypothesis.getCostFitness() > worstCost.getCostFitness()){
                    worstCost = hypothesis;
                } else if(hypothesis.getCostFitness() < bestCost.getCostFitness()){
                    bestCost = hypothesis;
                } else {
                    populationSeries.getData().add(new XYChart.Data<>(hypothesis.getDistanceFitness(), hypothesis.getCostFitness()));
                }

            }
            worstSeries.getData().add(new XYChart.Data<>(worstDistance.getDistanceFitness(), worstDistance.getCostFitness()));
            worstSeries.getData().add(new XYChart.Data<>(worstCost.getDistanceFitness(), worstCost.getCostFitness()));
            bestSeries.getData().add(new XYChart.Data<>(bestDistance.getDistanceFitness(), bestDistance.getCostFitness()));
            bestSeries.getData().add(new XYChart.Data<>(bestCost.getDistanceFitness(), bestCost.getCostFitness()));

            if(!Values.MTSP_MULTIPLE_PARETO_LINES_PLOT){
                worstParetoFrontSeries.getData().add(new XYChart.Data<>(worstParetoCost.getDistanceFitness(), worstParetoCost.getCostFitness()));
                worstParetoFrontSeries.getData().add(new XYChart.Data<>(worstParetoDistance.getDistanceFitness(), worstParetoDistance.getCostFitness()));
                bestParetoFrontSeries.getData().add(new XYChart.Data<>(bestParetoDistance.getDistanceFitness(), bestParetoDistance.getCostFitness()));
                bestParetoFrontSeries.getData().add(new XYChart.Data<>(bestParetoCost.getDistanceFitness(), bestParetoCost.getCostFitness()));
            }
        }

        consoleTextArea.appendText("Gen.:  " + String.format("%03d", generation) +
                " \tBest cost:  " + String.format("%4.0f", (double) bestHypothesis.getCostFitness()) +
                " \tBest distance:     " + String.format("%4.0f  ", (double) bestHypothesis.getDistanceFitness()) +
                " \tPhenotype:  " + phenoTypeString +
                "\n");/**/

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
        worstSeries.getData().retainAll();
        bestSeries.getData().retainAll();

        bestCostSeries.getData().retainAll();

        bestDistanceSeries.getData().retainAll();

        consoleTextArea.appendText("\n");
        consoleTextArea.appendText("\n");
        consoleTextArea.appendText("### RESTART ###");
        consoleTextArea.appendText("\n");
        consoleTextArea.appendText("\n");
    }
}
