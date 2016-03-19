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
        Values.BOARD.resetBoard();

        for (int i = 0; i < Values.FLATLAND_ITERATIONS; i++) {
            double[] outputLayer = Values.ANN.getMove(Values.BOARD.getSensors());

            int highestIndex = findHighestIndex(outputLayer);

            if (highestIndex == 0){
                Values.BOARD.moveForeward();
            }else if (highestIndex == 1){
                Values.BOARD.moveLeft();
            }
            else if (highestIndex == 2){
                Values.BOARD.moveRight();
            }
        }

        this.setFitness((Values.BOARD.getFoodEaten() - Values.POISON_PENALTY * Values.BOARD.getPoisonEaten()) / Values.BOARD.getFoodEaten());

        //TODO: Calcualte fitness

//        double sumOfOnes = 0.0;
//        int[] phenotype1 = this.phenotype;
//        for (int i = 0; i < phenotype1.length; i++) {
//            int aPhenotype = phenotype1[i];
//            if (aPhenotype == 1) {
//                sumOfOnes += 1;
//            }
//        }
//        setFitness(sumOfOnes / Values.ANN.getNumberOfWeights());


    }

    private int findHighestIndex(double[] outputLayer) {
        int highestIndex = 0;
        double highestValue = outputLayer[0];
        for (int i = 1; i < outputLayer.length; i++) {
            if(outputLayer[i] > highestValue){
                highestIndex = i;
                highestValue = outputLayer[i];
            }
        }
        return highestIndex;
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
    void mutate() {
        if (random.nextDouble() < Values.MUTATION_PROBABILITY) {
            for (int i = 0; i < this.getGenotype().length; i++) {
                this.getGenotype()[i] = random.nextInt(Values.FLATLAND_GENOTYPE_RANGE);
            }
        } else {
            for (int i = 0; i < this.getGenotype().length; i++) {
                if (random.nextDouble() < Values.MUTATION_PROBABILITY) {
                    this.getGenotype()[i] = random.nextInt(Values.FLATLAND_GENOTYPE_RANGE);
                }
            }
        }
    }
}
