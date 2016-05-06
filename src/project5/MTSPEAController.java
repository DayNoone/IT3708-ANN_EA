package project5;

import general.AbstractHypothesis;
import general.Pair;
import general.Values;

import java.util.*;

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
            case GENERATION_MIXING:
                List<MTSPHypothesis> allHypothesis = new ArrayList<>();
                allHypothesis.addAll(adults);
                allHypothesis.addAll(population);
                adults.clear();

                calculateRanks(allHypothesis);
                calculateCrowdingDistance(allHypothesis);

                Collections.sort(allHypothesis);

                while (adults.size() < Values.MAX_ADULT_SIZE) {
                    MTSPHypothesis hyp = allHypothesis.remove(0);
                    adults.add(hyp);
                    allHypothesis.remove(hyp);
                }
                break;
        }
    }

    public void parentSelection() {
        parentPairs.clear();
        List<MTSPHypothesis> tempAdults = new ArrayList<>();
        List<MTSPHypothesis> tournamentGroup = new ArrayList<>();

        tempAdults.addAll(adults);

        while (parentPairs.size() < Values.MAX_PARENT_SIZE) {
            tempAdults.clear();
            tempAdults.addAll(adults);

            tournamentGroup.clear();

            //Pick random attendants to tournament
            for (int i = 0; i < Values.TOURNAMENT_SELECTION_GROUP_SIZE; i++) {
                int index = random.nextInt(tempAdults.size());
                MTSPHypothesis tournamentAttendor = tempAdults.get(index);
                tournamentGroup.add(tournamentAttendor);
                tempAdults.remove(tournamentAttendor);
            }

            calculateRanks(tournamentGroup);
            calculateCrowdingDistance(tournamentGroup);

            if (random.nextDouble() >= 1 - Values.TOURNAMENT_SELECTION_EPSILON) {

                MTSPHypothesis bestAttendor = tournamentGroup.get(0);
                MTSPHypothesis secondBestAttendor = tournamentGroup.get(1);
                for (MTSPHypothesis tempAttendor : tournamentGroup) {
                    if (tempAttendor.getRank() > bestAttendor.getRank()) {
                        secondBestAttendor = bestAttendor;
                        bestAttendor = tempAttendor;
                    }
                    else if (tempAttendor.getRank() == bestAttendor.getRank()){
                        if (tempAttendor.getCrowdingDistance() > bestAttendor.getCrowdingDistance()) {
                            secondBestAttendor = bestAttendor;
                            bestAttendor = tempAttendor;
                        }
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

    private static void calculateCrowdingDistance(List<MTSPHypothesis> hyps) {

        for (MTSPHypothesis hyp : hyps){
            hyp.setCrowdingDistance(0);
        }

//        Distance
        hyps.sort(Comparator.comparingInt(MTSPHypothesis::getDistanceFitness));

        hyps.get(0).setCrowdingDistance(Double.NEGATIVE_INFINITY);
        hyps.get(hyps.size()-1).setCrowdingDistance(Double.NEGATIVE_INFINITY);

        for (int i = 1; i < hyps.size() - 1; i++) {
            double numerator = hyps.get(i+1).getDistanceFitness() - hyps.get(i-1).getDistanceFitness();
            double denominator = hyps.get(hyps.size() -1).getDistanceFitness() - hyps.get(0).getDistanceFitness();
            double crowdingDistance = hyps.get(i).getCrowdingDistance() + numerator * 1.0/denominator;
            hyps.get(i).setCrowdingDistance(crowdingDistance);
        }


//        Cost
        hyps.sort(Comparator.comparingInt(MTSPHypothesis::getCostFitness));

        hyps.get(0).setCrowdingDistance(Double.NEGATIVE_INFINITY);
        hyps.get(hyps.size()-1).setCrowdingDistance(Double.NEGATIVE_INFINITY);

        for (int i = 1; i < hyps.size() - 1; i++) {
            double numerator = hyps.get(i+1).getCostFitness() - hyps.get(i-1).getCostFitness();
            double denominator = hyps.get(hyps.size() -1).getCostFitness() - hyps.get(0).getCostFitness();
            double crowdingDistance = hyps.get(i).getCrowdingDistance() + numerator * 1.0/denominator;
            hyps.get(i).setCrowdingDistance(crowdingDistance);
        }

        hyps.sort(Comparator.comparingDouble(MTSPHypothesis::getCrowdingDistance));
    }


    public static void main(String[] args){
        Random random = new Random();
        List<MTSPHypothesis> tournamentGroup = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MTSPHypothesis h1 = new MTSPHypothesis();
            h1.setDistanceFitness(random.nextInt(100));
            h1.setCostFitness(random.nextInt(100));
            tournamentGroup.add(h1);
        }

        calculateCrowdingDistance(tournamentGroup);

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

    public double calculateStandardDeviation(List<MTSPHypothesis> population, double avgFitness) {
        return -1.0;
    }
}
