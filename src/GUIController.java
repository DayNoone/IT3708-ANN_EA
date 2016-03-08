import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import project2.enums.EAdultSelection;
import project2.enums.EParentSelection;
import project2.enums.EPoblemSelection;
import project2.enums.ESurprisingSequenceMode;

import java.util.List;

/**
 * Created by markus on 21.02.2016.
 */
@SuppressWarnings("unchecked")
public class GUIController {

    NumberAxis xAxis = new NumberAxis(0, Values.POPULATION_SIZE + Values.NUMBER_OF_ELITES - 1, 2);
    NumberAxis yAxis = new NumberAxis(0, 1, 0.1);

    //    final NumberAxis yAxis = new NumberAxis();
//    LineChart<Number, Number> populationFitnessLineChart = new LineChart<>(new NumberAxis(), new NumberAxis());
    LineChart<Number, Number> populationFitnessLineChart = new LineChart<>(xAxis, yAxis);
    final NumberAxis xAxis2 = new NumberAxis();
    final NumberAxis yAxis2 = new NumberAxis(0, 1, 0.1);

    //    final NumberAxis yAxis2 = new NumberAxis();
    final LineChart<Number, Number> maxFitnessLineChart = new LineChart<>(xAxis2, yAxis2);
    final NumberAxis xAxis3 = new NumberAxis();
    final NumberAxis yAxis3 = new NumberAxis(0, 1, 0.1);

    //    final NumberAxis yAxis3 = new NumberAxis();
    final LineChart<Number, Number> avarageFitnessLineChart = new LineChart<>(xAxis3, yAxis3);
    final NumberAxis xAxis4 = new NumberAxis();
    final NumberAxis yAxis4 = new NumberAxis();
    final LineChart<Number, Number> stdFitnessLineChart = new LineChart<>(xAxis4, yAxis4);

    //Population fitness linechart
    XYChart.Series<Number, Number> populationSeries;

    //Max fitness linechart
    XYChart.Series<Number, Number> maxFitnessSeries;

    //Avarage fitness linechart
    XYChart.Series<Number, Number> avgSeries;

    //Standard deviating fitness linechart
    XYChart.Series<Number, Number> stdSeries;
    private TextArea consoleTextArea;

    // Fields needed to calculate FPS
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;
    private Main mainClass;
    private Label lolzThresLabel;
    private TextField lolzThresTextField;
    private Label surpriseLabel;
    private TextField surpriseTextField;
    private HBox surpriseHBox;
    private TextField adultSizeTextField;
    private TextField tournamentGroupSize;
    private TextField tournamentEpsilon;
    private Label tournamentGroupSizeLabel;
    private Label tournamentEpsilonLabel;

