package project5;

import general.AbstractHypothesis;
import general.Values;

import java.util.*;

/**
 * Created by dagih on 29.04.2016.
 */
public class MTSPHypothesis{

    private int distanceFitness;

    private int costFitness;

    private int rank;

    protected Random random = new Random();
    protected int[] genotype;
    protected double[] phenotype;
    protected double exceptedValue;

    public MTSPHypothesis() {

        int[] genotype = new int[Values.MTSP_NUMBER_OF_CITIES];
        this.setGenotype(genotype);
        initiateRandomGenotype();
        generatePhenotype();

    }

    public MTSPHypothesis(int[] newGenotype) {
        this.genotype = newGenotype;
        generatePhenotype();
    }

    public void generatePhenotype() {
        this.phenotype = new double[this.genotype.length];
        for (int i = 0; i < this.genotype.length; i++) {
            this.phenotype[i] = (double)this.genotype[i];
        }
    }

    public void initiateRandomGenotype() {
        ArrayList<Integer> pool = new ArrayList<>();
        for(int i = 0; i < Values.MTSP_NUMBER_OF_CITIES; i++){
            pool.add(i);
        }
        int[] genotype = new int[Values.MTSP_NUMBER_OF_CITIES];
        for(int j = 0; j < Values.MTSP_NUMBER_OF_CITIES; j++){
            genotype[j] = pool.remove(random.nextInt(pool.size()));
        }
        setGenotype(genotype);
    }

    public void calculateFitness() {

    }

    public AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype) {

        return new MTSPHypothesis(genotype);
    }

    public AbstractHypothesis instantiateNewChild() {

        return new MTSPHypothesis();
    }

    public void mutate() {

    }

    public List<AbstractHypothesis> crossover(AbstractHypothesis parent1, AbstractHypothesis parent2) {
        return null;
    }

    public int getCostFitness() {
        return costFitness;
    }

    public int getDistanceFitness() {
        return distanceFitness;
    }

    public int getRank() {
            return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setDistanceFitness(int distanceFitness) {
        this.distanceFitness = distanceFitness;
    }

    public void setCostFitness(int costFitness) {
        this.costFitness = costFitness;
    }

    public boolean dominates(MTSPHypothesis hyp) {
        if (this.getCostFitness() > hyp.getCostFitness() || this.getDistanceFitness() > hyp.getDistanceFitness()){
            return false;
        }
        else
            return this.getCostFitness() < hyp.getCostFitness() || this.getDistanceFitness() < hyp.getDistanceFitness();

    }
}
