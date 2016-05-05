package project5;

import general.AbstractHypothesis;
import general.Pair;
import general.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by markus on 30.04.2016.
 */

public class MTSPEAController {

    protected Random random = new Random();
    protected List<MTSPHypothesis> population;
    protected ArrayList<MTSPHypothesis> adults;
    protected ArrayList<Pair> parentPairs;

    public MTSPEAController(){
        adults = new ArrayList<>();
        parentPairs = new ArrayList<>();

    }

    public void generateInitialPopulation(MTSPHypothesis initialObject, int sizeOfGeneration) {
        List<MTSPHypothesis> initialPopulation = new ArrayList<>();
        for (int i = 0; i < sizeOfGeneration; i++) {
            MTSPHypothesis hypothesis = initialObject.instantiateNewChild();
            initialPopulation.add(hypothesis);
        }
        this.population = initialPopulation;
    }

    public void generatePhenotypes() {
        population.forEach(MTSPHypothesis::generatePhenotype);
    }

    public void testAndUpdateFitnessOfPhenotypes() {
        for (MTSPHypothesis hypothesis : population) {
            hypothesis.calculateFitness();
        }
    }

    public void adultSelection() {
        List<MTSPHypothesis> tempPopulation = new ArrayList<>();
        tempPopulation.addAll(population);
        switch (Values.ADULT_SELECTION) {
            case FULL_GENERATION_REPLACEMENT:
                adults.clear();
                adults.addAll(tempPopulation);
                break;
            case OVER_PRODUCTION:
                adults.clear();
                while (adults.size() < Values.MAX_ADULT_SIZE) {
                    MTSPHypothesis hyp = fitnessRoulette(tempPopulation);
                    adults.add(hyp);
                    tempPopulation.remove(hyp);
                }
                break;
            case GENERATION_MIXING:
                List<MTSPHypothesis> allHypothesis = new ArrayList<>();
                allHypothesis.addAll(adults);
                allHypothesis.addAll(population);
                adults.clear();
                while (adults.size() < Values.MAX_ADULT_SIZE) {
                    MTSPHypothesis hyp = fitnessRoulette(allHypothesis);
                    adults.add(hyp);
                    allHypothesis.remove(hyp);
                }
                break;
        }
    }

    public void parentSelection() {
        parentPairs.clear();
        List<MTSPHypothesis> tempAdults = new ArrayList<>();
        tempAdults.addAll(adults);
        MTSPHypothesis parent1;
        MTSPHypothesis parent2;

        while (parentPairs.size() < Values.MAX_PARENT_SIZE) {
            tempAdults.clear();
            tempAdults.addAll(adults);

            //Pick random attendants to tournament
            List<MTSPHypothesis> tournamentGroup = new ArrayList<>();
            for (int i = 0; i < Values.TOURNAMENT_SELECTION_GROUP_SIZE; i++) {
                int index = random.nextInt(tempAdults.size());
                MTSPHypothesis tournamentAttendor = tempAdults.get(index);
                tournamentGroup.add(tournamentAttendor);
                tempAdults.remove(tournamentAttendor);
            }

            calculateRanks(tournamentGroup);

            if (random.nextDouble() >= 1 - Values.TOURNAMENT_SELECTION_EPSILON) {

                MTSPHypothesis bestAttendor = tournamentGroup.get(0);
                MTSPHypothesis secondBestAttendor = tournamentGroup.get(1);
                for (MTSPHypothesis tempAttendor : tournamentGroup) {
                    if (tempAttendor.getRank() > bestAttendor.getRank()) {
                        secondBestAttendor = bestAttendor;
                        bestAttendor = tempAttendor;
                    }
                }
                parentPairs.add(new Pair<>(bestAttendor, secondBestAttendor));
            } else {
                MTSPHypothesis rand1 = tournamentGroup.get(random.nextInt(tournamentGroup.size()));
                tournamentGroup.remove(rand1);
                MTSPHypothesis rand2 = tournamentGroup.get(random.nextInt(tournamentGroup.size()));
                parentPairs.add(new Pair<>(rand1, rand2));
            }
        }
    }

    private static void calculateRanks(List<MTSPHypothesis> tournamentGroup) {
        List<MTSPHypothesis> mtspHypothesises = new ArrayList<>();
        for (MTSPHypothesis hyp : tournamentGroup){
            mtspHypothesises.add((MTSPHypothesis) hyp);
        }

        int[] num_solution_for_rank = new int[mtspHypothesises.size()];

        int solution_counter = 0;
        while (solution_counter < mtspHypothesises.size()){

            int dominatedCount = calculateNumDominates(mtspHypothesises.get(solution_counter), mtspHypothesises);

            mtspHypothesises.get(solution_counter).setRank(dominatedCount);

            num_solution_for_rank[dominatedCount] = num_solution_for_rank[dominatedCount] + 1;

            solution_counter++;
        }
    }

