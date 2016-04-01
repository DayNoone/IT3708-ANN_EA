package general;

import enums.BoardElement;
import general.Values;

import java.util.ArrayList;

/**
 * Created by markus on 17.03.2016.
 */
public class ANN {

    private double[] networkWeights;
    private int numberOfWeights;
    private int weightCounter = 0;

    private double[] inputLayerNodeValues;
    private double[][] hiddenLayers;
    private double[] outputLayerNodeValues;

    public ANN() {

        initializeAnn();
    }

    private void initializeAnn() {

        //Init topology
        inputLayerNodeValues = new double[Values.ANN_INPUT_NODES];
        hiddenLayers = new double[Values.ANN_NODES_IN_HIDDEN_LAYERS.length][0];

        for (int i = 0; i < hiddenLayers.length; i++) {
            hiddenLayers[i] = new double[Values.ANN_NODES_IN_HIDDEN_LAYERS[i]];
        }
        outputLayerNodeValues = new double[Values.ANN_OUTPUT_NODES];

        calcLengthOfGenotype();
    }

    public int getMove(ArrayList<BoardElement> inputValues){
        initiateInputLayer(inputValues);

        for (int hiddenLayerCounter = 0; hiddenLayerCounter < hiddenLayers.length; hiddenLayerCounter++) {
            if (hiddenLayerCounter == 0){
                updateLayerNodes(hiddenLayers[hiddenLayerCounter], inputLayerNodeValues);
            }
            //Rest of hidden layers
            else{
                updateLayerNodes(hiddenLayers[hiddenLayerCounter], hiddenLayers[hiddenLayerCounter-1]);
            }
        }
        updateLayerNodes(outputLayerNodeValues, hiddenLayers[hiddenLayers.length-1]);


        int highestIndex = findHighestIndex(outputLayerNodeValues);
        return highestIndex;
    }

    private int findHighestIndex(double[] outputLayer) {
        int highestIndex = 0;
        double highestValue = outputLayer[0];
        for (int i = 1; i < outputLayer.length; i++) {
            if (outputLayer[i] > highestValue) {
                highestIndex = i;
                highestValue = outputLayer[i];
            }
        }
        return highestIndex;
    }

    private void updateLayerNodes(double[] currentLayer, double[] previousLayer){
        for (int currentLayerNodeCounter = 0; currentLayerNodeCounter < currentLayer.length; currentLayerNodeCounter++) {

            double totalValue = 0;
            for (int previousLayerNodeCounter = 0; previousLayerNodeCounter < previousLayer.length; previousLayerNodeCounter++) {
                totalValue += previousLayer[previousLayerNodeCounter] * networkWeights[weightCounter++];
            }

            currentLayer[currentLayerNodeCounter] = activationFunction(totalValue);
        }
        weightCounter = 0;
    }

    private double activationFunction(double totalValue) {
//        if (totalValue > 1){
//            totalValue = 1;
//        }
        return Math.max(0, totalValue);
    }

    private void initiateInputLayer(ArrayList<BoardElement> inputValues) {
        for (int i = 0; i < inputValues.size(); i++) {
            BoardElement be = inputValues.get(i);
            if (be.equals(BoardElement.EMPTY)) {
                inputLayerNodeValues[i] = 0;
            } else {
                inputLayerNodeValues[i] = 1;
            }
        }
    }


    public void setNetworkWeights(double[] networkWeights) {
        this.networkWeights = new double[networkWeights.length];
        System.arraycopy(networkWeights, 0, this.networkWeights, 0, networkWeights.length);
    }

    public int getNumberOfWeights() {
        return numberOfWeights;
    }

    public void setNumberOfWeights(int numberOfWeights) {
        this.numberOfWeights = numberOfWeights;
    }


    private void calcLengthOfGenotype(){
        int numWeights = 0;
        numWeights += Values.ANN_INPUT_NODES * Values.ANN_NODES_IN_HIDDEN_LAYERS[0];
        for (int i = 1; i < Values.ANN_NODES_IN_HIDDEN_LAYERS.length; i++) {
            numWeights += Values.ANN_NODES_IN_HIDDEN_LAYERS[i-1] * Values.ANN_NODES_IN_HIDDEN_LAYERS[i];
        }
        numWeights += Values.ANN_NODES_IN_HIDDEN_LAYERS[Values.ANN_NODES_IN_HIDDEN_LAYERS.length - 1] * Values.ANN_OUTPUT_NODES;
        setNumberOfWeights(numWeights);
    }
}
