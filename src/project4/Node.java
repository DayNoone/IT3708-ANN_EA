package project4;

import enums.ENodeType;

/**
 * Created by markus on 01.04.2016.
 */
public class Node {

    private ENodeType type;
    private double gain;
    private double timeConstant;
    private double outputValue;
    private double oldOutputValue;
    private double sValue;
    private double yValue;
    private int positionInLayer;

    private static int inputNodeCount = 0;
    private static int hiddenNodeCount = 0;
    private static int outputNodeCount = 0;

    private static int hiddenLayerCount = 0;

    public Node(ENodeType type) {
        this.type = type;
        switch (this.type){
            case HIDDEN:
                this.positionInLayer = hiddenNodeCount++;
                break;
            case INPUT:
                this.positionInLayer = inputNodeCount++;
                break;
            case OUTPUT:
                this.positionInLayer = outputNodeCount++;
                break;
        }
        this.gain = 1.0;
        this.timeConstant = 1.0;
        this.outputValue = 0;
        this.sValue = 0;
        this.yValue = 0;
    }

    public Node(ENodeType type, int hiddenLayerCount) {
        this(type);
        if (hiddenLayerCount > Node.hiddenLayerCount){
            hiddenNodeCount = 0;
        }
        Node.hiddenLayerCount = hiddenLayerCount;
    }

    public double getGain() {
        return gain;
    }

    public void setGain(double gain) {
        this.gain = gain;
    }

    public double getTimeConstant() {
        return timeConstant;
    }

    public void setTimeConstant(double timeConstant) {
        this.timeConstant = timeConstant;
    }

    public ENodeType getType() {
        return type;
    }

    @Override
    public String toString() {
        if (this.type == ENodeType.HIDDEN){
//            return String.valueOf(this.type) + " " + this.positionInLayer + " (" + hiddenLayerCount + ")";
            return String.valueOf(this.type) + " " + this.positionInLayer;


        }else{
            return String.valueOf(this.type) + " " + this.positionInLayer;
        }
    }

    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double newOutputValue) {
        this.oldOutputValue = this.outputValue;
        this.outputValue = newOutputValue;
    }

    public double getsValue() {
        return sValue;
    }

    public void setsValue(double sValue) {
        this.sValue = sValue;
    }

    public double getYValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public double getOldOutputValue() {
        return oldOutputValue;
    }

    public void resetValues() {
        this.gain = 1.0;
        this.timeConstant = 1.0;
        this.outputValue = 0;
        this.sValue = 0;
        this.yValue = 0;
    }
}