    private static int calculateNumDominates(MTSPHypothesis hyp, List<MTSPHypothesis> mtspHypothesises) {
        int counter = 0;
        for (MTSPHypothesis possibleDominator : mtspHypothesises){
            if (!possibleDominator.equals(hyp) && possibleDominator.dominates(hyp)){
                counter++;
            }
        }
        return counter;
    }

    public void generateNewPopulation() {
        List<MTSPHypothesis> newPopulation = new ArrayList<>();


        for (Pair<MTSPHypothesis, MTSPHypothesis> pair : parentPairs) {
            newPopulation.addAll(generateNewChildren(pair.getElement1(), pair.getElement2()));
        }

        for (int i = 0; i < Values.NUMBER_OF_ELITES; i++) {
            MTSPHypothesis elite = getBestHypothesis(population);
            population.remove(elite);
            newPopulation.add(elite);
        }

        population.clear();
        population.addAll(newPopulation);
    }

    public double calculateAvarageFitness(List<MTSPHypothesis> hypothesises) {
        double totalFitness = getTotalFitnessFromIHypothesis(hypothesises);
        return totalFitness / hypothesises.size();
    }


    public MTSPHypothesis getBestHypothesis(List<MTSPHypothesis> hypothesises) {
        calculateRanks(hypothesises);
        MTSPHypothesis bestHyp = hypothesises.get(0);
        for(MTSPHypothesis hyp : hypothesises) {
            if (hyp.getRank() < bestHyp.getRank()) {
                bestHyp = hyp;
            }
        }
        return bestHyp;
    }

    public MTSPHypothesis getWorstHypothesis(List<MTSPHypothesis> hypothesises) {
        calculateRanks(hypothesises);
        MTSPHypothesis worstHyp = hypothesises.get(0);
        for(MTSPHypothesis hyp : hypothesises){
            if (hyp.getRank() > worstHyp.getRank()){
                worstHyp = hyp;
            }
        }
        return worstHyp;
    }

    public List<MTSPHypothesis> getPopulation() {
        return population;
    }

    protected MTSPHypothesis fitnessRoulette(List<MTSPHypothesis> hypothesises) {
        double totalFitness = getTotalFitnessFromIHypothesis(hypothesises);
        double x = totalFitness * random.nextDouble();
        for (MTSPHypothesis hyp : hypothesises) {
            x -= hyp.getFitness();
            if (x <= 0) {
                return hyp;
            }
        }
        return null;
    }

    private List<MTSPHypothesis> generateNewChildren(MTSPHypothesis parent1, MTSPHypothesis parent2) {
        List<MTSPHypothesis> children = new ArrayList<>();
        if (random.nextDouble() <= Values.CROSSOVER_PROBABILITY) {
            children.addAll(parent1.crossover(parent1, parent2));
            children.forEach(MTSPHypothesis::mutate);
        } else {
            MTSPHypothesis child1 = parent1.instantiateNewChileWithGenoType(parent1.getGenotype());
            MTSPHypothesis child2 = parent2.instantiateNewChileWithGenoType(parent2.getGenotype());
            children.add(child1);
            children.add(child2);
        }

        return children;
    }

    private double getTotalFitnessFromIHypothesis(List<MTSPHypothesis> hypothesises) {
        double sum = 0;
        for (MTSPHypothesis hyp : hypothesises) {
            sum += hyp.getFitness();
        }
        return sum;
    }


    public static void main(String[] args){
        Random rand = new Random();

        MTSPHypothesis h1 = new MTSPHypothesis();
        MTSPHypothesis h2 = new MTSPHypothesis();
        MTSPHypothesis h3 = new MTSPHypothesis();
        MTSPHypothesis h4 = new MTSPHypothesis();
        MTSPHypothesis h5 = new MTSPHypothesis();

        List<MTSPHypothesis> hyps = new ArrayList<>();
        hyps.add(h1);
        hyps.add(h2);
        hyps.add(h3);
        hyps.add(h4);
        hyps.add(h5);

        for (MTSPHypothesis hypothesis : hyps){
            hypothesis.setCostFitness(rand.nextInt(10));
            hypothesis.setDistanceFitness(rand.nextInt(10));
        }

        List<MTSPHypothesis> hyps1 = new ArrayList<>();
        hyps1.addAll(hyps);

        calculateRanks(hyps1);
    }

    public double calculateStandardDeviation(List<MTSPHypothesis> population, double avgFitness) {
        return -1.0;
    }
}
