package project4;

import enums.ENodeType;

/**
 * Created by markus on 01.04.2016.
 */
public class Node {

    private ENodeType type;
    private double gain;
    private double timeConstant;

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
}
