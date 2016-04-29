package general;

import java.util.ArrayList;
import java.util.List;
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

    public List<AbstractHypothesis> crossover(AbstractHypothesis parent1, AbstractHypothesis parent2) {
        List<AbstractHypothesis> children = new ArrayList<>();

        int crossOverPoint = random.nextInt(parent1.getGenotype().length);

        int[] newGenotype1 = new int[parent1.getGenotype().length];
        int[] newGenotype2 = new int[parent1.getGenotype().length];
        for (int i = 0; i < parent1.getGenotype().length; i++) {
            if (crossOverPoint <= i) {
                newGenotype1[i] = parent1.getGenotype()[i];
                newGenotype2[i] = parent2.getGenotype()[i];
            } else {
                newGenotype1[i] = parent2.getGenotype()[i];
                newGenotype2[i] = parent1.getGenotype()[i];
            }
        }
        AbstractHypothesis child1 = parent1.instantiateNewChileWithGenoType(newGenotype1);
        AbstractHypothesis child2 = parent2.instantiateNewChileWithGenoType(newGenotype2);
        children.add(child1);
        children.add(child2);
        return children;
    }

}
