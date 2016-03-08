import project2.enums.ESurprisingSequenceMode;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by markus on 23.02.2016.
 */
public class SurprisingSequencesHypothesis extends AbstractHypothesis {

    private int numberOfBitsInProblem;
    private String phenoTypeToString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public SurprisingSequencesHypothesis(int numberOfBitsInProblem) {
        this.numberOfBitsInProblem = numberOfBitsInProblem;
        this.setGenotype(new int[numberOfBitsInProblem]);
        this.phenotype = new int[numberOfBitsInProblem];
    }

    public SurprisingSequencesHypothesis(int[] genotype) {
        this.genotype = genotype;
        this.phenotype = new int[this.getGenotype().length];
    }



    @Override
    void initiateRandomGenotype() {
        for (int i = 0; i < numberOfBitsInProblem; i++) {
            int randomInt = random.nextInt(Values.SURPRISING_SYMBOL_SIZE);
            getGenotype()[i] = randomInt;
        }
    }

    @Override
    void calculateFitness() {
        if (Values.SURPRISING_MODE == ESurprisingSequenceMode.Local){
            int localCollisionsCounter = calculateLocalCollisions();
//            this.fitness = 1.0 - localCollisionsCounter / (this.phenotype.length - 1.0);
            this.fitness = 1.0 / (1.0 + localCollisionsCounter);
        }else{
            int globalCollisionsCounter = calculateGlobalCollisions();
            this.fitness = 1.0 / (1.0 + globalCollisionsCounter);
        }
    }

    private int calculateGlobalCollisions() {
        Map<Integer, Map<Integer, Set<Integer>>> mainHashTable = new HashMap<>();
        int globalCollisionsCounter = 0;

        for (int firstNumberIterator = 0; firstNumberIterator < getPhenotype().length - 1; firstNumberIterator++) {
            for (int secondNumberIterator = firstNumberIterator + 1; secondNumberIterator < getPhenotype().length; secondNumberIterator++) {

                int firstNumber = getPhenotype()[firstNumberIterator];
                int secondNumber = getPhenotype()[secondNumberIterator];

                int distance = secondNumberIterator - firstNumberIterator - 1;

                if (mainHashTable.get(firstNumber) == null){
                    Set<Integer> integerSet = new HashSet<>();

                    integerSet.add(distance);

                    Map<Integer, Set<Integer>> innerHashMap = new HashMap<>();
                    innerHashMap.put(secondNumber, integerSet);

                    mainHashTable.put(firstNumber, innerHashMap);
                }

                else if(mainHashTable.get(firstNumber).get(secondNumber) == null){
                    Set<Integer> integerSet = new HashSet<>();

                    integerSet.add(distance);

                    mainHashTable.get(firstNumber).put(secondNumber, integerSet);
                }

                else {
                    Set<Integer> integerSet = mainHashTable.get(firstNumber).get(secondNumber);
                    if (integerSet.contains(distance)) {
                        globalCollisionsCounter += 1;
                    } else{
                        integerSet.add(distance);
                    }

                }
            }
        }
        return globalCollisionsCounter;
    }

    private int calculateLocalCollisions() {
        Map<Integer, Map<Integer, Set<Integer>>> mainHashTable = new HashMap<>();

        int localCollisionsCounter = 0;

        for (int firstNumberIterator = 0; firstNumberIterator < getPhenotype().length - 1; firstNumberIterator++) {
                int secondNumberIterator = firstNumberIterator + 1;
                int firstNumber = getPhenotype()[firstNumberIterator];
                int secondNumber = getPhenotype()[secondNumberIterator];

                int distance = secondNumberIterator - firstNumberIterator - 1;

                if (mainHashTable.get(firstNumber) == null){
                    Set<Integer> integerSet = new HashSet<>();

                    integerSet.add(distance);

                    Map<Integer, Set<Integer>> innerHashMap = new HashMap<>();
                    innerHashMap.put(secondNumber, integerSet);

                    mainHashTable.put(firstNumber, innerHashMap);
                }

                else if(mainHashTable.get(firstNumber).get(secondNumber) == null){
                    Set<Integer> integerSet = new HashSet<>();

                    integerSet.add(distance);

                    mainHashTable.get(firstNumber).put(secondNumber, integerSet);
                }

                else {
                    Set<Integer> integerSet = mainHashTable.get(firstNumber).get(secondNumber);
                    if (integerSet.contains(distance)) {
                        localCollisionsCounter += 1;
                    } else{
                        integerSet.add(distance);
                    }

                }
        }
        return localCollisionsCounter;
    }

    @Override
    AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype) {
        return new SurprisingSequencesHypothesis(genotype);
    }

    @Override
    AbstractHypothesis instantiateNewChild() {
        return new SurprisingSequencesHypothesis(numberOfBitsInProblem);
    }

    @Override
    String getPhenotypeString() {
        String phenoTypeString = "";
        phenoTypeString += String.valueOf(this.phenotype[0]);
        int[] phenotype1 = this.phenotype;
        for (int i = 1; i < phenotype1.length; i++) {
            int aPhenotype = phenotype1[i];
//            phenoTypeString += phenoTypeToString.charAt(aPhenotype);
            phenoTypeString += ", ";
            phenoTypeString += String.valueOf(aPhenotype);
        }
        return phenoTypeString;
    }

    @Override
    void mutate() {
        if (random.nextDouble() < Values.MUTATION_PROBABILITY) {
            for (int i = 0; i < this.getGenotype().length; i++) {
                this.getGenotype()[i] = random.nextInt(Values.SURPRISING_SYMBOL_SIZE);
            }
        } else {
            for (int i = 0; i < this.getGenotype().length; i++) {
                if (random.nextDouble() < Values.MUTATION_PROBABILITY) {
                    this.getGenotype()[i] = random.nextInt(Values.SURPRISING_SYMBOL_SIZE);
                }
            }
        }
    }
}
