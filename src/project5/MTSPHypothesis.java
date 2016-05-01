package project5;

import general.Values;

import java.util.*;

/**
 * Created by dagih on 29.04.2016.
 */
@SuppressWarnings("WeakerAccess")
public class MTSPHypothesis{

    private int distanceFitness;

    private int costFitness;

    private int rank;

    protected Random random = new Random();
    protected int[] genotype;
    protected double[] phenotype;
    protected double exceptedValue;

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

    public void generatePhenotype() {
        this.phenotype = new double[this.genotype.length];
        for (int i = 0; i < this.genotype.length; i++) {
            this.phenotype[i] = (double)this.genotype[i];
        }
    }

    public void initiateRandomGenotype() {
        ArrayList<Integer> pool = new ArrayList<>();
        for(int i = 0; i < Values.MTSP_NUMBER_OF_CITIES; i++){
            pool.add(i);
        }
        int[] genotype = new int[Values.MTSP_NUMBER_OF_CITIES];
        for(int j = 0; j < Values.MTSP_NUMBER_OF_CITIES; j++){
            genotype[j] = pool.remove(random.nextInt(pool.size()));
        }
        setGenotype(genotype);
    }

    public void calculateFitness() {
        this.setDistanceFitness(sumMatrix(Values.MTSP_DISTANCES));
        this.setCostFitness(sumMatrix(Values.MTSP_COSTS));
    }

    private int sumMatrix(int[][] matrix) {
        int sum = 0;
        int[] genotype = getGenotype();
        for(int i = 0; i < genotype.length - 1; i++){
            sum += matrix[genotype[i]][genotype[i+1]];
        }
        sum += matrix[genotype[genotype.length-1]][genotype[0]];
        return sum;
    }

    public MTSPHypothesis instantiateNewChileWithGenoType(int[] genotype) {

        return new MTSPHypothesis(genotype);
    }

    public MTSPHypothesis instantiateNewChild() {

        return new MTSPHypothesis();
    }

    public void mutate() {
        ArrayList<Integer> newGenotype = instansiateArrayList(getGenotype());
        ArrayList<Integer> interval = new ArrayList<>();

        /** INVERSION MUTATION **/
        int start = random.nextInt(Values.MTSP_NUMBER_OF_CITIES);
        int stop = random.nextInt(Values.MTSP_NUMBER_OF_CITIES);

        if(start > stop){
            int temp = start;
            start = stop;
            stop = temp;
        }
        int intervalLength = stop - start;
        int insertionPoint = random.nextInt(Values.MTSP_NUMBER_OF_CITIES - intervalLength);

        for (int i = 0; i < intervalLength; i++) {
            interval.add(newGenotype.remove(start));
        }

        for(int i = 0, j = interval.size() - 1; i < j; i++) {
            interval.add(i, interval.remove(j));
        }

        /** DISPLACEMENT MUTATION **/
        for (int i = 0; i < interval.size(); i++) {
            /** ONLY INVERSION NO DISPLACEMENT **/
            newGenotype.add(start + i, interval.get(i));
            /** DISPLACEMENT **/
            //newGenotype.add(insertionPoint + i, interval.get(i));
        }

        int[] mutatedGenotypeArray = new int[newGenotype.size()];
        for (int i = 0; i < newGenotype.size(); i++) {
            mutatedGenotypeArray[i] = newGenotype.get(i);
        }
        checkForDuplicate(mutatedGenotypeArray);
        setGenotype(mutatedGenotypeArray);

    }

    public List<MTSPHypothesis> crossover(MTSPHypothesis parent1, MTSPHypothesis parent2) {
        List<MTSPHypothesis> children = new ArrayList<>();

        int crossOverPoint = random.nextInt(parent1.getGenotype().length);
        int crossOverLength = random.nextInt(parent1.getGenotype().length - 2) + 1;
        int[] newGenotype1 = instantiateIntArray(parent1.getGenotype());
        int[] newGenotype2 = instantiateIntArray(parent2.getGenotype());
        int oldValue;
        for (int i = 0; i < parent1.getGenotype().length; i++) {
            if (i >= crossOverPoint && i < crossOverPoint + crossOverLength) {
                oldValue = newGenotype1[i];
                newGenotype1[i] = parent2.getGenotype()[i];
                newGenotype1 = swapOldValue(newGenotype1, oldValue, i);

                oldValue = newGenotype2[i];
                newGenotype2[i] = parent1.getGenotype()[i];
                newGenotype2 = swapOldValue(newGenotype2, oldValue, i);
            }
        }

        checkForDuplicate(newGenotype1);
        checkForDuplicate(newGenotype2);

        MTSPHypothesis child1 = parent1.instantiateNewChileWithGenoType(newGenotype1);
        MTSPHypothesis child2 = parent2.instantiateNewChileWithGenoType(newGenotype2);
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

    private ArrayList<Integer> instansiateArrayList(int[] array){
        ArrayList<Integer> newArrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            newArrayList.add(array[i]);
        }
        return newArrayList;
    }

    private int[] swapOldValue(int[] newGenotype, int oldValue, int i) {
        for(int j = 0; j < newGenotype.length; j++){
            if (newGenotype[i] == newGenotype[j] && i != j) {
                newGenotype[j] = oldValue;
                return newGenotype;
            }
        }
        return newGenotype;
    }

    public int getCostFitness() {
        return costFitness;
    }

    public int getDistanceFitness() {
        return distanceFitness;
    }

    public int getRank() {
            return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setDistanceFitness(int distanceFitness) {
        this.distanceFitness = distanceFitness;
    }

    public void setCostFitness(int costFitness) {
        this.costFitness = costFitness;
    }

    public boolean dominates(MTSPHypothesis hyp) {
        if (this.getCostFitness() > hyp.getCostFitness() || this.getDistanceFitness() > hyp.getDistanceFitness()){
            return false;
        }
        else
            return this.getCostFitness() < hyp.getCostFitness() || this.getDistanceFitness() < hyp.getDistanceFitness();

    }

    public String getPhenotypeString() {
        String phenoTypeString = "";
        for (double aPhenotype : this.phenotype) {
            phenoTypeString += " " + String.valueOf((int) aPhenotype);
        }
        return phenoTypeString;
    }

    public double getExpectedValue() {
        return this.exceptedValue;
    }

    public void setExpectedValue(double i) {
        this.exceptedValue = i;
    }


    public int[] getGenotype() {
        return genotype;
    }

    public void setGenotype(int[] genotype) {
        this.genotype = genotype;
    }

    public double[] getPhenotype() {

        return phenotype;
    }

    public double getFitness() {
        return getCostFitness() + getDistanceFitness();
    }
}
