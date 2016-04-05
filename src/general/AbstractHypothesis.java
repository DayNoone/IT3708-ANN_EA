package general;

import java.util.Random;

/**
 * Created by markus on 18.02.2016.
 */
public abstract class AbstractHypothesis {

    protected Random random = new Random();
    protected int[] genotype;
    protected double[] phenotype;
    protected double fitness;
    protected double exceptedValue;


    public abstract void generatePhenotype();

    public abstract void initiateRandomGenotype();

    public abstract void calculateFitness();

    public abstract AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype);

    public abstract AbstractHypothesis instantiateNewChild();

    public abstract void mutate();

    public String getPhenotypeString() {
        String phenoTypeString = "";
        for (double aPhenotype : this.phenotype) {
            phenoTypeString += " " + String.valueOf(aPhenotype);
        }
        return phenoTypeString;
    }

    public boolean checkIfSolution() {
        return getFitness() >= 1.0;
    }

    public double getFitness() {
        return this.fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }


    public double getExpectedValue() {
        return this.exceptedValue;
    }

    public void setExpectedValue(double i) {
        this.exceptedValue = i;
    }


    public int[] getGenotype() {
        return genotype;
    }

    public void setGenotype(int[] genotype) {
        this.genotype = genotype;
    }

    public double[] getPhenotype() {

        return phenotype;
    }

}
