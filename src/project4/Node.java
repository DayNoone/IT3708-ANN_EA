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

    public Node(ENodeType type) {
        this.type = type;
        this.gain = 0;
        this.timeConstant = 0;
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
        return String.valueOf(this.type) + " " + super.toString();
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

    public double getyValue() {
        return yValue;
    }

    public void setyValue(double yValue) {
        this.yValue = yValue;
    }

    public double getOldOutputValue() {
        return oldOutputValue;
    }
}
