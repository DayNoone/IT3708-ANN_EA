package project5;

import general.AbstractHypothesis;
import general.Values;

import java.util.*;

/**
 * Created by dagih on 29.04.2016.
 */
public class MTSPHypothesis extends AbstractHypothesis{


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

    @Override
    public void generatePhenotype() {
        this.phenotype = new double[this.genotype.length];
        for (int i = 0; i < this.genotype.length; i++) {
            this.phenotype[i] = (double)this.genotype[i];
        }
    }

    @Override
    public void initiateRandomGenotype() {
        ArrayList<Integer> pool = new ArrayList<Integer>();
        for(int i = 0; i < Values.MTSP_NUMBER_OF_CITIES; i++){
            pool.add(i);
        }
        int[] genotype = new int[48];
        for(int j = 0; j < Values.MTSP_NUMBER_OF_CITIES; j++){
            genotype[j] = pool.remove(random.nextInt(pool.size()));
        }
        setGenotype(genotype);
    }

    @Override
    public void calculateFitness() {

    }

    @Override
    public AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype) {

        return new MTSPHypothesis(genotype);
    }

    @Override
    public AbstractHypothesis instantiateNewChild() {

        return new MTSPHypothesis();
    }

    @Override
    public void mutate() {

    }

    @Override
    public List<AbstractHypothesis> crossover(AbstractHypothesis parent1, AbstractHypothesis parent2) {
        return null;
    }
}
