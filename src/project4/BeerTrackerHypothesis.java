package project4;

import general.AbstractHypothesis;
import general.Values;

/**
 * Created by markus on 16.03.2016.
 */
public class BeerTrackerHypothesis extends AbstractHypothesis {

    public BeerTrackerHypothesis() {

        this.setGenotype(new int[Values.CTRANN.getPhenotypeSize()]);
        initiateRandomGenotype();
        generatePhenotype();

    }

    public BeerTrackerHypothesis(int[] newGenotype) {
        this.genotype = newGenotype;
        generatePhenotype();
    }

    @Override
    public void generatePhenotype() {
        int noWeights = Values.CTRANN.getNumberOfNormalNodeWeights();
        int noBias = Values.CTRANN.getNumberOfBiasNodeWeights();
        int noGains = Values.CTRANN.getNumberOfGainValues();
        int noTime = Values.CTRANN.getNumberOfTimeConstantValues();

        this.phenotype = new double[this.genotype.length];
        for (int i = 0; i < this.phenotype.length; i++) {
            if(i < noWeights){
                phenotype[i] = this.genotype[i] + 5.0;
            } else if (i < noWeights + noBias) {
                phenotype[i] = this.genotype[i] + 10.0;
            } else if (i < noWeights + noBias + noGains) {
                double oldRange = (5.0 - 1.0);
                phenotype[i] = (((this.genotype[i] - 1.0) * 10.0) / oldRange);
            } else if (i < noWeights + noBias + noGains + noTime) {
                double oldRange = (2.0 - 1.0);
                phenotype[i] = (((this.genotype[i] - 1.0) * 10.0) / oldRange);
            } else {
                throw new NullPointerException("Generate phenotype: wrong lenght");
            }
        }
    }

    @Override
    public void initiateRandomGenotype() {
        for (int i = 0; i < Values.CTRANN.getPhenotypeSize(); i++) {
            int randomGenotypeInt = random.nextInt(Values.BEERWORLD_GENOTYPE_RANGE);
            getGenotype()[i] = randomGenotypeInt;
        }
    }

    @Override
    public void calculateFitness() {
        Values.CTRANN.setNetworkValues(this.phenotype);

        for (int i = 0; i < Values.FLATLAND_ITERATIONS; i++) {
            int[] sensorValues = Values.BEERWORLD.getSensors();

            double[] moveValues = Values.CTRANN.getMove(sensorValues);
            double moveValue = moveValues[1] - moveValues[0];
            int move;

            if (moveValue > 0.8){
                move = 4;
            } else if (moveValue > 0.6){
                move = 3;
            } else if (moveValue > 0.4){
                move = 2;
            } else if (moveValue > 0.2){
                move = 1;
            } else if (moveValue >= -0.2){
                move = 0;
            } else if (moveValue >= -0.4){
                move = -1;
            } else if (moveValue >= -0.6){
                move = -2;
            } else if (moveValue >= -0.8){
                move = -3;
            } else if (moveValue >= -1){
                move = -4;
            } else {
                move = 0;
            }

            Values.BEERWORLD.playTimestep(move);
        }


        int beerWorldFallenObjects = Values.BEERWORLD.getFallenObjects();
        double fitness;
        if (beerWorldFallenObjects != 0)
            fitness = (1.0 * Values.BEERWORLD.getCaptured()
                    + Values.BEERWORLD.getAvoided()
                    - (Values.BEERWORLD.getFailedAvoid() * Values.BEERWORLD_FAILEDAVOID_PENALTY)
                    - (Values.BEERWORLD.getFailedCapture() * Values.BEERWORLD_FAILEDCAPTURE_PENALTY)) / beerWorldFallenObjects;
        else {
            fitness = 0;
        }
        fitness = Math.max(0, fitness);
        this.setFitness(fitness);
    }


    @Override
    public AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype) {
        return new BeerTrackerHypothesis(genotype);
    }

    @Override
    public AbstractHypothesis instantiateNewChild() {
        return new BeerTrackerHypothesis();
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
