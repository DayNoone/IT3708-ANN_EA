package project5;

import general.AbstractHypothesis;
import general.Values;

import java.util.*;

/**
 * Created by dagih on 29.04.2016.
 */
public class MTSPHypothesis extends AbstractHypothesis{
    @Override
    public void generatePhenotype() {

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
        return null;
    }

    @Override
    public AbstractHypothesis instantiateNewChild() {
        return null;
    }

    @Override
    public void mutate() {

    }

    @Override
    public List<AbstractHypothesis> crossover(AbstractHypothesis parent1, AbstractHypothesis parent2) {
        return null;
    }
}