    public Pane generateGUI(Main main) {
        mainClass = main;
        BorderPane mainPane = new BorderPane();

        GridPane gridPane = getChartsAndConsoleGridPane();
        mainPane.setRight(gridPane);

        VBox controlPanelVBox = getControlPanelVBox();
        mainPane.setLeft(controlPanelVBox);

        updateVisibilities();

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
            updateVisibilities();
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
            if (updateCharts.isSelected()){
                Values.UPDATE_CHARTS = true;
            }else{
                Values.UPDATE_CHARTS = false;
            }

        });
        updateCharts.setSelected(Values.UPDATE_CHARTS);
        vBox.getChildren().add(updateCharts);

        return vBox;
    }

    private void generateProblemSpesificGUI(VBox vBox) {
        HBox buttonHBox = new HBox();
        Button restartButton = new Button("New run");
        restartButton.setOnAction(event -> mainClass.restartAlgorithm());
        buttonHBox.getChildren().add(restartButton);

        Button pauseButton = getPauseSimulationButton();
        buttonHBox.getChildren().add(pauseButton);





        vBox.getChildren().add(buttonHBox);

        Label problemLabel = addLabel(vBox, "Problem Variables");
        problemLabel.setFont(new Font(15));

        addLabel(vBox, "Select problem");
        ComboBox<EPoblemSelection> problemSelectionComboBox = new ComboBox<>();
        problemSelectionComboBox.setValue(Values.SELECTED_PROBLEM);
        problemSelectionComboBox.getItems().setAll(EPoblemSelection.values());
        problemSelectionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Values.SELECTED_PROBLEM = newValue;
            updateVisibilities();
        });
        vBox.getChildren().add(problemSelectionComboBox);

        addLabel(vBox, "Problem Size");
        TextField problemSizeTextField = new TextField();
        problemSizeTextField.setText(String.valueOf(Values.NUMBER_OF_BITS_IN_PROBLEM));
        problemSizeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int newProblemSize;
            try{
                newProblemSize = Integer.parseInt(newValue);
            }catch (Exception E){
                newProblemSize = Integer.parseInt(oldValue);
            }
            Values.NUMBER_OF_BITS_IN_PROBLEM = newProblemSize;
        });
        vBox.getChildren().add(problemSizeTextField);

        lolzThresLabel = addLabel(vBox, "Threshold");

        lolzThresTextField = new TextField();
        lolzThresTextField.setText(String.valueOf(Values.LOLZ_THRESHOLD));
        lolzThresTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int newInt;
            try{
                newInt = Integer.parseInt(newValue);
            }catch (Exception E){
                newInt = Integer.parseInt(oldValue);
            }
            Values.LOLZ_THRESHOLD = newInt;
        });
        vBox.getChildren().add(lolzThresTextField);


        surpriseLabel = addLabel(vBox, "Symbol size");

        surpriseTextField = new TextField();
        surpriseTextField.setText(String.valueOf(Values.SURPRISING_SYMBOL_SIZE));
        surpriseTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            int newInt;
            try{
                newInt = Integer.parseInt(newValue);
            }catch (Exception E){
                newInt = Integer.parseInt(oldValue);
            }
            Values.SURPRISING_SYMBOL_SIZE = newInt;
        });
        vBox.getChildren().add(surpriseTextField);

        surpriseHBox = new HBox();
        final ToggleGroup group = new ToggleGroup();

        RadioButton localRadioButton = new RadioButton("Local");
        localRadioButton.setToggleGroup(group);
        localRadioButton.setUserData(ESurprisingSequenceMode.Local);

        RadioButton globalRadioButton = new RadioButton("Global");
        globalRadioButton.setToggleGroup(group);
        globalRadioButton.setUserData(ESurprisingSequenceMode.Global);
        if (Values.SURPRISING_MODE == ESurprisingSequenceMode.Global){
            globalRadioButton.setSelected(true);
        }else {
            localRadioButton.setSelected(true);
        }

        group.selectedToggleProperty().addListener((observableValue, old_toggle, new_toggle) -> {
            if (group.getSelectedToggle() != null) {
                if (group.getSelectedToggle().getUserData().equals(ESurprisingSequenceMode.Local)){
                    Values.SURPRISING_MODE = ESurprisingSequenceMode.Local;
                }else if (group.getSelectedToggle().getUserData().equals(ESurprisingSequenceMode.Global)) {
                    Values.SURPRISING_MODE = ESurprisingSequenceMode.Global;
                }
            }
        });
        surpriseHBox.getChildren().add(localRadioButton);
        surpriseHBox.getChildren().add(globalRadioButton);
        vBox.getChildren().add(surpriseHBox);

    }

    private void updateVisibilities() {
        if (Values.SELECTED_PROBLEM == EPoblemSelection.LOLZ){
            lolzThresLabel.setVisible(true);
            lolzThresTextField.setVisible(true);
        }else{
            lolzThresLabel.setVisible(false);
            lolzThresTextField.setVisible(false);
        }
        if (Values.SELECTED_PROBLEM == EPoblemSelection.SURPRISING_SEQUENCES){
            surpriseLabel.setVisible(true);
            surpriseTextField.setVisible(true);
            surpriseHBox.setVisible(true);
        }
        else{
            surpriseLabel.setVisible(false);
            surpriseTextField.setVisible(false);
            surpriseHBox.setVisible(false);

        }
        if (Values.PARENT_SELECTION == EParentSelection.TOURNAMENT_SELECTION){
            tournamentEpsilon.setVisible(true);
            tournamentEpsilonLabel.setVisible(true);
            tournamentGroupSize.setVisible(true);
            tournamentGroupSizeLabel.setVisible(true);
        }
        else{
            tournamentEpsilon.setVisible(false);
            tournamentEpsilonLabel.setVisible(false);
            tournamentGroupSize.setVisible(false);
            tournamentGroupSizeLabel.setVisible(false);
        }
    }

    private Label addLabel(VBox vBox, String s) {
        Label label = new Label(s);
        vBox.getChildren().add(label);
        return label;
    }

    private GridPane getChartsAndConsoleGridPane() {
        GridPane gridPane = new GridPane();

        populationFitnessLineChart.setTitle("Population fitness");
        populationFitnessLineChart.setLegendVisible(false);
        populationFitnessLineChart.setPrefSize(600, 300);
        populationFitnessLineChart.setCreateSymbols(false);
        populationSeries = new XYChart.Series<>();
        populationFitnessLineChart.setAnimated(false);

        gridPane.add(populationFitnessLineChart, 0, 0);

        maxFitnessLineChart.setTitle("Best fitness");
        maxFitnessLineChart.setPrefSize(600, 300);
        maxFitnessLineChart.setAnimated(false);
        maxFitnessLineChart.setLegendVisible(false);
        maxFitnessLineChart.setCreateSymbols(false);

        maxFitnessSeries = new XYChart.Series<>();
        maxFitnessLineChart.getData().add(maxFitnessSeries);

        gridPane.add(maxFitnessLineChart, 1, 0);

        avarageFitnessLineChart.setTitle("Avarage fitness");
        avarageFitnessLineChart.setPrefSize(600, 300);
        avarageFitnessLineChart.setAnimated(false);
        avarageFitnessLineChart.setLegendVisible(false);
        avarageFitnessLineChart.setCreateSymbols(false);

        avgSeries = new XYChart.Series<>();
        avarageFitnessLineChart.getData().add(avgSeries);

        gridPane.add(avarageFitnessLineChart, 0, 1);


        stdFitnessLineChart.setTitle("Standard deviation fitness");
        stdFitnessLineChart.setPrefSize(600, 300);
        stdFitnessLineChart.setAnimated(false);
        stdFitnessLineChart.setLegendVisible(false);
        stdFitnessLineChart.setCreateSymbols(false);
        stdSeries = new XYChart.Series<>();
        stdFitnessLineChart.getData().add(stdSeries);

        gridPane.add(stdFitnessLineChart, 1, 1);


        consoleTextArea = new TextArea();
        consoleTextArea.setEditable(false);
        consoleTextArea.setPrefSize(1200, 200);

        gridPane.add(consoleTextArea, 0, 2, 2, 1);
        return gridPane;
    }

    public void updateLineCharts(List<AbstractHypothesis> population, double maxFitness, double avgFitness, double stdFitness, int generation, String phenoTypeString) {
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
        }

        consoleTextArea.appendText("Gen.:  " + String.format("%03d", generation) +
                " \tBest fitness:  " + String.format("%4.3f", maxFitness) +
                " \tAvg fitness:   " + String.format("%4.3f", avgFitness) +
                " \tStandard deviation:  " + String.format("%.3f", stdFitness) +
                " \tPhenotype:  " + phenoTypeString +
                "\n");/**/

    }

    public void appendTextToConsole(String s) {
        consoleTextArea.appendText(s);
    }

    public void updateFPS(long now, Stage primaryStage) {
        String fpsString = getFPSString(now);
        primaryStage.setTitle(fpsString);
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
            if (mainClass.simualtionPaused) {
                restartButton.setText("Pause simulation");
                mainClass.simualtionPaused = false;
            } else {
                mainClass.simualtionPaused = true;
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
