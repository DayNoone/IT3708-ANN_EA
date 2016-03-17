/**
 * Created by markus on 16.03.2016.
 */
public class FlatlandHypothesis extends AbstractHypothesis {

    public FlatlandHypothesis() {

        this.setGenotype(new int[Values.ANN.getNumberOfWeights()]);
        this.phenotype = new int[Values.ANN.getNumberOfWeights()];

    }

    public FlatlandHypothesis(int[] newGenotype) {
        this.genotype = newGenotype;
        this.phenotype = new int[Values.ANN.getNumberOfWeights()];
    }

    @Override
    void initiateRandomGenotype() {
        for (int i = 0; i < Values.ANN.getNumberOfWeights(); i++) {
            int randomGenotypeInt = random.nextInt(Values.FLATLAND_GENOTYPE_RANGE);
            getGenotype()[i] = randomGenotypeInt;
        }
    }

    @Override
    public void calculateFitness() {
        double sumOfOnes = 0.0;
        int[] phenotype1 = this.phenotype;
        for (int i = 0; i < phenotype1.length; i++) {
            int aPhenotype = phenotype1[i];
            if (aPhenotype == 1) {
                sumOfOnes += 1;
            }
        }
        setFitness(sumOfOnes / Values.ANN.getNumberOfWeights());
    }

    @Override
    public AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype) {
        return new FlatlandHypothesis(genotype);
    }

    @Override
    public AbstractHypothesis instantiateNewChild() {
        return new FlatlandHypothesis();
    }

    @Override
    public void mutate() {
        if (random.nextDouble() < 0.01) {
            for (int i = 0; i < this.getGenotype().length; i++) {
                this.getGenotype()[i] = this.getGenotype()[i] == 0 ? 1 : 0;
            }
        } else {
            for (int i = 0; i < this.getGenotype().length; i++) {
                if (random.nextDouble() < Values.MUTATION_PROBABILITY) {
                    this.getGenotype()[i] = this.getGenotype()[i] == 0 ? 1 : 0;
                }
            }
        }

    }
}
