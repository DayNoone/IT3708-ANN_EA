import enums.BoardElement;

import java.util.ArrayList;

/**
 * Created by markus on 16.03.2016.
 */
public class FlatlandHypothesis extends AbstractHypothesis {

    public FlatlandHypothesis() {

        this.setGenotype(new int[Values.ANN.getNumberOfWeights()]);
        initiateRandomGenotype();
        generatePhenotype();

    }

    public FlatlandHypothesis(int[] newGenotype) {
        this.genotype = newGenotype;
        generatePhenotype();
    }

    @Override
    void generatePhenotype() {
        this.phenotype = new double[this.genotype.length];
        for (int i = 0; i < this.phenotype.length; i++) {
            int tempGEnoVal = this.genotype[i];
            double tempPhenotypeValue = 1.0 * tempGEnoVal / Values.FLATLAND_GENOTYPE_RANGE;
            this.phenotype[i] = tempPhenotypeValue;
        }
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
        Values.ANN.setNetworkWeights(this.phenotype);

        for (int i = 0; i < Values.FLATLAND_ITERATIONS; i++) {
            ArrayList<BoardElement> sensorValues = Values.BOARD.getSensors();
            double[] outputLayer = Values.ANN.getMove(sensorValues);

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

        double fitness = (1.0 * Values.BOARD.getFoodEaten() - Values.POISON_PENALTY * Values.BOARD.getPoisonEaten()) / Values.FLATLAND_MAX_FOOD_COUNT;
        this.setFitness(fitness);
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
