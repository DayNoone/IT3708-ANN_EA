package project5;

import general.AbstractHypothesis;
import general.EAController;
import general.Pair;
import general.Values;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by markus on 30.04.2016.
 */

public class MTSPEAController extends EAController {

    public void parentSelection() {
        parentPairs.clear();
        List<AbstractHypothesis> tempAdults = new ArrayList<>();
        tempAdults.addAll(adults);
        AbstractHypothesis parent1;
        AbstractHypothesis parent2;

        while (parentPairs.size() < Values.MAX_PARENT_SIZE) {

            //Pick random attendants to tournament
            List<AbstractHypothesis> tournamentGroup = new ArrayList<>();
            for (int i = 0; i < Values.TOURNAMENT_SELECTION_GROUP_SIZE; i++) {
                AbstractHypothesis tournamentAttendor = tempAdults.get(random.nextInt(tempAdults.size()));
                tournamentGroup.add(tournamentAttendor);
                tempAdults.remove(tournamentAttendor);
            }

            calculateRanks(tournamentGroup);

            if (random.nextDouble() >= 1 - Values.TOURNAMENT_SELECTION_EPSILON) {

                AbstractHypothesis bestAttendor = tournamentGroup.get(0);
                AbstractHypothesis secondBestAttendor = tournamentGroup.get(1);

                double bestFitness = bestAttendor.getFitness();



                parentPairs.add(new Pair<>(bestAttendor, secondBestAttendor));

            } else {
                AbstractHypothesis rand1 = tournamentGroup.get(random.nextInt(tournamentGroup.size()));
                tournamentGroup.remove(rand1);
                AbstractHypothesis rand2 = tournamentGroup.get(random.nextInt(tournamentGroup.size()));
                parentPairs.add(new Pair<>(rand1, rand2));
            }
        }
    }

    private static void calculateRanks(List<AbstractHypothesis> tournamentGroup) {
        List<MTSPHypothesis> mtspHypothesises = new ArrayList<>();
        for (AbstractHypothesis hyp : tournamentGroup){
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

        List<AbstractHypothesis> hyps1 = new ArrayList<>();
        hyps1.addAll(hyps);

        calculateRanks(hyps1);
    }
}
