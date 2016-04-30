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
        int[] genotype = new int[Values.MTSP_NUMBER_OF_CITIES];
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
        List<AbstractHypothesis> children = new ArrayList<>();

        int crossOverPoint = random.nextInt(parent1.getGenotype().length);
        int crossOverLength = random.nextInt(parent1.getGenotype().length - 2) + 1;
        crossOverPoint = 2;
        crossOverLength = 3;

        int[] newGenotype1 = instantiateIntArray(parent1.getGenotype());
        int[] newGenotype2 = instantiateIntArray(parent2.getGenotype());
        int oldValue;
        for (int i = 0; i < parent1.getGenotype().length; i++) {
            if (i >= crossOverPoint && i < crossOverPoint + crossOverLength) {
                oldValue = newGenotype1[i];
                newGenotype1[i] = parent2.getGenotype()[i];
                newGenotype1 = repairCrossover(newGenotype1, oldValue, i);

                oldValue = newGenotype2[i];
                newGenotype2[i] = parent1.getGenotype()[i];
                newGenotype2 = repairCrossover(newGenotype2, oldValue, i);
            }
        }

        checkForDuplicate(newGenotype1);
        checkForDuplicate(newGenotype2);

        int[] p1 = parent1.getGenotype();
        int[] p2 = parent2.getGenotype();
        AbstractHypothesis child1 = parent1.instantiateNewChileWithGenoType(newGenotype1);
        AbstractHypothesis child2 = parent2.instantiateNewChileWithGenoType(newGenotype2);
        children.add(child1);
        children.add(child2);
        return children;
    }

    private void checkForDuplicate(int[] newGenotype1) {
        for(int x = 0; x < newGenotype1.length - 1; x++){
            for(int y = x + 1; y < newGenotype1.length; y++){
                if(newGenotype1[x] == newGenotype1[y]){
                    System.out.println("Duplicate cities in genotype");
                    try {
                        throw new Exception("Duplicate cities in genotype");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private int[] instantiateIntArray(int[] genotype) {
        int[] newGenotype = new int[genotype.length];
        for(int i = 0; i < genotype.length; i++){
            newGenotype[i] = genotype[i];
        }
        return newGenotype;
    }

    private int[] repairCrossover(int[] newGenotype, int oldValue, int i) {
        for(int j = 0; j < newGenotype.length; j++){
            if (newGenotype[i] == newGenotype[j] && i != j) {
                newGenotype[j] = oldValue;
                return newGenotype;
            }
        }
        return null;
    }
}
