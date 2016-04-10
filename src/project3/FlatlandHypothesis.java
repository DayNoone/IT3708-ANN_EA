package project3;

import enums.BoardElement;
import general.AbstractHypothesis;
import general.Values;

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
    public void generatePhenotype() {
        this.phenotype = new double[this.genotype.length];
        for (int i = 0; i < this.phenotype.length; i++) {
            int tempGEnoVal = this.genotype[i];
//            double tempPhenotypeValue = 1.0 * tempGEnoVal / Values.FLATLAND_GENOTYPE_RANGE;
            double tempPhenotypeValue = convertToNewRange(tempGEnoVal, Values.FLATLAND_GENOTYPE_RANGE, 0, 5.0, -5.0);
            this.phenotype[i] = tempPhenotypeValue;
        }
    }

    @Override
    public void initiateRandomGenotype() {
        for (int i = 0; i < Values.ANN.getNumberOfWeights(); i++) {
            int randomGenotypeInt = random.nextInt(Values.FLATLAND_GENOTYPE_RANGE);
            getGenotype()[i] = randomGenotypeInt;
        }
    }

    @Override
    public void calculateFitness() {
        if(Values.FLATLAND_DIFFERENT_SCENARIOS){
            for(Board board: Values.BOARDS){
                board.resetBoard();
            }
            Values.ANN.setNetworkWeights(this.phenotype);

            double[] allFitness = new double[Values.FLATLAND_NUMBER_OF_DIFFERENT_SCENARIOS];

            for (int boardIndex = 0; boardIndex < Values.FLATLAND_NUMBER_OF_DIFFERENT_SCENARIOS; boardIndex++){
                for (int i = 0; i < Values.FLATLAND_ITERATIONS; i++) {
                    ArrayList<BoardElement> sensorValues = Values.BOARDS[boardIndex].getSensors();

                    int highestIndex = Values.ANN.getMove(sensorValues);

                    if (highestIndex == 0){
                        Values.BOARDS[boardIndex].moveForeward();
                    }else if (highestIndex == 1){
                        Values.BOARDS[boardIndex].moveLeft();
                    }
                    else if (highestIndex == 2){
                        Values.BOARDS[boardIndex].moveRight();
                    }
                }
                int flatlandMaxFoodCount = Values.FLATLAND_MAX_FOOD_COUNT;
                allFitness[boardIndex] = (1.0 * Values.BOARDS[boardIndex].getFoodEaten() - Values.POISON_PENALTY * Values.BOARDS[boardIndex].getPoisonEaten()) / flatlandMaxFoodCount;

            }
            double avgFitness = 0.0;
            for(double fitness: allFitness){
                avgFitness += fitness;
            }
            avgFitness = avgFitness / Values.FLATLAND_NUMBER_OF_DIFFERENT_SCENARIOS;

            double max = Math.max(0, avgFitness);
            this.setFitness(max);

        } else {
            Values.BOARD.resetBoard();
            Values.ANN.setNetworkWeights(this.phenotype);

            for (int i = 0; i < Values.FLATLAND_ITERATIONS; i++) {
                ArrayList<BoardElement> sensorValues = Values.BOARD.getSensors();

                int highestIndex = Values.ANN.getMove(sensorValues);

                if (highestIndex == 0){
                    Values.BOARD.moveForeward();
                }else if (highestIndex == 1){
                    Values.BOARD.moveLeft();
                }
                else if (highestIndex == 2){
                    Values.BOARD.moveRight();
                }
            }

            int flatlandMaxFoodCount = Values.FLATLAND_MAX_FOOD_COUNT;
            double fitness = (1.0 * Values.BOARD.getFoodEaten() - Values.POISON_PENALTY * Values.BOARD.getPoisonEaten()) / flatlandMaxFoodCount;
            double max = Math.max(0, fitness);
            this.setFitness(max);
        }
    }

    private double convertToNewRange(int oldValue, double oldMax, double oldMin, double newMax, double newMin) {
        double oldRange = (oldMax - oldMin);
        double newRange = (newMax - newMin);
        return (((oldValue - oldMin) * newRange) / oldRange) + newMin;
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
