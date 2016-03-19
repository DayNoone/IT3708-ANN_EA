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


    abstract void generatePhenotype();

    abstract void initiateRandomGenotype();

    abstract void calculateFitness();

    abstract AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype);

    abstract AbstractHypothesis instantiateNewChild();

    abstract void mutate();

    String getPhenotypeString() {
        String phenoTypeString = "";
        for (double aPhenotype : this.phenotype) {
            phenoTypeString += String.valueOf(aPhenotype);
        }
        return phenoTypeString;
    }

    boolean checkIfSolution() {
        return getFitness() >= 1.0;
    }

    double getFitness() {
        return this.fitness;
    }

    void setFitness(double fitness) {
        this.fitness = fitness;
    }


    double getExpectedValue() {
        return this.exceptedValue;
    }

    void setExpectedValue(double i) {
        this.exceptedValue = i;
    }


    int[] getGenotype() {
        return genotype;
    }

    void setGenotype(int[] genotype) {
        this.genotype = genotype;
    }

    double[] getPhenotype() {

        return phenotype;
    }

}
